package ie.tcd.cs7is3.assignment.two.parser.la;

public class LosAngelesTimesDocument {
	private String title;
	private String text;
	private String docID;
	private String date;
	private String section;

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTitle() {
		if(title==null){
			return "";
		}
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		if(text==null){
			return "";
		}
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDocID() {
		return docID;
	}

	public void setDocID(String docID) {
		this.docID = docID.strip();
	}
}
