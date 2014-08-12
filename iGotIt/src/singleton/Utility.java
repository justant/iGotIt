package singleton;

import android.util.Log;

public class Utility {
	private static final String TAG = "Singleton Utility";
	private static Utility utility = new Utility();
	
	private Utility(){
	
	}
	
	public static Utility getInstance(){
		Log.v(TAG, "Singleton Utility getInstance()");
		return utility;
	}
	
}
