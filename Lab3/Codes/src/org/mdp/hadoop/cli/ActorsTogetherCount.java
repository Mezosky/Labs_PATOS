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

public class ActorsTogetherCount {
	
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
	public static class CountActorsMapper extends Mapper<Object, Text, Text, IntWritable>{
		
		private Text word = new Text();
		private final IntWritable one = new IntWritable(1);
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
			String line = value.toString();
			
			// Realizamos split para obtener los atributos
			// (star_name, one)
			String[] rows = line.split(SPLIT_REGEX);
			
			// Seteamos solo con el valor que importa.
			String starsTogether = rows[0];
			word.set(starsTogether);
			
			// Guardamos los nombres de las estrellas que trabajan
			// juntas con un contador.
			output.write(word, one);
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
	 * @author Grupo34
	 *
	 */
	public static class CountActorReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

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
	    
	    // Se mantiene igual
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(IntWritable.class);
	    job.setMapOutputKeyClass(Text.class);
	    job.setMapOutputValueClass(IntWritable.class);
	    
	    // Llamamos a los jobs creados
	    job.setMapperClass(CountActorsMapper.class);
	    job.setReducerClass(CountActorReducer.class);
	     
	    job.setJarByClass(ActorsTogetherCount.class);
		job.waitForCompletion(true);
	}	
}
