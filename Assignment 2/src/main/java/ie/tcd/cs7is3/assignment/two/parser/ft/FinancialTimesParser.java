package ie.tcd.cs7is3.assignment.two.parser.ft;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class FinancialTimesParser {

	private List<Document> ftDocList;

	public FinancialTimesParser() {
		ftDocList = new ArrayList<>();
	}

	public List<Document> parseFinancialTimesDocs()
	{
		try {
			List<FinancialTimesDocument> documents = readInCollection();
			for (FinancialTimesDocument ftdoc: documents) {
				Document doc = new Document();
				doc.add(new TextField("documentNo", ftdoc.getDocNumber(), Field.Store.YES));
				doc.add(new TextField("text", ftdoc.getText(), Field.Store.YES));
				doc.add(new TextField("headline", ftdoc.getHeadline(), Field.Store.YES));
				ftDocList.add(doc);
			}
		} catch (Exception e) {
			System.err.println("Error reading in the financial times documents");
			e.printStackTrace();
		}
		return ftDocList;
	}

	private void getFilesInCollection(String directoryName, List<File> files) {
		File directory = new File(directoryName);

		// Get all files from directory
		File[] fileList = directory.listFiles();
		if (fileList != null) {
			for (File file : fileList) {
				if (file.isFile() && file.getName().startsWith("ft")) {
					files.add(file);
				} else if (file.isDirectory()) {
					getFilesInCollection(file.getPath(), files);
				}
			}
		}
	}

	private List<FinancialTimesDocument> readInCollection() throws ParserConfigurationException, SAXException, IOException {
		List<File> files = new ArrayList<>();
		getFilesInCollection("src/main/resources/ft", files);

		List<FinancialTimesDocument> documents = new ArrayList<>();

		for(File file : files) {
			List<InputStream> streams = Arrays.asList(
					new ByteArrayInputStream("<root>".getBytes()),
					new FileInputStream(file),
					new ByteArrayInputStream("</root>".getBytes())
					);

			try(InputStream is = new SequenceInputStream(Collections.enumeration(streams))) {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				org.w3c.dom.Document document = builder.parse(is);
				document.getDocumentElement().normalize();

				NodeList nodeList = document.getElementsByTagName("DOC");
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						FinancialTimesDocument doc = parseDoc(file.getName(), (Element) node);
						documents.add(doc);
					}
				}
			}
		}
		return documents;
	}

	private FinancialTimesDocument parseDoc(String fileName, Element element) {
		FinancialTimesDocument doc = new FinancialTimesDocument();
		String tagValue;

		try {
			tagValue = element.getElementsByTagName("DOCNO").item(0).getTextContent();
			doc.setDocNumber(tagValue);
		} catch (NullPointerException nlp) {
			doc.setDocNumber("-");
			System.err.println("DOCNO tag is missing in file " + fileName);
		}

		try {
			tagValue = element.getElementsByTagName("PROFILE").item(0).getTextContent();
			doc.setProfile(tagValue);
		} catch (NullPointerException nlp) {
			doc.setProfile("-");
			System.err.println("PROFILE tag is missing in docno " + doc.getDocNumber() + " in file " + fileName);
		}

		try {
			tagValue = element.getElementsByTagName("DATE").item(0).getTextContent();
			doc.setDate(tagValue);
		} catch (NullPointerException nlp) {
			doc.setDate("-");
			System.err.println("DATE tag is missing in docno " + doc.getDocNumber() + " in file " + fileName);
		}

		try {
			tagValue = element.getElementsByTagName("HEADLINE").item(0).getTextContent();
			doc.setHeadline(tagValue);
		} catch (NullPointerException nlp) {
			doc.setHeadline("-");
			System.err.println("HEADLINE tag is missing in docno " + doc.getDocNumber() + " in file " + fileName);
		}

		try {
			tagValue = element.getElementsByTagName("TEXT").item(0).getTextContent();
			doc.setText(tagValue);
		} catch (NullPointerException nlp) {
			doc.setText("-");
			System.err.println("TEXT tag is missing in docno " + doc.getDocNumber() + " in file " + fileName);
			if (doc.getDocNumber().equalsIgnoreCase("FT924-11838")) {
				System.out.println("Setting dateline tagvalue for text of doc " + doc.getDocNumber());
				tagValue = element.getElementsByTagName("DATELINE").item(0).getTextContent();
				doc.setText(tagValue);
			}
		}

		try {
			tagValue = element.getElementsByTagName("PUB").item(0).getTextContent();
			doc.setPublication(tagValue);
		} catch (NullPointerException nlp) {
			doc.setPublication("-");
			System.out.println("PUB tag is missing in docno " + doc.getDocNumber() + " in file " + fileName);
		}

		try {
			tagValue = element.getElementsByTagName("PAGE").item(0).getTextContent();
			doc.setPage(tagValue);
		} catch (NullPointerException nlp) {
			doc.setPage("-");
			System.err.println("PAGE tag is missing in docno " + doc.getDocNumber() + " in file " + fileName);
		}
		return doc;
	}
}
