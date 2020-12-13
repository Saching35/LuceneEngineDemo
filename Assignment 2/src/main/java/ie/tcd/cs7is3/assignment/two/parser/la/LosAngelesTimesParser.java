package ie.tcd.cs7is3.assignment.two.parser.la;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LosAngelesTimesParser {

	List<Document> laDocList= new ArrayList<>();

    public List<LosAngelesTimesDocument> parseFile(String documentpath) throws IOException, ParserConfigurationException, SAXException {
        List<LosAngelesTimesDocument> laDocs = new ArrayList<>();
        List<InputStream> streams = Arrays.asList(
                new ByteArrayInputStream("<root>".getBytes()),
                new FileInputStream(documentpath),
                new ByteArrayInputStream("</root>".getBytes())
        );

        try(InputStream is = new SequenceInputStream(Collections.enumeration(streams))) {
	        org.w3c.dom.Document fileDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
	        
	        NodeList children = fileDoc.getDocumentElement().getChildNodes();
	        for(int i = 0; i < children.getLength(); i++) {
	            Node child = children.item(i);
	            if(child.getNodeType() == Node.ELEMENT_NODE) {
	            	LosAngelesTimesDocument doc = parseDoc(child);
	            	laDocs.add(doc);
	            }
	        }
        }
        return laDocs;
    }

	private LosAngelesTimesDocument parseDoc(Node child) {
		LosAngelesTimesDocument doc = new LosAngelesTimesDocument();
		NodeList children = child.getChildNodes();
		for(int j = 0; j < children.getLength(); j++) {
		    Node data = children.item(j);
		    switch (data.getNodeName()){
		        case "DOCNO":
		        	doc.setDocID(data.getTextContent());
		            break;
		        case "DATE":
		        	doc.setDate(data.getTextContent());
		            break;
		        case "SECTION":
		        	doc.setSection(data.getTextContent());
		            break;
		        case "TEXT":
		        	doc.setText(data.getTextContent());
		            break;
		        case "GRAPHIC":
		            if(!doc.getText().equals("")){
		                break;
		            }
		            doc.setText(data.getTextContent());
		            if(doc.getTitle().equals("")){
		            	 doc.setTitle(data.getTextContent());
		            }
		            break;
		        case "HEADLINE":
		        	doc.setTitle(data.getTextContent());
		            break;
		    }
		}
		return doc;
	}

    public List<Document> parseLaTimesDocs() {
        File directory = new File("src/main/resources/latimes");
        if(directory.isDirectory()) {
            File[] files = directory.listFiles();
            for (File file : files) {
                if(file.getName().startsWith("la")) {
                    try {
                        List<LosAngelesTimesDocument> docsInFile = parseFile(file.getPath());
                        for (LosAngelesTimesDocument laDoc : docsInFile) {
                        	Document doc = new Document();
                            doc.add(new TextField("documentNo", laDoc.getDocID(),Field.Store.YES));
                            doc.add(new TextField("text", laDoc.getText(), Field.Store.YES));
                            doc.add(new TextField("headline", laDoc.getTitle(), Field.Store.YES));
                            laDocList.add(doc);
                        }
                    } catch (IOException | ParserConfigurationException | SAXException e) {
                        System.err.println("Error reading in the LA Times documents");
                        e.printStackTrace();
                    }
                }
            }
        }
        return laDocList;
    }
}
