package singleton;

import android.util.Log;
import com.facebook.widget.ProfilePictureView;

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
	// ���̽��� ������ ����
	private ProfilePictureView facebook_profilePicture;
	// tts ������
	private String preference_ttsPitch;
	// tts ������
	private String preference_ttsRate;
	// �̹����н� Ÿ�̸� �ð�
	private String preference_timeLimit;

	public String getFacebook_id() {
		return facebook_id;
	}
	public void setFacebook_id(String facebook_id) {
		this.facebook_id = facebook_id;
	}
	public String getFacebook_name() {
		return facebook_name;
	}
	public void setFacebook_name(String facebook_name) {
		this.facebook_name = facebook_name;
	}
	public String getFacebook_email() {
		return facebook_email;
	}
	public void setFacebook_email(String facebook_email) {
		this.facebook_email = facebook_email;
	}
	public ProfilePictureView getFacebook_profilePicture() {
		return facebook_profilePicture;
	}
	public void setFacebook_profilePicture(
			ProfilePictureView facebook_profilePicture) {
		this.facebook_profilePicture = facebook_profilePicture;
	}
	public String getPreference_ttsPitch() {
		return preference_ttsPitch;
	}
	public void setPreference_ttsPitch(String preference_ttsPitch) {
		this.preference_ttsPitch = preference_ttsPitch;
	}
	public String getPreference_ttsRate() {
		return preference_ttsRate;
	}
	public void setPreference_ttsRate(String preference_ttsRate) {
		this.preference_ttsRate = preference_ttsRate;
	}
	public String getPreference_timeLimit() {
		return preference_timeLimit;
	}
	public void setPreference_timeLimit(String preference_timeLimit) {
		this.preference_timeLimit = preference_timeLimit;
	}
	
}
