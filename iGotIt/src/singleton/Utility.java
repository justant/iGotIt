package singleton;

import android.util.Log;
import com.facebook.widget.ProfilePictureView;

public class Utility {
	private static final String TAG = "Singleton Utility";
	private static Utility utility = new Utility();
	
	private Utility(){
	
	}
	
	public static Utility getInstance(){
		Log.v(TAG, "Singleton Utility getInstance()");
		return utility;
	}
	
	private String facebookId = null;
	private String facebookName = null;
	private ProfilePictureView profilePictureView;
	public String getFacebookId() {
		Log.v(TAG, "getFacebookId() = " + facebookId);
		return facebookId;
	}
	public void setFacebookId(String facebookId) {
		Log.v(TAG, "setFacebookId(String facebookId) = " + facebookId);
		this.facebookId = facebookId;
	}
	public String getFacebookName() {
		Log.v(TAG, "getFacebookName() = " + facebookName);
		return facebookName;
	}
	public void setFacebookName(String facebookName) {
		Log.v(TAG, "setFacebookName(String facebookName) = " + facebookName);
		this.facebookName = facebookName;
	}
}
