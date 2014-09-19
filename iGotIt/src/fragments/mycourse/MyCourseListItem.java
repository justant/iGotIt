package fragments.mycourse;

public class MyCourseListItem {
	private String vocabularyId;
	private String name;
	private String editor;
	private int wordSize;
	private int learn;
	private String type;
	private String state;
	
	MyCourseListItem(String vocabularyId, String name, String editor, String wordSize,
			String learn, String type, String state){
		
		this.vocabularyId = vocabularyId;
		this.name = name;
		this.editor = editor;
		this.wordSize = Integer.parseInt(wordSize);
		this.learn = Integer.parseInt(learn);
		this.type = type;
		this.state = state;
	}

	public String getVocabularyId() { return vocabularyId; }
	public void setVocabularyId(String vocabularyId) { this.vocabularyId = vocabularyId; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getEditor() { return editor; }
	public void setEditor(String editor) { this.editor = editor; }
	public int getWordSize() { return wordSize; }
	public void setWordSize(String wordSize) { this.wordSize = Integer.parseInt(wordSize); }
	public int getLearn() { return learn; }
	public void setLearn(String learn) { this.learn = Integer.parseInt(learn); }
	public String getType() { return type; }
	public void setType(String type) { this.type = type; }
	public String getState() { return state; }
	public void setState(String state) { this.state = state; }
	
}
