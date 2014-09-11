package singleton;

import image.google.GoogleImage;

import java.util.ArrayList;

import fragments.study.Words;
import android.util.Log;

public class Utility {
	private static final String TAG = "Singleton Utility";
	private static Utility utility = new Utility();
	private Utility(){}
	public static Utility getInstance(){
		Log.v(TAG, "Singleton Utility getInstance()");
		return utility;
	}
	
	// 페이스북 사용자 ID
	private String facebook_id;
	// 페이스북 사용자 이름
	private String facebook_name;
	// 페이스북 사용자 메일
	private String facebook_email;
	// tts 음높이
	private String preference_ttsPitch;
	// tts 빠르기
	private String preference_ttsRate;
	// 이미지학습 타이머 시간
	private String preference_timeLimit;
	
	// 테이블에 접근한 모든 이미지학습할 데이터
	private ArrayList<Words> chapterWordList = new ArrayList<Words>();
	
	// 이미지 연상학습에서 필요한 특정 단어가 가지고있는 origin url, thumnail url 과 같은 이미지정보
	private ArrayList<GoogleImage> googleImageList = new ArrayList<GoogleImage>();

	public String getFacebook_id() { return facebook_id; }
	public void setFacebook_id(String facebook_id) { this.facebook_id = facebook_id; }
	public String getFacebook_name() { return facebook_name; }
	public void setFacebook_name(String facebook_name) { this.facebook_name = facebook_name; }
	public String getFacebook_email() { return facebook_email; }
	public void setFacebook_email(String facebook_email) { this.facebook_email = facebook_email; }
	public String getPreference_ttsPitch() { return preference_ttsPitch; }
	public void setPreference_ttsPitch(String preference_ttsPitch) { this.preference_ttsPitch = preference_ttsPitch; }
	public String getPreference_ttsRate() { return preference_ttsRate; }
	public void setPreference_ttsRate(String preference_ttsRate) { this.preference_ttsRate = preference_ttsRate; }
	public String getPreference_timeLimit() { return preference_timeLimit; }
	public void setPreference_timeLimit(String preference_timeLimit) { this.preference_timeLimit = preference_timeLimit; }
	public ArrayList<Words> getChapterWordList() { return chapterWordList; }
	public void setChapterWordList(ArrayList<Words> chapterWordList) { this.chapterWordList = chapterWordList; }
	public ArrayList<GoogleImage> getGoogleImageList() { return googleImageList; }
	public void setGoogleImageList(ArrayList<GoogleImage> googleImageList) { this.googleImageList = googleImageList; }
	
}