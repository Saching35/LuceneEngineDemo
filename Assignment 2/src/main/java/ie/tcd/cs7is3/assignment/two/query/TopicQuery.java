package ie.tcd.cs7is3.assignment.two.query;

public class TopicQuery {
	protected static final String TOPIC_TAG = "<top>";
	protected static final String TOPIC_CLOSE_TAG = "</top>";
	protected static final String NUMBER_TAG = "<num>";
	protected static final String TITLE_TAG = "<title>";
	protected static final String DESCRIPTION_TAG = "<desc>";
	protected static final String NARRATIVE_TAG = "<narr>";

	protected static final String NUMBER_HEADER = "Number:";
	protected static final String DESCRIPTION_HEADER = "Description:";
	protected static final String NARRATIVE_HEADER = "Narrative:";

	private int number;
	private String title;
	private String description;
	private String narrative;

	public TopicQuery(int number, String title, String description, String narrative) {
		this.setNumber(number);
		this.setTitle(title);
		this.setDescription(description);
		this.setNarrative(narrative);
	}

	public TopicQuery() {}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNarrative() {
		return narrative;
	}

	public void setNarrative(String narrative) {
		this.narrative = narrative;
	}

	public String createQuery() {
		// Experiment here with how we want to generate the query. Taking the default query as a combination of title, description and narrative
		return title + " " + description + " " + narrative;
	}

    @Override
	public String toString() {
		return TOPIC_TAG + "\n\n"
				+ NUMBER_TAG + " " + NUMBER_HEADER + " " + getNumber() + "\n"
				+ TITLE_TAG + " " + getTitle() + "\n\n"
				+ DESCRIPTION_TAG + " " + DESCRIPTION_HEADER + "\n" + getDescription() + "\n\n"
				+ NARRATIVE_TAG + " " + NARRATIVE_HEADER + "\n" + getNarrative() + "\n\n"
				+ TOPIC_CLOSE_TAG + "\n";
	}
 }
