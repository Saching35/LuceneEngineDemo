package LuceneEngine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;

public class IndexData {

	final String index= "\\.I \\d";
	final String title= "\\.T";
	final String author= "\\.A";
	final String text= "\\.W";
	final String biblo= "\\.B";
	Document doc = null;
	String fieldType;
	String value;
	
	public void indexingData(IndexWriter writer, Path path)
	{
		try 
		{
			FileReader fr = new FileReader(path.toFile());
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			while(line != null)
			{
				if(line.startsWith(".I"))
				{
					doc = new Document();
					Field item = new StringField("item", line, Field.Store.YES);
					doc.add(item);
					line = br.readLine();
					while(!line.startsWith(".I"))
					{
						if(line.startsWith(".T"))
						{
							fieldType = "Title";
							while(!line.startsWith(".A"))
							{
							line = br.readLine();
							if(!line.startsWith(".A"))
								{
									value+=" "+line;
								}
							}
						}
						else if(line.startsWith(".A"))
						{
							fieldType = "Author";
							while(!line.startsWith(".B"))
							{
							line = br.readLine();
							if(!line.startsWith(".B"))
								{
									value+=" "+line;
								}
							}
						}
						else if(line.startsWith(".B"))
						{
							fieldType = "Bibliography";
							while(!line.startsWith(".W"))
							{
							line = br.readLine();
							if(!line.startsWith(".W"))
								{
									value+=" "+line;
								}
							}
						}
						else if(line.startsWith(".W"))
						{
							fieldType = "Words";
							while(line != null && !line.startsWith(".I"))
							{
							line = br.readLine();
							if(line != null && !line.startsWith(".I"))
								{
									value+=" "+line;
								}
							}
						}
						else {
							System.out.println("Unknown Character");
							throw new Exception("Unknown Character");
						}
						doc.add(new TextField(fieldType, value, Field.Store.YES));
						value = "";
						if(line == null)
							break;
					}
					writer.addDocument(doc);
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
}
