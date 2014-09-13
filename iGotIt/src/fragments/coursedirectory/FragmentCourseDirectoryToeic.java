package fragments.coursedirectory;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.igotit.R;

public class FragmentCourseDirectoryToeic extends SherlockFragment {
	private static final String TAG = "FragmentCourseDirectoryToeic";

	private List<CourseDirectoryListItem> listItems;
	private ListView listView; 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		listItems = new ArrayList<CourseDirectoryListItem>();
		 
		for (int i = 0; i < 10; i++) {
			CourseDirectoryListItem item = new CourseDirectoryListItem();
			listItems.add(item);
		}
		
		View view = inflater.inflate(R.layout.fragment_course, container, false);
		listView = (ListView) view.findViewById(R.id.studyListView);
		listView.setAdapter(new CourseAdapter(getActivity(), listItems));
				
		return view;
	}
	
	class CourseAdapter extends BaseAdapter{

		Context context;
		List<CourseDirectoryListItem> listItems;
		
		CourseAdapter(Context context, List<CourseDirectoryListItem> listItems){
			this.context = context;
			this.listItems = listItems;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listItems.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return listItems.indexOf(getItem(position));
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	            convertView = mInflater.inflate(R.layout.item_directory, null);
			}
			
			TextView textView1 = (TextView) convertView.findViewById(R.id.dicTextView1);
			TextView textView2 = (TextView) convertView.findViewById(R.id.dicTextView2);
			TextView textView3 = (TextView) convertView.findViewById(R.id.dicTextView3);
			Button button1 = (Button) convertView.findViewById(R.id.dicButton1);
			
			textView1.setText("text1");
			textView2.setText("text2");
			textView3.setText("text3");
			
			return convertView;
		}
	}
}