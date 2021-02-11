package classes;

public class URL {
	int id;
	String url;
	String contentText;
	public URL(int id, String url, String contentText) {
		super();
		this.id = id;
		this.url = url;
		this.contentText = contentText;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getContentText() {
		return contentText;
	}
	public void setContentText(String contentText) {
		this.contentText = contentText;
	}
}
