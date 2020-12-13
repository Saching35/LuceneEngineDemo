package ie.tcd.cs7is3.assignment.two.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;

import ie.tcd.cs7is3.assignment.two.parser.fbis.ForeignBroadcastParser;
import ie.tcd.cs7is3.assignment.two.parser.fr94.FederalRegister1994Parser;
import ie.tcd.cs7is3.assignment.two.parser.ft.FinancialTimesParser;
import ie.tcd.cs7is3.assignment.two.parser.la.LosAngelesTimesParser;

public class AllDocParser {
	private List<Document> finalDocList;

	public AllDocParser() {
		finalDocList = new ArrayList<>();
	}

	public List<Document> readAllCollections() {
		readFinancialTimesCollection();
		readFederalRegisterCollection();
		readForeignBroadcastCollection();
		readLATimesCollection();
		return finalDocList;
	}

	private void readFinancialTimesCollection() {
		System.out.println("Reading Financial Times Limited Documents");
		FinancialTimesParser ftParser = new FinancialTimesParser();
		List<Document> ftDocs = ftParser.parseFinancialTimesDocs();
		finalDocList.addAll(ftDocs);
	}

	private void readFederalRegisterCollection() {
		System.out.println("Reading Federal Register 1994 Documents");
		FederalRegister1994Parser fr94Parse = new FederalRegister1994Parser();
		List<Document> fr94Docs = fr94Parse.parseFederalRegisterDocs(new File("src/main/resources/fr94"));
		finalDocList.addAll(fr94Docs);
	}

	private void readForeignBroadcastCollection() {
		System.out.println("Reading Foreign Broadcast Information Service Documents");
		ForeignBroadcastParser fbisParser = new ForeignBroadcastParser();
		List<Document> fbisDoc = fbisParser.parseForeignBroadcastDocs();
		finalDocList.addAll(fbisDoc);
	}

	private void readLATimesCollection() {
		System.out.println("Reading Los Angeles Times Documents");
		LosAngelesTimesParser laParser = new LosAngelesTimesParser();
		List<Document> laDocs = laParser.parseLaTimesDocs();
		finalDocList.addAll(laDocs);
	}
}
