package org.mdp.kafka.cli;

import java.io.FileNotFoundException;
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

public class MyConsumer3 {
	public static final String[] EARTHQUAKE_SUBSTRINGS = new String[] { "terremoto", "temblor", "sismo", "quake" };
	public static final int FIFO_SIZE = 50; // detect this number in a window
	public static final int WARNING_WINDOW_SIZE = 50000; // create warning for this window
	public static final int CRITICAL_WINDOW_SIZE = 25000; // create critical message for this window
	public static final SimpleDateFormat TWITTER_DATE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException{
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
		LinkedList<String> twits = new LinkedList<String>();
		LinkedList<String> Minortwits = new LinkedList<String>();
		LinkedList<String> Majortwits = new LinkedList<String>();
		boolean burst50, burst25;
		try{
			while (true) {
				// every ten milliseconds get all records in a batch
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10));
				burst50 = false;
				burst25 = false;
				
				// for all records in the batch
				for (ConsumerRecord<String, String> record : records) {
					String lowercase = record.value().toLowerCase();
					
					// check if record value contains keyword
					// (could be optimized a lot)
					twits.addLast(lowercase);
					if (twits.size() == FIFO_SIZE) {
						String strFirstTwit = twits.getFirst();
						String strLastTwit = twits.getLast();
						String[] tabsFirstTwit = strFirstTwit.split("\t");
						String[] tabsLastTwit = strLastTwit.split("\t");
						long timeDataFirstTwit = TWITTER_DATE.parse(tabsFirstTwit[0]).getTime();
						long timeDataLastTwit = TWITTER_DATE.parse(tabsLastTwit[0]).getTime();
						long timeDelta = (timeDataLastTwit - timeDataFirstTwit)/1000;
						if (timeDelta <= 25 && !burst25 && timeDelta > 0) {
							burst25 = true;
							Minortwits.addLast(strFirstTwit);
						} else if (timeDelta > 25 && burst25  && timeDelta > 0){
							burst25 = false;
						}
						if (timeDelta <= 50 && !burst50  && timeDelta > 0) {
							burst50 = true;
							Majortwits.addLast(strFirstTwit);
						} else if (timeDelta > 50 && burst50  && timeDelta > 0){
							burst50 = false;
						}
						twits.clear();
					}
				}
				String countValue25 = Integer.toString(Majortwits.size());
				String countValue50 = Integer.toString(Minortwits.size());
				System.out.print("##########Major Tweets##########");
				System.out.print(" \n");
				System.out.print(" \n");
				System.out.print(Majortwits);
				System.out.print(" \n");
				System.out.print(" \n");
				System.out.print("##########Minor Tweets##########");
				System.out.print(" \n");
				System.out.print(" \n");
				System.out.print(Minortwits);
				System.out.print(" \n");
				System.out.print(" \n");
				System.out.print("##########Resumen##########");
				System.out.print(" \n");
				System.out.print(" \n");
				System.out.print("Fueron encontrados "+ countValue25 + " Minor burst.");
				System.out.print("Fueron encontrados "+ countValue50 + " Major burst.");
				System.out.print(" \n");
				System.out.print(" \n");
			}
		} finally{
			consumer.close();
		}
	}
}
