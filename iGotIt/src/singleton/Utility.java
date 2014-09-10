package singleton;

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
	
	// ���̽��� ����� ID
	private String facebook_id;
	// ���̽��� ����� �̸�
	private String facebook_name;
	// ���̽��� ����� ����
	private String facebook_email;
	// tts ������
	private String preference_ttsPitch;
	// tts ������
	private String preference_ttsRate;
	// �̹����н� Ÿ�̸� �ð�
	private String preference_timeLimit;
	
	// ���̺� ������ ��� �̹����н��� ������
	private ArrayList<Words> wordList;
	// �н��� �������� ���� ����
	private int wordPosition;
	// �����������κ��� ũ��
	private int wordSize;

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
	public ArrayList<Words> getWordList() { return wordList; }
	public void setWordList(ArrayList<Words> wordList) { this.wordList = wordList; }
	public int getWordPosition() { return wordPosition; }
	public void setWordPosition(int wordPosition) { this.wordPosition = wordPosition; }
	public int getWordSize() { return wordSize; }
	public void setWordSize(int wordSize) { this.wordSize = wordSize; }
	
	
	
}
