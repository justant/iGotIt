package fragments.mycourse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.igotit.R;

public class FragmentMyCourseSaved extends SherlockFragment {
	 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

          View view=inflater.inflate(R.layout.fragment_mycourse,container, false);

          return view;
    }

}
