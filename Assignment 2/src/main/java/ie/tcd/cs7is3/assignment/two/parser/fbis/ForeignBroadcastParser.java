package ie.tcd.cs7is3.assignment.two.parser.fbis;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ForeignBroadcastParser {
	private List<ForeignBroadcastDocument> documentList;

	public ForeignBroadcastParser() {
		documentList = new ArrayList<>();
	}

	public void readInCollection(String directoryPath) {
		File directory = new File(directoryPath);
		if(directory.isDirectory()) {
			File[] files = directory.listFiles();
			for (File file : files) {
				if(file.getName().startsWith("fb")) {
					try {
						parseFile(file);
					} catch (IOException e) {
						System.err.println("Failure reading in the file");
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void parseFile(File file) throws IOException {
		org.jsoup.nodes.Document document = Jsoup.parse(file, "US-ASCII");
        Elements elements = document.select("DOC");
        
        for(Element element : elements) {
        	ForeignBroadcastDocument parsedDoc = new ForeignBroadcastDocument();
        	parseDocumentElements(parsedDoc, element);
        	documentList.add(parsedDoc);
        }
	}

	private void parseDocumentElements(ForeignBroadcastDocument parsedDoc, Element element) {
        for(Element child : element.children()) {
        	String elementTag = child.tag().normalName().toUpperCase();
        	switch (elementTag) {
        		case "DATE1":
        			parsedDoc.setDate(child.ownText());
        			break;
			   	case "DOCNO":
			   		parsedDoc.setDocID(child.ownText());
			   		break;
			   	case "HEADER":
			   		parsedDoc.setHeaderInfo(child.ownText());
			   		break;
			   	case "F":
			   	case "TEXT":
			   		parsedDoc.addText(child.ownText());
			   		break;
			   	case "TI":
			   		parsedDoc.setTitle(child.ownText());
			   		break;
			   	default:
			   		if(elementTag.matches("H[0-9]?")) {
			   			parsedDoc.addText(child.ownText());
			   		}
        	}
        	if(element.childrenSize() > 0) {	// Has children elements
        		parseDocumentElements(parsedDoc, child);
        	}
        }
	}

	public List<ForeignBroadcastDocument> getDocumentList() {
		return documentList;
	}
	
	public List<Document> parseForeignBroadcastDocs() {
		readInCollection("src/main/resources/fbis");
		List<ForeignBroadcastDocument> documents = getDocumentList();
		
		List<Document> fbisDocList= new ArrayList<>();
		for (ForeignBroadcastDocument fbisDoc : documents) {
			Document doc = new Document();
            doc.add(new TextField("documentNo", fbisDoc.getDocID(), Field.Store.YES));
            doc.add(new TextField("text", fbisDoc.getText() + " " + fbisDoc.getHeaderInfo(), Field.Store.YES));
            doc.add(new TextField("headline", fbisDoc.getTitle() + " " + fbisDoc.getDate(), Field.Store.YES));
            fbisDocList.add(doc);
        }
		return fbisDocList;
	}
}
