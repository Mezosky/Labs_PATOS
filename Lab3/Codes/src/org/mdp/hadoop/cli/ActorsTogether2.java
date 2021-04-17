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

public class ActorsTogether2 {
	
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
		
		// Definimos los Text de salida.
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
			
			
			String line = value.toString();
			
			// Realizamos split para obtener los atributos
			// (star, Movie Name, Year, Movie Number, Movie Type, ...)
			String[] rows = line.split(SPLIT_REGEX);
			
			// Definimos el tipo de pelicula que deseamos filtrar y
			// escogemos el atributo que posee este valor
			String TM = "THEATRICAL_MOVIE";
			String type_movie = rows[4];
			
			// Aplicamos condicional para filtrar por el tipo.
			if (TM.equals(type_movie)){
				
				// Creamos la llave en base a la combinación que hace unica
				// a una pelicula y guardamos los nombres de los actores.
				String unique_key = rows[1]+"##"+rows[2]+"##"+rows[3];
				String star_name = rows[0];
				
				// Seteamos los Texts para entregarlos como salida.
				// Pasamos a minuscula en caso de errores en el dataset.
				word1.set(unique_key.toLowerCase());
				word2.set(star_name.toLowerCase());
				
				// (key: combinacion, value: actor)
				output.write(word1, word2);
			}
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
	public static class ActorsReducer extends Reducer<Text, Text, Text, IntWritable> {
		
		// Utilizamos esto solamente porque el Reducer necesita doble salida
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
			
			// Creamos una lista temporal para recorrer todos los actores
			// de una misma pelicula.
			ArrayList<String> actors = new ArrayList<String>();  
			
			// Aprovechamos el agrupamiento del Reducer para guardar en la
			// lista todos los actores de la misma pelicula.
			for(Text value:values) {
				actors.add(value.toString());
			}
			// Este no debería ser necesario pero nos dimos cuenta que habian
			// nombres que no estaban bien ordenados, un ejemplo es el apellido
			// "acosta".
			Collections.sort(actors);
			
			// Recorremos la lista de forma desfasada para guardar todas las combaciones
			// posibles en el archivo de salida.
			for(int it1=0; it1 < actors.size(); it1++) {
				for(int it2=it1+1; it2< actors.size(); it2++) {
					
					// Generamos la combinación solicitada para escribir en el archivo
					Text actorsTogether = new Text(actors.get(it1)+"##"+actors.get(it2));
					
					// Notar que "one" no tiene un util, solo nos importa actorsTogether
					output.write(actorsTogether, one);
				}
			}
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
	    
	    // Se cambian las salidas del mapper y Reducer para el problema.
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(IntWritable.class);
	    job.setMapOutputKeyClass(Text.class);
	    job.setMapOutputValueClass(Text.class);
	    
	    // Cambiamos jobs a los creados
	    job.setMapperClass(ActorsMapper.class);
	    job.setReducerClass(ActorsReducer.class);
	     
	    job.setJarByClass(ActorsTogether2.class);
		job.waitForCompletion(true);
	}	
}
