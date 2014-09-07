package fragments.mycourse;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.igotit.R;


public class FragmentMyCourseStudying extends SherlockFragment {
	private List<MyCourseListItems> listItems;
	private ListView listView; 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		listItems = new ArrayList<MyCourseListItems>();
		 
		for (int i = 0; i < 10; i++) {
			MyCourseListItems item = new MyCourseListItems();
			listItems.add(item);
		}
		
		View view = inflater.inflate(R.layout.fragment_course, container, false);
		listView = (ListView) view.findViewById(R.id.courseListView);
		listView.setAdapter(new MyCourseAdapter(getActivity(), listItems));
		
		return view;
	}
	
	
	
	
	class MyCourseAdapter extends BaseAdapter{
		private Context context;
		private List<MyCourseListItems> listItems;
		private ViewHolder viewHolder = null;
		
		MyCourseAdapter(Context context, List<MyCourseListItems> listItems){
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
	            convertView = mInflater.inflate(R.layout.item_mycourse, null);
	            
	            viewHolder = new ViewHolder();
	            viewHolder.tv1 = (TextView)convertView.findViewById(R.id.textView1);
	            viewHolder.tv2 = (TextView)convertView.findViewById(R.id.textView2);
	            viewHolder.tv3 = (TextView)convertView.findViewById(R.id.textView3);
	            viewHolder.tv4 = (TextView)convertView.findViewById(R.id.textView4);
	            viewHolder.pb1 = (ProgressBar)convertView.findViewById(R.id.seekbar1);
	            viewHolder.iv1 = (ImageView)convertView.findViewById(R.id.imageView1);
	            viewHolder.btn1 = (Button)convertView.findViewById(R.id.button1);
	            //viewHolder.btn1.setFocusable(false);;
	            
	            convertView.setTag(viewHolder);	           
			}else {
				viewHolder = (ViewHolder)convertView.getTag();
			}
			
			viewHolder.btn1.setOnClickListener(buttonClickListener);
			
			return convertView;
		}
		
		 private View.OnClickListener buttonClickListener = new View.OnClickListener() {
		        @Override
		        public void onClick(View v) {
		            switch (v.getId()) {
		             
		            // 이미지 클릭
//		            case R.id.iv_image:
//		                Toast.makeText(
//		                        mContext, 
//		                        "이미지 Tag = " + v.getTag(),
//		                        Toast.LENGTH_SHORT
//		                        ).show();
//		                break;
		             
		            // 버튼 클릭
		            case R.id.button1:
					Toast.makeText(context, "버튼 Tag = " + v.getTag(),
							Toast.LENGTH_SHORT).show();
		                break;
		 
		            default:
		                break;
		            }
		        }
		 };
		 
		 // getView의 속도를 위해서 사용한다
	    class ViewHolder{
	    	public TextView tv1 = null;
	    	public TextView tv2 = null;
	    	public TextView tv3 = null;
	    	public TextView tv4 = null;
	    	public ProgressBar pb1 = null;
	        public ImageView iv1 = null;
	        public Button btn1 = null;
	    }
	}
}