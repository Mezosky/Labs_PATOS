package org.mdp.kafka.cli;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.mdp.kafka.def.KafkaConstants;

public class MyConsumer {
	public static final int FIFO_SIZE = 50; // detect this number in a window
	public static final int PARECE_QUE_YA_TERMINO = 1000; // create critical message for this window
	public static final SimpleDateFormat TWITTER_DATE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	public static void main(String[] args) throws IOException, ParseException{
		if(args.length==0 || args.length>2 || (args.length==2 && !args[1].equals("replay"))){
			System.err.println("Usage [inputTopic] [replay]");
			return;
		}
		
		Properties props = KafkaConstants.PROPS;
		if(args.length==2){ // if we should replay stream from the start
			// randomise consumer ID for kafka doesn't track where it is
			props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString()); 
			// tell kafka to replay stream for new consumers
			props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		}
		
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
		consumer.subscribe(Arrays.asList(args[0]));
		LinkedList<String> Minortwits = new LinkedList<String>();
		LinkedList<String> Majortwits = new LinkedList<String>();
		LinkedList<String> BurstMajor = new LinkedList<String>();
		LinkedList<String> BurstMinor = new LinkedList<String>();
		// Booleanos necesarios para marcar las etapas de ra-fa-ga!
		boolean boolMinor, boolMajor;
		int counter = 0;
		try{
			boolMinor = false;
			boolMajor = false;
			while (true) {
				// every ten milliseconds get all records in a batch
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10));
				if(records.isEmpty()) {
					counter++;
				}
				if(counter==PARECE_QUE_YA_TERMINO)
					break;
				// Recorremos todos los elementos del batch
				for (ConsumerRecord<String, String> record : records) {
					counter = 0;
					String lowercase = record.value().toLowerCase();
					// Agregamos a la lista enlazada un twit.
					Minortwits.addLast(lowercase);
					Majortwits.addLast(lowercase);
					// Si la lista alcanza el largo 50 revisamos si existe burst.
					if (Majortwits.size() == FIFO_SIZE) {
						// Obtenemos el primer y ultimo elemento de la lista
						String strFirstTwit = Majortwits.getFirst();
						String strLastTwit = Majortwits.getLast();
						// Separamos los elementos
						String[] tabsFirstTwit = strFirstTwit.split("\t");
						String[] tabsLastTwit = strLastTwit.split("\t");
						// Obtenemos el tiempo en el formato Timestamp
						long timeDataFirstTwit = TWITTER_DATE.parse(tabsFirstTwit[0]).getTime();
						long timeDataLastTwit = TWITTER_DATE.parse(tabsLastTwit[0]).getTime();
						// Realizamos la resta, y transformamos a segundos
						long timeDelta = (timeDataLastTwit - timeDataFirstTwit)/1000;
						// Realizamos condicionales para la busqueda de las rafagas major/minor
						if (timeDelta <= 25 && !boolMajor && timeDelta > 0) {
							boolMajor = true;
							BurstMajor.addLast(strFirstTwit);
							Majortwits.removeFirst();
						} else if (timeDelta > 25 && boolMajor){
							boolMajor = false;
							Majortwits.clear();
							Majortwits.addLast(lowercase);
						} else {
							Majortwits.removeFirst();
						}
					}
					if (Minortwits.size() == FIFO_SIZE) {
						// Obtenemos el primer y ultimo elemento de la lista
						String strFirstTwit = Minortwits.getFirst();
						String strLastTwit = Minortwits.getLast();
						// Separamos los elementos
						String[] tabsFirstTwit = strFirstTwit.split("\t");
						String[] tabsLastTwit = strLastTwit.split("\t");
						// Obtenemos el tiempo en el formato Timestamp
						long timeDataFirstTwit = TWITTER_DATE.parse(tabsFirstTwit[0]).getTime();
						long timeDataLastTwit = TWITTER_DATE.parse(tabsLastTwit[0]).getTime();
						// Realizamos la resta, y transformamos a segundos
						long timeDelta = (timeDataLastTwit - timeDataFirstTwit)/1000;
						// Realizamos condicionales para la busqueda de las rafagas major/minor
						if (timeDelta <= 50 && !boolMinor && timeDelta > 0) {
							boolMinor = true;
							BurstMinor.addLast(strFirstTwit);
							Minortwits.removeFirst();
						} else if (timeDelta > 50 && boolMinor){
							boolMajor = false;
							Minortwits.clear();
							Minortwits.addLast(lowercase);
						} else {
							Minortwits.removeFirst();
						}
					}
				}
			}
			// Imprimimos resultados :D
			String countValue25 = Integer.toString(BurstMajor.size());
			String countValue50 = Integer.toString(BurstMinor.size());
			System.out.print(" \n");
			System.out.print(" $$$$$$\\                                           $$$$$$\\  $$\\   $$\\ \n");
			System.out.print("$$  __$$\\                                         $$ ___$$\\ $$ |  $$ |\n");
			System.out.print("$$ //  \\__| $$$$$$\\  $$\\   $$\\  $$$$$$\\   $$$$$$\\  \\_/   $$ |$$ |  $$ |\n");
			System.out.print("$$ |$$$$\\ $$  __$$\\ $$ |  $$ |$$  __$$\\ $$  __$$\\   $$$$$ / $$$$$$$$ |\n");
			System.out.print("$$ |\\_$$ |$$ |  \\__|$$ |  $$ |$$ /  $$ |$$ /  $$ |  \\___$$\\ \\_____$$ |\n");
			System.out.print("$$ |  $$ |$$ |      $$ |  $$ |$$ |  $$ |$$ |  $$ |$$\\   $$ |      $$ |\n");
			System.out.print("\\$$$$$$  |$$ |      \\$$$$$$  |$$$$$$$  |\\$$$$$$  |\\$$$$$$  |      $$ |\n");
			System.out.print(" \\______/ \\__|       \\______/ $$  ____/  \\______/  \\______/       \\__|\n");
			System.out.print("                              $$ |                                    \n");
			System.out.print("                              $$ |                                    \n");
			System.out.print("                              $$ |                                    \n");
			System.out.print("                              $$ |                                    \n");
			System.out.print("                              \\__|                                    \n");
			System.out.print("##########Major Tweets##########");
			System.out.print(" \n");
			System.out.print(" \n");
			for(String tweet: BurstMajor)
		      {
		    	  System.out.println(tweet);
		    	  System.out.print(" \n");
		      }
			//System.out.print(Majortwits);
			System.out.print(" \n");
			System.out.print(" \n");
			System.out.print("##########Minor Tweets##########");
			System.out.print(" \n");
			System.out.print(" \n");
			for(String tweet: BurstMinor)
		      {
		    	  System.out.println(tweet);
		    	  System.out.print(" \n");
		      }
			//System.out.print(Minortwits);
			System.out.print(" \n");
			System.out.print(" \n");
			System.out.print("##########Resumen##########");
			System.out.print(" \n");
			System.out.print(" \n");
			System.out.print("Fueron encontrados "+ countValue25 + " Major burst.");
			System.out.print(" \n");
			System.out.print("Fueron encontrados "+ countValue50 + " Minor burst.");
			System.out.print(" \n");
			System.out.print(" \n");
		} finally{
			consumer.close();
		}
	}
}
