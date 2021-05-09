package org.mdp.kafka.cli;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.mdp.kafka.def.KafkaConstants;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class MyProducer {
	public static final String[] EARTHQUAKE_SUBSTRINGS = new String[] { "terremoto", "temblor", "sismo", "quake" };
	public static final SimpleDateFormat TWITTER_DATE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static int TWEET_ID = 2;
	BufferedReader tweets;
	String outputTopic;
	long startSim = 0;
	long startData = 0;
	int speedup = 1000;
	int id = TWEET_ID;

	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.err.println("Usage [gzipped twits] [outputTopic]");
			return;
		}
		BufferedReader tweets = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(args[0]))));
		String outputTopic = args[1];
		MyProducer c = new MyProducer(tweets, outputTopic);
		c.produce();
	}

	MyProducer(BufferedReader tweets, String topic) {
		this.tweets = tweets;
		this.outputTopic = topic;
	}

	public void produce() {
		Thread one = new Thread() {
			public void run() {
				String line;
				long wait = 0;
				boolean isAboutEarthquakes;
				Producer<String, String> producer = new KafkaProducer<String, String>(KafkaConstants.PROPS);
				try{
					while((line = tweets.readLine())!=null){
						String[] tabs = line.split("\t");
						if(tabs.length>id){
							try{
								isAboutEarthquakes = false;
								for(String ek: EARTHQUAKE_SUBSTRINGS) {
									if(line.toLowerCase(Locale.ROOT).contains(ek)){
										isAboutEarthquakes = true;
										break;
									}
								}
								if(!isAboutEarthquakes) continue;
								long timeData = getUnixTime(tabs[0]);
								if(startData == 0)
									startData = timeData;
								wait = calculateWait(timeData);
								String idStr = tabs[id];
								if(wait>0){
									Thread.sleep(wait);
								}
								producer.send(new ProducerRecord<String,String>(outputTopic, 0, timeData, idStr, line));
							} catch(ParseException | NumberFormatException pe){
								System.err.println("Cannot parse date "+tabs[0]);
							}
						}

						if (Thread.interrupted()) {
							throw new InterruptedException();
						}
					}
				} catch(IOException ioe){
					System.err.println(ioe.getMessage());
				} catch(InterruptedException ie){
					System.err.println("Interrupted "+ie.getMessage());
				}

				System.err.println("Finished! Messages were "+wait+" ms from target speed-up times.");
			}
		};
		one.start();
	}
	private long calculateWait(long time) {
		long current = System.currentTimeMillis();

		// how long we have waited since start
		long delaySim = current - startSim;
		if(delaySim<0){
			// the first element ...
			// wait until startSim
			return delaySim*-1;
		}

		// calculate how long we should wait since start
		long delayData = time - startData;
		long shouldDelay = delayData / speedup;

		// if we've already waited long enough
		if(delaySim>=shouldDelay) return 0;
			// otherwise return wait time
		else return shouldDelay - delaySim;
	}

	// example 2017-09-19 00:07:03
	public long getUnixTime(String dateTime) throws ParseException{
		Date d = TWITTER_DATE.parse( dateTime );
		return d.getTime();
	}
}
