package cl.uchile.pmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.AlreadyBoundException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import cl.uchile.pmd.BuildWikiIndexBulk.FieldNames;

/**
 * Main method to search articles using Elasticsearch.
 * 
 * @author Aidan, Alberto
 */
public class SearchWikiIndex {
	
	// used to keep track of the names of fields
	// that we are using
	public enum FieldNames {
		URL, TITLE, MODIFIED, ABSTRACT, RANK
	}

	// used to assign higher/lower ranking
	// weight to different document fields
	public static final HashMap<String, Float> BOOSTS = new HashMap<String, Float>();
	static {
		BOOSTS.put(FieldNames.ABSTRACT.name(), 1f); // <- default
		BOOSTS.put(FieldNames.TITLE.name(), 5f);
	}

	public static final int DOCS_PER_PAGE = 10;

	public static void main(String args[]) throws IOException, ClassNotFoundException, AlreadyBoundException,
			InstantiationException, IllegalAccessException {
		Option inO = new Option("i", "input elasticsearch index name");
		inO.setArgs(1);
		inO.setRequired(true);

		Options options = new Options();
		options.addOption(inO);

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.err.println("***ERROR: " + e.getClass() + ": " + e.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("parameters:", options);
			return;
		}

		// print help options and return
		if (cmd.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("parameters:", options);
			return;
		}

		TransportClient client = ElasticsearchCluster.getTransportClient();
		
		String indexName = cmd.getOptionValue(inO.getOpt());
		System.err.println("Querying index at  " + indexName);

		startSearchApp(client,indexName);
		
		client.close();
	}

	/**
	 * 
	 * @param inDirectory : the location of the index directory
	 * @throws IOException
	 */
	public static void startSearchApp(TransportClient client, String indexName) throws IOException {
		// we open a UTF-8 reader over std-in
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "utf-8"));

		while (true) {
			System.out.println("Enter a keyword search phrase:");

			// read keyword search from user
			String line = br.readLine();
			if (line != null) {
				line = line.trim();
				if (!line.isEmpty()) {
					try {
						// we will use a multi-match query builder that
						// will allow us to match multiple document fields
						// (e.g., search over title and abstract)
						MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(line,
								FieldNames.TITLE.name(), FieldNames.ABSTRACT.name());
						
						// the boost sets a matching word in a title to have
						// a different ranking weight than other fields
						float titleBoost = BOOSTS.get(FieldNames.TITLE.name());
						multiMatchQueryBuilder.field(FieldNames.TITLE.name(), titleBoost);
						
						//@TODO: add the abstract field to the fields searched
						// the boost sets a matching word in an abstract to have
						// a different ranking weight than other fields
						float abstractBoost = BOOSTS.get(FieldNames.ABSTRACT.name());
						multiMatchQueryBuilder.field(FieldNames.ABSTRACT.name(), abstractBoost);

						// here we run the search, specifying how many results
						// we want per "page" of results
						SearchResponse response = client.prepareSearch(indexName).setQuery(multiMatchQueryBuilder) // Query
								.setSize(DOCS_PER_PAGE).setExplain(true).get();
						
						// for each document in the results ...
						System.out.println(" .d8888b.                                      .d8888b.      d8888  \n");
						System.out.println("d88P  Y88b                                    d88P  Y88b    d8P888  \n");
						System.out.println("888    888                                         .d88P   d8P 888  \n");
						System.out.println("888        888d888 888  888 88888b.   .d88b.      8888\"   d8P  888  \n");
						System.out.println("888  88888 888P\"   888  888 888 \"88b d88\"\"88b      \"Y8b. d88   888  \n");
						System.out.println("888    888 888     888  888 888  888 888  888 888    888 8888888888 \n");
						System.out.println("Y88b  d88P 888     Y88b 888 888 d88P Y88..88P Y88b  d88P       888  \n");
						System.out.println(" \"Y8888P88 888      \"Y88888 88888P\"   \"Y88P\"   \"Y8888P\"        888  \n");
						System.out.println("                            888                                     \n");
						System.out.println("                            888                                     \n");
						System.out.println("                            888                                     \n");
						System.out.println("Title\tURL\tAbstract\n");
						for (SearchHit hit : response.getHits().getHits()) {
							// get the JSON data per field
							Map<String, Object> json = hit.getSourceAsMap();
							String title = (String) json.get(FieldNames.TITLE.name());
							
							//@TODO: get and print the title, url and abstract of each result
							// print the details of the doc (title, url, abstract) to standard out
							String url = (String) json.get(FieldNames.URL.name());
							String abst = (String) json.getOrDefault(FieldNames.ABSTRACT.name(), "");
							System.out.println(title+"\t"+url+"\t"+abst);
							System.out.println("\n");
						}
					} catch (Exception e) {
						System.err.println("Error with query '" + line + "'");
						e.printStackTrace();
					}
				}
			}
		}
	}
}