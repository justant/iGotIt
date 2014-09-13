package fragments.study;

import java.util.ArrayList;

import singleton.Utility;

import com.example.igotit.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

// 학습할 단어가 넘어오면 이 단어의 Image Url을 만들어서 ImageGridFragment/ImagePagerFragment로 넘겨준다

public class SimpleImageActivity extends FragmentActivity {
	private Utility utility = Utility.getInstance();
	private int fragmentIndex;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		fragmentIndex = intent.getIntExtra(Constants.Extra.FRAGMENT_INDEX, 0);

		Fragment fragment;
		String tag;
		int titleRes;
		
		switch (fragmentIndex) {
			default:
			
			case ImageGridFragment.INDEX:
				tag = ImageGridFragment.class.getSimpleName();
				fragment = getSupportFragmentManager().findFragmentByTag(tag);
				if (fragment == null) {
					fragment = new ImageGridFragment();
				}
				
				titleRes = R.string.ac_name_image_grid;
				break;
			
			case ImagePagerFragment.INDEX:
				tag = ImagePagerFragment.class.getSimpleName();
				fragment = getSupportFragmentManager().findFragmentByTag(tag);
				if (fragment == null) {
					fragment = new ImagePagerFragment();
					fragment.setArguments(getIntent().getExtras());
				}
				titleRes = R.string.ac_name_image_pager;
				break;
		}

		setTitle(titleRes);
		getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fragment, tag).commit();
	}
}