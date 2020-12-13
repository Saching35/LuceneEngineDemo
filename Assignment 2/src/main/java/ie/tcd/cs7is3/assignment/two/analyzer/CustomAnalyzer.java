package ie.tcd.cs7is3.assignment.two.analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.synonym.SynonymGraphFilter;

import ie.tcd.cs7is3.assignment.two.CustomSynonymMap.CustomSynonymMapBuilder;

public class CustomAnalyzer extends Analyzer{
	
	
	
	protected TokenStreamComponents createComponents(String fieldName) {
        StandardTokenizer src = new StandardTokenizer();
        TokenFilter result = new LowerCaseFilter(src);
        
        List<String> stopWords = null;
        String line;
        try {
			BufferedReader br = new BufferedReader(new FileReader(new File("src/main/resources/stopWordsList.txt")));
			while ((line = br.readLine()) != null) {
				stopWords.add(line);
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		CharArraySet stopWordSet = new CharArraySet(stopWords, true);
        
        
        result = new StopFilter(result, stopWordSet);
        try {
			result = new SynonymGraphFilter(result, CustomSynonymMapBuilder.create(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to build SynonymMap");
			e.printStackTrace();
		}
        result = new PorterStemFilter(result);
        return new TokenStreamComponents(src, result);
    }

}
