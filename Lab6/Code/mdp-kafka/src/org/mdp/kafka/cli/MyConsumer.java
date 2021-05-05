package org.mdp.kafka.cli;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.mdp.kafka.def.KafkaConstants;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

public class MyConsumer {
	public static final String[] EARTHQUAKE_SUBSTRINGS = new String[] { "terremoto", "temblor", "sismo", "quake" };
	public static final int FIFO_SIZE = 50; // detect this number in a window
	public static final int WARNING_WINDOW_SIZE = 50000; // create warning for this window
	public static final int CRITICAL_WINDOW_SIZE = 25000; // create critical message for this window
	public static final SimpleDateFormat TWITTER_DATE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static void main(String[] args) throws FileNotFoundException, IOException{
		if(args.length==0 || args.length>1){
			System.err.println("Usage [inputTopic]");
			return;
		}
		LinkedList<String> twits = new LinkedList<String>();
		Properties props = KafkaConstants.PROPS;
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
		consumer.subscribe(Arrays.asList(args[0]));
		boolean burst50, burst25;

		try {
			while (true) {
				// every ten milliseconds get all records in a batch
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10));
				burst50 = false;
				burst25 = false;
				// for all records in the batch
				for (ConsumerRecord<String, String> record : records) {
					String lowercase = record.value().toLowerCase();
					twits.addLast(lowercase);
					if (twits.size() == 50) {
						String strFirstTwit = twits.getFirst();
						String strLastTwit = twits.getLast();
						String[] tabsFirstTwit = strFirstTwit.split("\t");
						String[] tabsLastTwit = strLastTwit.split("\t");
						long timeDataFirstTwit = TWITTER_DATE.parse(tabsFirstTwit[0]).getTime();
						long timeDataLastTwit = TWITTER_DATE.parse(tabsLastTwit[0]).getTime();
						long timeDelta = (timeDataLastTwit - timeDataFirstTwit)/1000;
						if (timeDelta <= 25 && !burst25) {
							burst25 = true;
							System.out.print("25: " + strFirstTwit);
						} else {
							burst25 = false;
						}
						if (timeDelta <= 50 && !burst50) {
							burst50 = true;
							System.out.print("50: " + strFirstTwit);
						} else {
							burst50 = false;
						}
						twits.removeFirst();
					}
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			consumer.close();
		}
	}
}
