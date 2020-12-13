package ie.tcd.cs7is3.assignment.two.query;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TopicQueryParser {
	private static final String TAG_LOOKAHEAD_REGEX = "(?=<.*>)";
	private static final String NOT_A_NUMBER_REGEX = "[^0-9]";
	private static final String TAG_REGEX = "<.*>";
	private static final String TAG_INCLUDING_HEADER_REGEX = "<.*:";

	public TopicQueryParser() {
		// Nothing to initialize
	}

	public List<TopicQuery> parseTopicQueries(String fileName) throws IOException {
		String content = new String(Files.readAllBytes(Paths.get(fileName)));
		String[] topics = content.split(TopicQuery.TOPIC_TAG);
		ArrayList<TopicQuery> topicQueries = new ArrayList<>();

		for(String topicString : topics) {
			if(!topicString.isEmpty()) {
				TopicQuery topicQuery = parseTopicQuery(topicString);
				topicQueries.add(topicQuery);
			}
		}
		return topicQueries;
	}

	private TopicQuery parseTopicQuery(String topicString) {
		TopicQuery topicQuery = new TopicQuery();
		String[] sections = topicString.split(TAG_LOOKAHEAD_REGEX);

		for(String section : sections) {
			if(section.contains(TopicQuery.NUMBER_TAG)) {
				topicQuery.setNumber(extractQueryNumber(section));
			}
			else if(section.contains(TopicQuery.TITLE_TAG)) {
				topicQuery.setTitle(extractTitle(section));
			}
			else if(section.contains(TopicQuery.DESCRIPTION_TAG)) {
				topicQuery.setDescription(extractDescription(section));
			}
			else if(section.contains(TopicQuery.NARRATIVE_TAG)) {
				topicQuery.setNarrative(extractNarrative(section));
			}
			// else just ignore since it is not a valid category
		}
		return topicQuery;
	}

	private int extractQueryNumber(String line) {
		String numberString = line.replaceAll(NOT_A_NUMBER_REGEX, "");
		return Integer.parseInt(numberString);
	}

	private String extractTitle(String line) {
		return line.replaceAll(TAG_REGEX, "").trim();
	}

	private String extractDescription(String line) {
		return line.replaceAll(TAG_INCLUDING_HEADER_REGEX, "").trim();
	}

	private String extractNarrative(String line) {
		return line.replaceAll(TAG_INCLUDING_HEADER_REGEX, "").trim();
	}

	public static void main(String[] args) {	// For testing
		TopicQueryParser topicQueryParser = new TopicQueryParser();
		try {
			List<TopicQuery> topicQueries = topicQueryParser.parseTopicQueries("src/main/resources/queries/topics");
			for(TopicQuery topicQuery : topicQueries) {
				System.out.println(topicQuery);
			}
		} catch (IOException e) {
			System.err.println("Failed to read in the queries");
			e.printStackTrace();
		}
	}
}
