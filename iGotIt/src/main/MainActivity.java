package main;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.example.igotit.R;

import fragments.coursedirectory.FragmentCourseDirectory;
import fragments.home.FragmentHome;
import fragments.mycourse.FragmentMyCourse;
import fragments.preference.FragmentPreference;

@SuppressLint("ResourceAsColor")
public class MainActivity extends SherlockFragmentActivity{
	public static String TAG = "MainActivity";	
	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	
	CustomDrawerAdapter adapter;
	List<DrawerItem> dataList;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from activity_main.xml
		setContentView(R.layout.activity_main);
		
		// Get the Title
		mTitle = mDrawerTitle = getTitle();
		
		// Locate DrawerLayout in activity_main.xml
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		// Locate ListView in activity_main.xml
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
	
		// Set a custom shadow that overlays the main content when the drawer opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		mDrawerLayout.setScrimColor(Color.TRANSPARENT);

		// mrawerList에 들어 갈 데이터들
		dataList = new ArrayList<DrawerItem>();
		
		// Drawer User
		dataList.add(new DrawerItem());
		
		// Drawer Item
		dataList.add(new DrawerItem("Home", R.drawable.ic_drawer_home));
		dataList.add(new DrawerItem("My Courses", R.drawable.ic_drawer_course));
		dataList.add(new DrawerItem("Course Directory", R.drawable.ic_drawer_preferences));
		dataList.add(new DrawerItem("Preferences", R.drawable.ic_drawer_preferences));
		dataList.add(new DrawerItem("Support", R.drawable.ic_drawer_preferences));
		dataList.add(new DrawerItem("Logout", R.drawable.ic_drawer_preferences));

		adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item, dataList);
		
		// Set the MenuListAdapter to the ListView
		mDrawerList.setAdapter(adapter);
		
		// Capture listview menu item click
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// Enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);

				// Set the title on the action when drawer close
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); 
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				
				// Set the title on the action when drawer open
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); 
			}
		};
		
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		SelectItem(1);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                mDrawerLayout.openDrawer(mDrawerList);
            }
        }
        return super.onOptionsItemSelected(item);
	}
	
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
    
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	
	@Override
	public void setTitle(CharSequence title) {
		Log.v(TAG, "setTitle = " + title);
		
		int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
	    
		TextView tv = (TextView) findViewById(titleId);
		mTitle = title;
		tv.setText(mTitle);
	    
	    //헬베티카 폰트로 변경
	 	Typeface fontType = Typeface.createFromAsset(getAssets(),"fonts/Helvetica.ttf"); 
	    tv.setTypeface(fontType, Typeface.BOLD);
	}
	
	// ListView click listener in the navigation drawer
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// user 이름이 null 이라는것은 item 이라는 것
			// 반대로, item 이름이 null 이라는 것은 user 라는것
			if (dataList.get(position).getUserName() == null) {
				Log.v(TAG, "click list position = " + position);
				SelectItem(position);
			}
		}
	}
	
	public void SelectItem(int possition) {
		Fragment fragment = null;

		// Locate Position
		switch (possition) {
		case 1:
			fragment = new FragmentHome();
			break;
			
		case 2:
			fragment = new FragmentMyCourse();
			break;
		
		case 3:
			fragment = new FragmentCourseDirectory();
			break;
			
		case 4:
			fragment = new FragmentPreference();
			break;
			
		case 5:
			// Support
			Log.v(TAG, "MainActivity Support");
			return;
			
		case 6:
			// Logout
			Log.v(TAG, "MainActivity Logout");
			return;
		
		default:
			return;
		}
		
		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction().replace(R.id.container, fragment).commit();

		
		mDrawerList.setItemChecked(possition, true);
		// Get the title followed by the position
		setTitle(dataList.get(possition).getItemName());
		// Close drawer
		mDrawerLayout.closeDrawer(mDrawerList);
	}
}
