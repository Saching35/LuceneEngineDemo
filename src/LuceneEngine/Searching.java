package LuceneEngine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;

public class Searching {
	
	
	public static void executeSearch(IndexSearcher search, QueryParser parse, Path queries, String output)
	{
		
		try {
			FileReader fr = new FileReader(queries.toFile());
			BufferedReader br = new BufferedReader(fr);
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(output));
			
			String line;
			String query = "";
			line = br.readLine();
			int qryNum = 1;
			while(line != null)
			{
				if(line.startsWith(".I"))
				{
//					String qryNum= line.replaceAll("[^0-9]", "");
					
					line = br.readLine();
					if(line == null)
						break;
					line = br.readLine();
					while(line != null && !line.startsWith(".I"))
					{
						query += " "+line;
						line = br.readLine();
					}
					
					Query qry = parse.parse(QueryParser.escape(query.trim()));
					query = "";
					ScoreDoc[] result = search.search(qry, 1400).scoreDocs;
					
					for(int i = 0; i<result.length; i++)
					{
						Document doc = search.doc(result[i].doc);
						String resultLine = qryNum + " 0 " + doc.get("item").substring(3) + " 0 " + result[i].score + " EXP\n";
						bw.write(resultLine);
					}
					qryNum++;
				}
			}
			
			br.close();
			bw.close();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}

}
