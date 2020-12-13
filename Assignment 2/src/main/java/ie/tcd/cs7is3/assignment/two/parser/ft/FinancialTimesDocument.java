package ie.tcd.cs7is3.assignment.two.parser.ft;

public class FinancialTimesDocument {
	private String docNumber;
	private String profile;
	private String date;
	private String headline;
	private String text;
	private String publication;
	private String page;

	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getPublication() {
		return publication;
	}

	public void setPublication(String publication) {
		this.publication = publication;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	@Override
	public String toString() {
		return "GenericFTDocument [doc_number=" + docNumber + ", profile=" + profile + ", date=" + date + ", headline="
				+ headline + ", text=" + text + ", publication=" + publication + ", page=" + page + "]";
	}
}
