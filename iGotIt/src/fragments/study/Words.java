package fragments.study;

import android.os.Parcel;
import android.os.Parcelable;

public class Words implements Parcelable{
	private String id;
	private String word;
	private String pronuciation;
	private String mean;
	private String memo;
	private String learn;
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Words createFromParcel(Parcel in) {
			return new Words(in);
		}

		public Words[] newArray(int size) {
			return new Words[size];
		}
	};
	
	public Words(){
		
	}
	
	public Words(Parcel in) { 
		id = in.readString();
		word = in.readString();
		pronuciation = in.readString();
		mean = in.readString();
		memo = in.readString();
		learn = in.readString();
	}
	
	public Words(String id, String word, String pronunciation,
			String mean, String memo, String learn){
		this.id = id;
		this.word = word;
		this.pronuciation = pronunciation;
		this.mean = mean;
		this.memo = memo;
		this.learn = learn;
	}
	
	// getter & setter
	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	public String getWord() { return word; }
	public void setWord(String word) { this.word = word; }
	public String getPronuciation() { return pronuciation; }
	public void setPronuciation(String pronuciation) { this.pronuciation = pronuciation; }
	public String getMean() { return mean; }
	public void setMean(String mean) { this.mean = mean; }
	public String getMemo() { return memo; }
	public void setMemo(String memo) { this.memo = memo; }
	public String getLearn() { return learn; }
	public void setLearn(String learn) { this.learn = learn; }
	
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(word);
		dest.writeString(pronuciation);
		dest.writeString(mean);
		dest.writeString(memo);
		dest.writeString(learn);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((learn == null) ? 0 : learn.hashCode());
		result = prime * result + ((mean == null) ? 0 : mean.hashCode());
		result = prime * result + ((memo == null) ? 0 : memo.hashCode());
		result = prime * result
				+ ((pronuciation == null) ? 0 : pronuciation.hashCode());
		result = prime * result + ((word == null) ? 0 : word.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Words other = (Words) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (learn == null) {
			if (other.learn != null)
				return false;
		} else if (!learn.equals(other.learn))
			return false;
		if (mean == null) {
			if (other.mean != null)
				return false;
		} else if (!mean.equals(other.mean))
			return false;
		if (memo == null) {
			if (other.memo != null)
				return false;
		} else if (!memo.equals(other.memo))
			return false;
		if (pronuciation == null) {
			if (other.pronuciation != null)
				return false;
		} else if (!pronuciation.equals(other.pronuciation))
			return false;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		return true;
	}
	
	
}
