package LuceneSearch;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import LuceneEngine.DocAnalyzer;
import LuceneEngine.IndexData;
import LuceneEngine.Searching;

public class LuceneSearch {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String similarityModel = "1";
		Similarity sm = new BM25Similarity();
		Properties prop = new Properties();
		try {
		    prop.load(new FileInputStream("similarity/similarity.properties"));
		    similarityModel = prop.getProperty("similarity");
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		switch (similarityModel) {
		case "1": 
			sm = new BM25Similarity();
			break;
		case "2": 
			sm = new BooleanSimilarity();
			break;
		case "6": 
			sm = new ClassicSimilarity();
			break;
		default:
			sm = new BM25Similarity();
			break;
		}
		
		
		try {
		
			Path cranAll = Paths.get("./cran/cran.all.1400");
		
			Directory dir = FSDirectory.open(Paths.get("./index"));
			
			Analyzer analyzer = new DocAnalyzer();
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			config.setSimilarity(sm);
			config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
			IndexWriter write = new IndexWriter(dir, config);
			
			IndexData id = new IndexData();
			id.indexingData(write, cranAll);
			System.out.println("Document Indexing Completedddd");
			write.close();
			
			
			Path query = Paths.get("./cran/cran.qry");
			String resultDoc = "./result/result";
			File f = new File(resultDoc);
			
			DirectoryReader dr = DirectoryReader.open(dir);
			IndexSearcher is = new IndexSearcher(dr);
			
			is.setSimilarity(sm);
			
			MultiFieldQueryParser parser = new MultiFieldQueryParser (new String[]{"Title", "Author", "Bibliography", "Words"},analyzer);
			
			Searching.executeSearch(is, parser, query, resultDoc);
			
			System.out.println("Query Search Completedddd");
			
			dr.close();
			dir.close();
			
			
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

}
