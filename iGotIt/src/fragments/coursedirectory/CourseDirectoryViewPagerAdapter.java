package fragments.coursedirectory;

import com.actionbarsherlock.app.SherlockFragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CourseDirectoryViewPagerAdapter extends FragmentPagerAdapter {
	
	public CourseDirectoryViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	private final String[] tap_names = { "TOEIC", "TOEFL", "TEPS", "수능", "NEAT/NEPT", "초중고", "기타"};

	@Override
	public CharSequence getPageTitle(int position) {
		return tap_names[position];
	}

	@Override
	public int getCount() {
		return tap_names.length;
	}
	
	@Override
	public SherlockFragment getItem(int position) {
		switch (position) {
		case 0:
			return new FragmentCourseDirectoryToeic();
			
		case 1:
			return new FragmentCourseDirectoryToefl();
			
		case 2:
			return new FragmentCourseDirectorySuneung();
			
		case 3:
			return new FragmentCourseDirectoryTeps();
		
		case 4:
			return new FragmentCourseDirectoryNeat();
		
		case 5:
			return new FragmentCourseDirectorySchools();
			
		case 6:
			return new FragmentCourseDirectoryEtc();

		default:
			throw new IllegalArgumentException("The item position should be less or equal to:" + 3);
		
		}
	}
}
