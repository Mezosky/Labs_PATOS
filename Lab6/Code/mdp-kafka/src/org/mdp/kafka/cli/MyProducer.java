package org.mdp.kafka.cli;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.mdp.kafka.def.KafkaConstants;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

public class MyProducer {
	public static final String[] EARTHQUAKE_SUBSTRINGS = new String[] { "terremoto", "temblor", "sismo", "quake" };
	public static final int FIFO_SIZE = 50; // detect this number in a window
	public static final int WARNING_WINDOW_SIZE = 50000; // create warning for this window
	public static final int CRITICAL_WINDOW_SIZE = 25000; // create critical message for this window
	public static final SimpleDateFormat TWITTER_DATE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static void main(String[] args) throws FileNotFoundException, IOException{
		if(args.length==0 || args.length>2){
			System.err.println("Usage [inputTopic] [outputTopic]");
			return;
		}
		String outputTopic = args[1];
		Properties props = KafkaConstants.PROPS;
		/*
		if(args.length==2){ // if we should replay stream from the start
			// randomise consumer ID for kafka doesn't track where it is
			props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString()); 
			// tell kafka to replay stream for new consumers
			props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		}
		*/
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
		consumer.subscribe(Arrays.asList(args[0]));
		Producer<String, String> producer = new KafkaProducer<String, String>(KafkaConstants.PROPS);

		try{
			while (true) {
				// every ten milliseconds get all records in a batch
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10));
				
				// for all records in the batch
				for (ConsumerRecord<String, String> record : records) {
					String lowercase = record.value().toLowerCase();
					
					// check if record value contains keyword
					// (could be optimised a lot)
					for(String ek: EARTHQUAKE_SUBSTRINGS){
						// if so print it out to the console
						if(lowercase.contains(ek)){
							String[] tabs = lowercase.split("\t");
							String idStr = tabs[2];
							long timeData = TWITTER_DATE.parse(tabs[0]).getTime();
							producer.send(new ProducerRecord<String,String>(outputTopic, 0, timeData, idStr, lowercase));
							//System.out.println(record.value());
							//prevents multiple print of the same tweet
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			System.err.print("failure: " + e.getMessage());
		} finally{
			consumer.close();
		}
	}
}
