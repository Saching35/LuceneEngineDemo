package ie.tcd.cs7is3.assignment.two.parser.fr94;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FederalRegister1994Parser {
	private static final String TAGS_REGEX = "<[^>]*>";

	private List<Document> frDocList;

	public FederalRegister1994Parser() {
		frDocList = new ArrayList<>();
	}

	public List<Document> parseFederalRegisterDocs(File dir) {
		readInCollection(dir);
		return frDocList;
	}

    private void readInCollection(File dir) {
        List<FederalRegister1994Document> frList = new ArrayList<>();
        
        for (final File file : dir.listFiles()) {
            if (file.isDirectory()) {
                readInCollection(file);
            }
            else {
            	if(file.getName().startsWith("fr")) {
                    try {
                        org.jsoup.nodes.Document document= Jsoup.parse(file,"UTF-8");
                        Elements elements = document.select("DOC");

                        for (Element e: elements) {
                            String docNo = e.getElementsByTag("DOCNO").toString().replaceAll(TAGS_REGEX, "").replace("\n", " ").trim();
                            String parentNo = e.getElementsByTag("PARENT").toString().replaceAll(TAGS_REGEX, "").replace("\n", " ").trim();
                            String text = e.getElementsByTag("TEXT").toString().replaceAll(TAGS_REGEX, "").replace("\n", " ").trim();
                            
                            FederalRegister1994Document frDoc  = new FederalRegister1994Document();
                            frDoc.setDocNo(docNo);
                            frDoc.setTitle(parentNo);
                            frDoc.setText(text);
                            frList.add(frDoc);
                        }
                    } catch (IOException e) {
                    	System.err.println("Error reading in the file " + file.getName());
                        e.printStackTrace();
                    }
                }
            }
        }
        addDocuments(frList);
    }

	private void addDocuments(List<FederalRegister1994Document> frList) {
    	for (FederalRegister1994Document frDoc : frList) {
            Document doc = new Document();
            doc.add(new TextField("documentNo",frDoc.getDocNo(), Field.Store.YES));
            doc.add(new TextField("text", frDoc.getText(), Field.Store.YES));
            doc.add(new TextField("headline",frDoc.getTitle(), Field.Store.YES));
            frDocList.add(doc);
        }
    }
}
