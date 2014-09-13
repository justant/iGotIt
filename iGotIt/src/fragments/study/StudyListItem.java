package fragments.study;

public class StudyListItem {
	private int id;
	private String title;
	private int wordSize;
	private int learn;	
	
	public StudyListItem(int id, String title, int wordSize, int learn){
		this.id = id;
		this.title = title;
		this.wordSize = wordSize;
		this.learn = learn;
	}

	// Getter & Setter
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public int getWordSize() { return wordSize; }
	public void setWordSize(int wordSize) { this.wordSize = wordSize; }
	public int getLearn() { return learn; }
	public void setLearn(int learn) { this.learn = learn; }
	
	
}
