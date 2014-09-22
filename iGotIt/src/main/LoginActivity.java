package main;

import java.security.MessageDigest;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;

import com.facebook.Session;

public class LoginActivity extends FragmentActivity {
	private static final String TAG = "LoginActivity";
	
	private LoginFragment loginFragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    if (savedInstanceState == null) {
	        // Add the fragment on initial activity setup
	    	loginFragment = new LoginFragment();
	        getSupportFragmentManager().beginTransaction().add(android.R.id.content, loginFragment).commit();
	        Log.v(TAG, "savedInstanceState == null");
	        
	    } else {
	        // Or set the fragment from restored state info
	    	loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(android.R.id.content);
	    	Log.v(TAG, "savedInstanceState != null");
	    }
	    
	    try {
	        PackageInfo info = getPackageManager().getPackageInfo(
	                "com.example.igotit", 
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
	            }
	    } catch (Exception e) {
	    	Log.v(TAG, "Exception e = " + e);
	    }
	    
	    
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		Log.v(TAG, "onActivityResult()");
	}
}
