package fragments.preference;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.igotit.R;

public class FragmentPreference extends SherlockFragment {
	 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

          View view = inflater.inflate(R.layout.fragment_coursedirectory, container, false);

          return view;
    }

}