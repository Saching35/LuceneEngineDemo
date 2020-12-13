package ie.tcd.cs7is3.assignment.two.CustomSynonymMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.util.CharsRef;

public class CustomSynonymMapBuilder {

	public static SynonymMap create() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File("src/main/resources/SynonymMap.txt")));
		String line;
		SynonymMap.Builder builder = new SynonymMap.Builder(true);
		while ((line = br.readLine()) != null) {
			String val[] = line.split("-");
            builder.add(new CharsRef(val[0].toLowerCase()), new CharsRef(val[1].toLowerCase()), true);
				}
		return builder.build();
	}
}
