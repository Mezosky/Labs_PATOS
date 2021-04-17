package org.mdp.hadoop.cli;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.mdp.hadoop.cli.CountWords.CountWordsMapper;
import org.mdp.hadoop.cli.CountWords.CountWordsReducer;

/**
 * Java class to run a remote Hadoop word count job.
 * 
 * Contains the main method, an inner Reducer class 
 * and an inner Mapper class.
 * 
 * @author Grupo34
 */

public class ActorsTogether {
	
	/**
	 * Usamos line.split(SPLIT_REGEX) y seteamos como
	 * separador el caracter \t, el cual representa un tab.
	 */
	public static String SPLIT_REGEX = "\t";

	/**
	 * This is the Mapper Class. This sends key-value pairs to different machines
	 * based on the key.
	 * 
	 * Remember that the generic is Mapper<InputKey, InputValue, MapKey, MapValue>
	 * 
	 * InputKey we don't care about (a LongWritable will be passed as the input
	 * file offset, but we don't care; we can also set as Object)
	 * 
	 * InputKey will be Text: a line of the file
	 * 
	 * MapKey will be Text: a word from the file
	 * 
	 * MapValue will be IntWritable: a count: emit 1 for each occurrence of the word
	 * 
	 * @author Grupo34
	 *
	 */
	public static class ActorsMapper extends Mapper<Object, Text, Text, Text>{
		
		private Text word1 = new Text();
		private Text word2 = new Text();
		
		/**
		 * Given the offset in bytes of a line (key) and a line (value),
		 * we will output (word,1) for each word in the line.
		 * 
		 * @throws InterruptedException 
		 * 
		 */
		 @Override
		public void map(Object key, Text value, Context output)
						throws IOException, InterruptedException {
			String TM = "THEATRICAL_MOVIE";
			String line = value.toString();
			String[] rows = line.split(SPLIT_REGEX);
			
			if (!rows.equals(null) && TM.equals(rows[4])){
				String star_name = rows[0];
				String movie_name = rows[1];
				String year = rows[2];
				String movie_number = rows[3];
				
				String unique_key = movie_name+"##"+year+"##"+movie_number;
				word1.set(unique_key.toLowerCase());
				word2.set(star_name.toLowerCase());
			}
			output.write(word1, word2);
						}
			}

	/**
	 * This is the Reducer Class.
	 * 
	 * This collects sets of key-value pairs with the same key on one machine. 
	 * 
	 * Remember that the generic is Reducer<MapKey, MapValue, OutputKey, OutputValue>
	 * 
	 * MapKey will be Text: a word from the file
	 * 
	 * MapValue will be IntWritable: a count: emit 1 for each occurrence of the word
	 * 
	 * OutputKey will be Text: the same word
	 * 
	 * OutputValue will be IntWritable: the final count
	 * 
	 * @author Aidan
	 *
	 */
	public static class ActorsReducer extends Reducer<Text, Text, Text, IntWritable> {
		
		private final IntWritable one = new IntWritable(1);
		/**
		 * Given a key (a word) and all values (partial counts) for 
		 * that key produced by the mapper, then we will sum the counts and
		 * output (word,sum)
		 * 
		 * @throws InterruptedException 
		 */
		@Override
		public void reduce(Text key, Iterable<Text> values,
				Context output) throws IOException, InterruptedException {
	
			ArrayList<String> actors = new ArrayList<String>();  
			
			for(Text value:values) {
				if (!actors.contains(value.toString())) {
					actors.add(value.toString());
				}
			}
			Collections.sort(actors);
			for(int it1=0; it1 < actors.size(); it1++) {
				for(int it2=it1+1; it2< actors.size(); it2++) {
					Text actorsTogether = new Text(actors.get(it1)+"##"+actors.get(it2));
					output.write(actorsTogether, one);
				}
			}
		}
	}
	
	public static class ActorsReducerCount extends Reducer<Text, IntWritable, Text, IntWritable> {

		/**
		 * Given a key (a word) and all values (partial counts) for 
		 * that key produced by the mapper, then we will sum the counts and
		 * output (word,sum)
		 * 
		 * @throws InterruptedException 
		 */
		@Override
		public void reduce(Text key, Iterable<IntWritable> values,
				Context output) throws IOException, InterruptedException {
			int sum = 0;
			for(IntWritable value: values) {
				sum += value.get();
			}
			output.write(key, new IntWritable(sum));
		}
	}
	
	

	/**
	 * Main method that sets up and runs the job
	 * 
	 * @param args First argument is input, second is output
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage: "+CountWords.class.getName()+" <in> <out>");
			System.exit(2);
		}
		String inputLocation = otherArgs[0];
		String outputLocation = otherArgs[1];
		
		Job job = Job.getInstance(new Configuration());
	     
	    FileInputFormat.setInputPaths(job, new Path(inputLocation));
	    FileOutputFormat.setOutputPath(job, new Path(outputLocation));
	    
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(IntWritable.class);
	    job.setMapOutputKeyClass(Text.class);
	    job.setMapOutputValueClass(Text.class);
	    
	    job.setMapperClass(ActorsMapper.class);
	    job.setReducerClass(ActorsReducer.class);
	    job.setReducerClass(ActorsReducerCount.class);
	     
	    job.setJarByClass(ActorsTogether.class);
		job.waitForCompletion(true);
	}	
}
