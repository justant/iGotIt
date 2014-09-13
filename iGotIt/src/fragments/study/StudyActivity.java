package fragments.study;

import java.util.ArrayList;
import java.util.List;

import singleton.Utility;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.igotit.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import database.DatabaseManager;

public class StudyActivity  extends Activity {
	private static final String TAG = "StudyActivity";
	private Utility utility = Utility.getInstance();
	
	private DatabaseManager dbAdapter;
	private ArrayList<Words> wordList;
	
	private List<StudyListItem> listItems;
	private ListView listView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_study);
		
		// SQLite Adapter 
		wordList = new ArrayList<Words>();
		this.dbAdapter = new DatabaseManager(this);
		// ���߿� getIntent()�� ���ؼ� �������̺��ȣ(ex:3102968)�� �ҷ��´�.
		loadDatabase("3102968");
		
		// �ܾ 100���� ������.
		listItems = new ArrayList<StudyListItem>();

		// ����Ʈ�� �ѷ��� �ܿ�ܾ ������ ī��Ʈ�Ѵ�.
		int i, learn = 0, id = 0;
		int wordSize = wordList.size();
		
		for (i = 0; i < wordSize; i++) {
			if(wordList.get(i).getLearn().equals("1")) learn++;
			
			if(i != 0 && (i + 1) % 100 == 0){
				listItems.add(new StudyListItem(id++, "title", 100, learn));
				learn = 0;
			}
		}
		
		// �������� 0�̶�°��� ���̻� �߰��Ұ� ����.
		if(i % 100 != 0){
			listItems.add(new StudyListItem(id, "title", i % 100, learn));
		}
		
		listView = (ListView) findViewById(R.id.studyListView);
		listView.setAdapter(new StudyAdapter(this, listItems));
		
	}	
	
	@Override
	public void onBackPressed() {
		ImageLoader.getInstance().stop();
		super.onBackPressed();
	}	
	
	// �ܾ������� �������ִ� ������ ���̺� �����Ͽ� DB������ �ҷ��´�.
	public void loadDatabase(String tableName){
		Log.v(TAG, "loadDatabase()");

		dbAdapter.open();
		Cursor cursor = dbAdapter.fetchAll(tableName);
		cursor.moveToPosition(0); 
		
		while (!cursor.isAfterLast()) {
			// id, word, pronunciation, mean, memo, lear
			wordList.add( new Words(cursor.getString(0), cursor.getString(1), cursor.getString(2),
					cursor.getString(3), cursor.getString(4), cursor.getString(5)) );
			cursor.moveToNext();
		}
		
		cursor.close();
		dbAdapter.close();
		
	}
	
	class StudyAdapter extends BaseAdapter{
		private LayoutInflater inflater = null;
		private List<StudyListItem> listItems = null;
		private ViewHolder viewHolder = null;
		private Context context = null;
		
		StudyAdapter(Context context, List<StudyListItem> listItems){
			this.context = context;
			this.inflater = LayoutInflater.from(context);
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			if(convertView == null){
	            convertView = inflater.inflate(R.layout.item_study, null);
	            
	            viewHolder = new ViewHolder();
	            viewHolder.ttt1 = (TextView)convertView.findViewById(R.id.ttt1);
	            viewHolder.ttt2 = (TextView)convertView.findViewById(R.id.ttt2);
	            viewHolder.ttt3 = (TextView)convertView.findViewById(R.id.ttt3);
	            
	            convertView.setTag(viewHolder);
	            
	            convertView.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						switch(event.getAction()){
						case MotionEvent.ACTION_DOWN:
							Log.v(TAG, "setOnTouchListener position = " + position);
							
							
							// onImageGridClick
							Intent intent = new Intent(context, SimpleImageActivity.class);
							intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageGridFragment.INDEX);
							
							// �н��� �����͵��� ���Ӱ� set���ش�.
							utility.getChapterWordList().clear();
							int startPos = position * 100;
							int wordSize = listItems.get(position).getWordSize();
							
							for(int i = startPos; i <startPos + wordSize; i++){
								utility.getChapterWordList().add(wordList.get(i));
								//Log.v(TAG, "wordList.get(i) = " + wordList.get(i));
							}
							
							startActivity(intent);
						}
						return false;
					}
				});
	            
			}else {
				viewHolder = (ViewHolder)convertView.getTag();
			}
			viewHolder.ttt1.setText(String.valueOf(listItems.get(position).getId() + 1));
			viewHolder.ttt2.setText(String.valueOf(listItems.get(position).getLearn()));
			viewHolder.ttt3.setText(String.valueOf(listItems.get(position).getWordSize()));
			
			return convertView;
		}
		
		 // getView�� �ӵ��� ���ؼ� ����Ѵ�
	    class ViewHolder{
	    	public int position;
	    	public TextView ttt1 = null;
	    	public TextView ttt2 = null;
	    	public TextView ttt3 = null;
	    }
	}
}
