package main;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import singleton.Utility;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.igotit.R;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;

import database.DatabaseManager;
import database.LocalFileManager;
import database.Repository;

public class LoginFragment extends Fragment {
	private static final String TAG = "LoginFragment";
	private UiLifecycleHelper uiHelper;
	private ProfilePictureView profilePictureView;
	private LoginButton authButton;
	private Context context;
	private DatabaseManager dbAdapter;
	private Utility utility = Utility.getInstance();
	
	private ProgressDialog progressDialog;
	private Repository repo;
	
	// 자동 로그인을 최초 한번만 실행할 수 있게 한다. 
	private boolean initStart = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    Log.v(TAG, "onCreate");
	    
	    uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);
	    this.context = this.getActivity();
	    
	 // 처음 iGotIt.sqlite 초기화 다이얼로그
		initThread(context);

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.activity_login, container, false);
	    Log.v(TAG, "onCreateView");

	    // SQLite Adapter 
		this.dbAdapter = new DatabaseManager(context);
	    
	    authButton = (LoginButton) view.findViewById(R.id.authButton);
	    profilePictureView = (ProfilePictureView) view.findViewById(R.id.profilePicture);
	    authButton.setFragment(this);
	    
	    // set permission list, Don't foeget to add email
        authButton.setReadPermissions(Arrays.asList("basic_info", "email", "public_profile"));
	    
	    return view;
	}
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	        Log.v	(TAG, "call");
	        if (session.isOpened()) {
	        	Log.v(TAG, "session is Opened()");
	            String fbAccessToken = session.getAccessToken();
	            // make request to get facebook user info
				Request.newMeRequest(session, new Request.GraphUserCallback() {

					// callback after Graph API response with user object
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
					    	Log.v(TAG, "facebook user = " + user.toString());
							Log.v(TAG, "facebook userName = " + user.getProperty("email"));
							
							// 프로필 사진을 불러온다.
							profilePictureView.setProfileId(user.getId());
							
							// DB에 저장할 값들을 보낸다
							String[] fields = { 
									"facebook_id", 
									"facebook_name", 
									"facebook_email" };
							
							String values[] = {
									user.getId(),
									user.getName(),
									user.getProperty("email").toString()};
							
							saveDatabase("facebook", fields, values);
						}
					}
				}).executeAsync();
			}else {
				Log.v(TAG, "session is Closed()");
				initStart = true;
				
				// 프로필 사진을 default 값으로 한다.
				profilePictureView.setProfileId(null);
			}	        
	    }
	};
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	    	Log.v(TAG, "Logged in...");
	    	
	    	if(initStart){
	    		initStart = false;
	    		// utility에 기본적인 값들을 설정한다.
	    		loadDatabase();	   
	    		
	    		Intent intent = new Intent(context, MainActivity.class);
				startActivity(intent);
	    	}
	    } else if (state.isClosed()) {
	        Log.v(TAG, "Logged out...");
	    }
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    Log.v(TAG, "onResume()");
	    
	    // For scenarios where the main activity is launched and user
	    // session is not null, the session state change notification
	    // may not be triggered. Trigger it if it's open/closed.
	    Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }

	    uiHelper.onResume();
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	    Log.v(TAG, "onPause()");
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	    Log.v(TAG, "onDestroy()");
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	    Log.v(TAG, "onSaveInstanceState()");
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	    Log.v(TAG, "onActivityResult() code = " + resultCode);
	}
	
	// DB정보를 불러온다.
	private void loadDatabase(){
		Log.v(TAG, "loadDatabase()");
		dbAdapter.open();
		
		Cursor result = dbAdapter.fetchAll("facebook");
		result.moveToFirst();
		
		utility.setFacebook_id(result.getString(1));
		utility.setFacebook_name(result.getString(2));
		utility.setFacebook_email(result.getString(3));

		result = dbAdapter.fetchAll("preference");
		result.moveToFirst();
		
		utility.setPreference_ttsPitch(result.getString(1));
		utility.setPreference_ttsRate(result.getString(2));
		utility.setPreference_timeLimit(result.getString(3));
		
		result.close();
		dbAdapter.close();
	}
	
	// DB에 정보를 저장한다.
	private void saveDatabase(String tableName, String fields[], String values[]){
		Log.v(TAG, "saveDatabase()");
		dbAdapter.open();
		dbAdapter.update(tableName, fields, values);
		dbAdapter.close();
	}
	
	
	// 처음 iGotIt.sqlite 초기화 다이얼로그
	public void initThread(final Context context){
		progressDialog = new ProgressDialog(context);
		progressDialog.setTitle("Wait...");
		progressDialog.setMessage("Please wait while loading...");
		progressDialog.setIndeterminate(true);
		progressDialog.setProgressStyle(R.style.NewDialog);
		progressDialog.setCancelable(true);
		progressDialog.show();
	
		new Thread(new Runnable() {
			public void run() {
				try {
					Log.v(TAG, "IntroActivity repo = Repository.getInstance(context)");
					/* Open DataBase from assets (외부 sqlite를 내부로 복사한다) */
					repo = Repository.getInstance(context);
					
				} catch (Throwable ex) {
					ex.printStackTrace();
				}
				progressDialog.dismiss();
			}
		}).start();
	}
	
	private void getProfilePicture(final String facebookId) {
		new Thread(new Runnable() {
			public void run() {
				Log.v(TAG, "getProfilePicture");
				Bitmap profile = null;
//				HttpURLConnection connection = null;
				
				
				try {
					URL aURL = new URL("http://graph.facebook.com/609528485834580/picture?type=large");
                    URLConnection conn = aURL.openConnection();
                    conn.setUseCaches(true);
                    conn.connect(); 
                    InputStream is = conn.getInputStream(); 
                    BufferedInputStream bis = new BufferedInputStream(is); 
                    profile = BitmapFactory.decodeStream(bis);

                    bis.close(); 
                    is.close();
					
					
					//URL profileUrl = new URL("http://graph.facebook.com/" + facebookId.toString() + "/picture?type=large");
//					URL profileUrl = new URL("http://graph.facebook.com/609528485834580/picture?type=large");
//					connection = (HttpURLConnection) profileUrl.openConnection();
//					connection.connect();
//					
//					int size = connection.getContentLength();
//					bis = new BufferedInputStream(connection.getInputStream(), size);
//					profile = BitmapFactory.decodeStream(bis);
					
					//profile = BitmapFactory.decodeStream(profileUrl.openConnection().getInputStream());
					
					
					LocalFileManager fileManager = new LocalFileManager(getActivity());
					fileManager.SaveImageFile(profile, new String("profile"));
				} catch (Throwable ex) {
					Log.v(TAG, "error message = " + ex.getMessage());
					ex.printStackTrace();
				}
			}
		}).start();
	}
}

//try {
////URL image_value = new URL("http://graph.facebook.com/609528485834580/picture?type=large");
//profileImage = BitmapFactory.decodeStream(image_value.openConnection().getInputStream());
//} catch (MalformedURLException e) {
//// TODO Auto-generated catch block
//e.printStackTrace();
//} catch (IOException e) {
//// TODO Auto-generated catch block
//e.printStackTrace();
//}
