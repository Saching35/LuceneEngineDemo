package ie.tcd.cs7is3.assignment.two;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.similarities.AxiomaticF1EXP;
import org.apache.lucene.search.similarities.AxiomaticF1LOG;
import org.apache.lucene.search.similarities.AxiomaticF2EXP;
import org.apache.lucene.search.similarities.AxiomaticF2LOG;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.search.similarities.MultiSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import ie.tcd.cs7is3.assignment.two.analyzer.CustomAnalyzer;
import ie.tcd.cs7is3.assignment.two.parser.AllDocParser;
import ie.tcd.cs7is3.assignment.two.query.TopicQuery;
import ie.tcd.cs7is3.assignment.two.query.TopicQueryParser;

public class Main {

	private static final String INDEX_PATH = System.getProperty("user.dir") + "/index";
	private static final String RESULTS_FILE = System.getProperty("user.dir")  + "/Qresult";
	private static final String QUERY_FILE = System.getProperty("user.dir") + "/src/main/resources/queries/topics";
	private static final int NUMBER_OF_RESULTS = 1000;

	public static void main(String[] args) {
		Main main = new Main();
		AllDocParser docParser = new AllDocParser();
		List<Document> docs = docParser.readAllCollections();
		System.out.println("Indexing collection containing " + docs.size() + " documents");
		main.indexDocs(docs);
		main.executeSearch();
		System.out.println("Query have been outputted to " + RESULTS_FILE);
	}

	private void executeSearch() {
		System.out.println("Executing search");
		try {
			Directory dir = FSDirectory.open(Paths.get(INDEX_PATH));
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher searcher = new IndexSearcher(reader);

			String queryString = null;

List<Similarity> simsList = new ArrayList<Similarity>();
			
			Properties prop = new Properties();
			prop.load(new FileInputStream("src/main/resources/Similarity.properties"));
			
			if(prop.getProperty("BM25Similarity") == "1")
				simsList.add(new BM25Similarity());
			if(prop.getProperty("AxiomaticF1EXP") == "1")
				simsList.add(new AxiomaticF1EXP());
			if(prop.getProperty("AxiomaticF1LOG") == "1")
				simsList.add(new AxiomaticF1LOG());
			if(prop.getProperty("AxiomaticF2EXP") == "1")
				simsList.add(new AxiomaticF2EXP());
			if(prop.getProperty("AxiomaticF2LOG") == "1")
				simsList.add(new AxiomaticF2LOG());
			if(prop.getProperty("BooleanSimilarity") == "1")
				simsList.add(new BooleanSimilarity());
			if(prop.getProperty("ClassicSimilarity") == "1")
				simsList.add(new ClassicSimilarity());
			if(prop.getProperty("LMDirichletSimilarity") == "1")
				simsList.add(new LMDirichletSimilarity());
			
			
			Similarity[] sims = null;
			
			MultiSimilarity ms = new MultiSimilarity(sims);
			

			searcher.setSimilarity(ms);

			// Initialize the analyzer with the stop-words
			EnglishAnalyzer analyzer = new EnglishAnalyzer(getStopWords());
			Analyzer canalyzer = new CustomAnalyzer();

			HashMap<String, Float> boosts = new HashMap<String, Float>();
			boosts.put("headline", 0.60f);
			boosts.put("text", 0.40f);

			MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[] { "headline", "text" }, canalyzer,
					boosts);


			parser.setAllowLeadingWildcard(true);
			PrintWriter writer = new PrintWriter(RESULTS_FILE, "UTF-8");

			TopicQueryParser topicQueryParser = new TopicQueryParser();
			List<TopicQuery> queries = topicQueryParser.parseTopicQueries(QUERY_FILE);

			System.out.println("Executing " + queries.size() + " queries");
			for (TopicQuery topic : queries) {

				queryString = topic.createQuery();
				Query query = parser.parse(QueryParser.escape(queryString));

				ScoreDoc[] hits = searcher.search(query, NUMBER_OF_RESULTS).scoreDocs;

				for (int i = 0; i < hits.length; i++) {
					Document hitDoc = searcher.doc(hits[i].doc);
					writer.println(topic.getNumber() + " Q0 " + hitDoc.get("documentNo") + " " + (i + 1) + " "
							+ hits[i].score + " STANDARD");
				}
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void indexDocs(List<Document> documents) {
		FieldType ft = new FieldType(TextField.TYPE_STORED);
		ft.setTokenized(true);
		ft.setStoreTermVectors(true);
		ft.setStoreTermVectorPositions(true);
		ft.setStoreTermVectorOffsets(true);
		ft.setStoreTermVectorPayloads(true);

		try {
			// Initialize the analyzer with the stop-words
			EnglishAnalyzer analyzer = new EnglishAnalyzer(getStopWords());

			Analyzer canalyzer = new CustomAnalyzer();
			IndexWriterConfig config = new IndexWriterConfig(canalyzer);

			Directory directory = FSDirectory.open(Paths.get(INDEX_PATH));
			config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
			IndexWriter iwriter = new IndexWriter(directory, config);

			for(Document doc : documents) {
				iwriter.addDocument(doc);
			}
			iwriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private CharArraySet getStopWords() {
		List<String> stopWords = Arrays.asList("a", "about", "above", "after", "again", "against", "ain", "all", "am",
				"an", "and", "any", "are", "aren", "aren't", "as", "at", "be", "because", "been", "before", "being",
				"below", "between", "both", "but", "by", "can", "couldn", "couldn't", "d", "did", "didn", "didn't",
				"do", "does", "doesn", "doesn't", "doing", "don", "don't", "down", "during", "each", "few", "for",
				"from", "further", "had", "hadn", "hadn't", "has", "hasn", "hasn't", "have", "haven", "haven't",
				"having", "he", "her", "here", "hers", "herself", "him", "himself", "his", "how", "i", "if", "in",
				"into", "is", "isn", "isn't", "it", "it's", "its", "itself", "just", "ll", "m", "ma", "me", "mightn",
				"mightn't", "more", "most", "mustn", "mustn't", "my", "myself", "needn", "needn't", "no", "nor", "not",
				"now", "o", "of", "off", "on", "once", "only", "or", "other", "our", "ours", "ourselves", "out", "over",
				"own", "re", "s", "same", "shan", "shan't", "she", "she's", "should", "should've", "shouldn",
				"shouldn't", "so", "some", "such", "t", "than", "that", "that'll", "the", "their", "theirs", "them",
				"themselves", "then", "there", "these", "they", "this", "those", "through", "to", "too", "under",
				"until", "up", "ve", "very", "was", "wasn", "wasn't", "we", "were", "weren", "weren't", "what", "when",
				"where", "which", "while", "who", "whom", "why", "will", "with", "won", "won't", "wouldn", "wouldn't",
				"y", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves", "could",
				"he'd", "he'll", "he's", "here's", "how's", "i'd", "i'll", "i'm", "i've", "let's", "ought", "she'd",
				"she'll", "that's", "there's", "they'd", "they'll", "they're", "they've", "we'd", "we'll", "we're",
				"we've", "what's", "when's", "where's", "who's", "why's", "would");
		return new CharArraySet(stopWords, true);
	}
}
