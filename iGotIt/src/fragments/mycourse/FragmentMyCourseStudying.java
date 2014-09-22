package fragments.mycourse;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.igotit.R;

import database.DatabaseManager;
import fragments.study.StudyActivity;


public class FragmentMyCourseStudying extends SherlockFragment {
	private static final String TAG = "FragmentMyCourseStudying";
	private final String TABLE_NAME_VOCAINFO = "vocabularys";
	private DatabaseManager dbAdapter;
	
	private List<MyCourseListItem> myCourseListItems;
	private ListView listView; 
	private MyCourseAdapter myCourseAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		myCourseListItems = new ArrayList<MyCourseListItem>();
		this.dbAdapter = new DatabaseManager(getActivity());
		
		// 사용자가 다운받은 단어 정보들을 불러온다.
		loadDatabase(TABLE_NAME_VOCAINFO); 
		
		View view = inflater.inflate(R.layout.fragment_course, container, false);
		listView = (ListView) view.findViewById(R.id.studyListView);
		myCourseAdapter = new MyCourseAdapter(getActivity(), myCourseListItems);
		listView.setAdapter(myCourseAdapter);
		
		return view;
	}
	
	// 단어정보를 가지고있는 고유의 테이블에 접근하여 DB정보를 불러온다.
	public void loadDatabase(String tableName){
		Log.v(TAG, "loadDatabase()");

		dbAdapter.open();
		Cursor cursor = dbAdapter.fetchAll(tableName);
		cursor.moveToPosition(0); 
		
		myCourseListItems.clear();
		
		while (!cursor.isAfterLast()) {
			// vocabulary_id, name, editor, word_size, learn, type, state
			myCourseListItems.add(
					new MyCourseListItem(cursor.getString(1), cursor.getString(2), cursor.getString(3),
					cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7)) );
			cursor.moveToNext();
		}
		
		cursor.close();
		dbAdapter.close();
		
	}
	
	
	class MyCourseAdapter extends BaseAdapter{
		private Context context;
		private List<MyCourseListItem> listItems;
		private ViewHolder viewHolder = null;
		
		MyCourseAdapter(Context context, List<MyCourseListItem> listItems){
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
	            viewHolder.tvName = (TextView)convertView.findViewById(R.id.tv_name);
	            viewHolder.tvProgress = (TextView)convertView.findViewById(R.id.tv_progress);
	            viewHolder.tvWordSize = (TextView)convertView.findViewById(R.id.tv_word_size);
	            viewHolder.tvLearn = (TextView)convertView.findViewById(R.id.tv_learn);
	            viewHolder.progressBar = (ProgressBar)convertView.findViewById(R.id.mycourse_progressbar);
	            viewHolder.ivType = (ImageView)convertView.findViewById(R.id.iv_type);
	            viewHolder.btnStart = (Button)convertView.findViewById(R.id.btn_start);
	            // 버튼에 position 값을 태그한다.
	            viewHolder.btnStart.setTag(position);
	            convertView.setTag(viewHolder);	           
			}else {
				viewHolder = (ViewHolder)convertView.getTag();
			}
			
			viewHolder.tvName.setText(myCourseListItems.get(position).getName());
			int progressMax = myCourseListItems.get(position).getWordSize();
			int progressCurrent = myCourseListItems.get(position).getLearn();
			viewHolder.tvProgress.setText(String.valueOf((progressCurrent * 100) / progressMax) + "%");
			viewHolder.tvWordSize.setText(String.valueOf(myCourseListItems.get(position).getWordSize()));
			viewHolder.tvLearn.setText(String.valueOf(myCourseListItems.get(position).getLearn()));
			
			viewHolder.progressBar.setMax(progressMax);
			viewHolder.progressBar.setProgress(progressCurrent);
			String type = "ic_course_" + myCourseListItems.get(position).getType();
			viewHolder.ivType.setImageResource(
					context.getResources().getIdentifier(type, "drawable", context.getPackageName()) );
			
			viewHolder.btnStart.setOnClickListener(buttonClickListener);
			
			return convertView;
		}
	
		
		 private View.OnClickListener buttonClickListener = new View.OnClickListener() {
		        @Override
		        public void onClick(View view) {
		            switch (view.getId()) {
		             
		            // Start 버튼 클릭
		            case R.id.btn_start:

		            	// 버튼이 어떤 position의 버튼인지 구분한 값을 불러온다.
		            	int position= (Integer)view.getTag();
		            	
		            	Intent intent = new Intent(context, StudyActivity.class);
		            	intent.putExtra("vocabularyId", myCourseListItems.get(position).getVocabularyId());
		            	startActivity(intent);

		                break;
		 
		            default:
		                break;
		            }
		        }
		 };
		 
		 // getView의 속도를 위해서 사용한다
	    class ViewHolder{
	    	public TextView tvName = null;
	    	public TextView tvProgress = null;
	    	public TextView tvWordSize = null;
	    	public TextView tvLearn = null;
	    	public ProgressBar progressBar = null;
	        public ImageView ivType = null;
	        public Button btnStart = null;
	    }
	}
}