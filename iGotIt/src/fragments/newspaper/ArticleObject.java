package fragments.newspaper;

public class ArticleObject {
	private String title;
	private String newsdate;
	private String content;
	
	public ArticleObject(String title, String newsdate, String content) {
		setTitle(title);
		setNewsdate(newsdate);
		setContent(content);
	}
	public ArticleObject(){
		setTitle(null);
		setNewsdate(null);
		setContent(null);
	}
	
	public String toString(){
		return "<"+getTitle()+">"+"\n\n"+getContent();
	}
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getNewsdate() {
		return newsdate;
	}
	public void setNewsdate(String newsdate) {
		this.newsdate = newsdate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
