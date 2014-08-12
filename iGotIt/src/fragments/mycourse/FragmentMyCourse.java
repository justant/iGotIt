package fragments.mycourse;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.example.igotit.R;

public class FragmentMyCourse extends Fragment {
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_mycourse_pager, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view.findViewById(R.id.mycourse_tabs);
		ViewPager pager = (ViewPager) view.findViewById(R.id.mycourse_pager);
		ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
		pager.setAdapter(adapter);
		tabs.setViewPager(pager);
		
		tabs.setIndicatorColorResource(R.color.iGotIt_Orange_light);
		tabs.setUnderlineColorResource(R.color.iGotIt_Orange_light);
		tabs.setIndicatorHeight(20);
		tabs.setTextColorResource(R.color.iGotIt_Gray_white);
		
	    //헬베티카 폰트로 변경
	 	Typeface fontType = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Helvetica.ttf"); 
	    tabs.setTypeface(fontType, Typeface.BOLD);
	    tabs.setTextSize(40);
	}
}