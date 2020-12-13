package ie.tcd.cs7is3.assignment.two.parser.fbis;

public class ForeignBroadcastDocument {
	private String docID;
    private String title;
    private String date;
    private String headerInfo;
    private StringBuilder text;

    public ForeignBroadcastDocument() {
    	text = new StringBuilder();
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeaderInfo() {
        return headerInfo;
    }

	public void setHeaderInfo(String headerInfo) {
		this.headerInfo = headerInfo;
	}

    public String getText() {
        return text.toString();
    }

    public void addText(String text) {
        this.text.append(text + " ");
    }

    @Override
	public String toString() {
		return "ForeignBroadcastDocument\nDoc number: " + getDocID() + "\nTitle: " + getTitle() + "\nDate: " + getDate() + "\nHeader: " + getHeaderInfo()
		+ "\nText: " + getText();
	}
}
