package fragments.study;

import android.os.Parcel;
import android.os.Parcelable;

public class Vocabulary implements Parcelable {
	// FragmentMyCourse에서 필요한 기본정보 (sqlite vocabularys 테이블에서 가져온다.)
	private String vocabularyId;
	private String name;
	private String editor;
	private String wordSize;
	private String learn;
	private String state;
	
	public static final Parcelable.Creator<Vocabulary> CREATOR = new Parcelable.Creator<Vocabulary>() {
		public Vocabulary createFromParcel(Parcel in) {
			return new Vocabulary(in);
		}

		public Vocabulary[] newArray(int size) {
			return new Vocabulary[size];
		}
	};
	
	public Vocabulary(String vocabularyId, String name, String editor, String wordSize,
			String learn, String state){
		this.vocabularyId = vocabularyId;
		this.name = name;
		this.editor = editor;
		this.wordSize = wordSize;
		this.learn = learn;
		this.state = state;	
	}
	
	// getter & setter
	public String getVocabularyId() { return vocabularyId; }
	public void setVocabularyId(String vocabularyId) { this.vocabularyId = vocabularyId; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getEditor() { return editor; }
	public void setEditor(String editor) { this.editor = editor; }
	public String getWordSize() { return wordSize; }
	public void setWordSize(String wordSize) { this.wordSize = wordSize; }
	public String getLearn() { return learn; }
	public void setLearn(String learn) { this.learn = learn; }
	public String getState() { return state; }
	public void setState(String state) { this.state = state; }
	
	public Vocabulary(Parcel in) { 
		vocabularyId = in.readString();
		name = in.readString();
		editor = in.readString();
		wordSize = in.readString();
		learn = in.readString();
		state = in.readString();
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(vocabularyId);
		dest.writeString(name);
		dest.writeString(editor);
		dest.writeString(wordSize);
		dest.writeString(learn);
		dest.writeString(state);
	}
}
