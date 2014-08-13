package fragments.mycourse;

import com.actionbarsherlock.app.SherlockFragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyCourseViewPagerAdapter extends FragmentPagerAdapter {
	public MyCourseViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	private final String[] tap_names = { "Studying", "Saved", "Completed"};

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
			return new FragmentMyCourseStudying();
			
		case 1:
			return new FragmentMyCourseSaved();
			
		case 2:
			return new FragmentMyCourseCompleted();

		default:
			throw new IllegalArgumentException("The item position should be less or equal to:" + 3);
		
		}
	}
}
