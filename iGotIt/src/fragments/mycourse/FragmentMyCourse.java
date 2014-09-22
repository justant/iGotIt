package fragments.mycourse;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.example.igotit.R;

// view pager notifyDataSetChanged�� �ؾ��Ѵ�.(���� ������ ���ؼ� ������ ������ �ȵ�)
public class FragmentMyCourse extends Fragment {
	private static final String TAG = "FragmentMyCourse";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.v(TAG, "onCreateView");
		return inflater.inflate(R.layout.fragment_course_pager, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Log.v(TAG, "onViewCreated");
		super.onViewCreated(view, savedInstanceState);

		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view.findViewById(R.id.course_tabs);
		ViewPager pager = (ViewPager) view.findViewById(R.id.course_pager);
		MyCourseViewPagerAdapter adapter = new MyCourseViewPagerAdapter(getChildFragmentManager());
		pager.setAdapter(adapter);
		tabs.setViewPager(pager);
		
		tabs.setIndicatorColorResource(R.color.iGotIt_Orange_light);
		tabs.setUnderlineColorResource(R.color.iGotIt_Orange_light);
		tabs.setIndicatorHeight(20);
		tabs.setTextColorResource(R.color.iGotIt_Gray_white);
		
	    //�ﺣƼī ��Ʈ�� ����
	 	Typeface fontType = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Helvetica.ttf"); 
	    tabs.setTypeface(fontType, Typeface.BOLD);
	    tabs.setTextSize(40);
	}
}