package fragments.study;

import java.util.ArrayList;
import java.util.List;

import singleton.Utility;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.igotit.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import database.DatabaseManager;

public class StudyActivity extends Activity {
	private static final String TAG = "StudyActivity";
	private Utility utility = Utility.getInstance();

	private DatabaseManager dbAdapter;
	private ArrayList<Words> wordList;

	private StudyAdapter studyAdapter;
	private List<StudyListItem> listItems;
	private ListView listView;

	// wordList중에 필요한 단어들만 뽑아서 utility.chapterList를 만들어 준다.
	// 이때 wordList의 start index와 end index를 표현하는데 필요하다.
	private int startPos = 0;
	private int wordSize = 0;

	// 현재 오픈된 db 테이블의 이름
	private String vocabularyId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_study);

		// SQLite Adapter
		wordList = new ArrayList<Words>();
		this.dbAdapter = new DatabaseManager(this);

		// getIntent()를 통해서 고유테이블번호(ex:3102968)를 불러온다.
		Intent intent = getIntent();
		vocabularyId = intent.getStringExtra("vocabularyId");
		loadDatabase(vocabularyId);

		// 단어를 100개씩 나눈다.
		listItems = new ArrayList<StudyListItem>();

		// listItems 초기화
		initListItems();
		
		listView = (ListView) findViewById(R.id.studyListView);
		studyAdapter = new StudyAdapter(this, listItems);
		listView.setAdapter(studyAdapter);

	}
	
	// listItems 초기화
	private void initListItems() {
		listItems.clear();
		// 리스트에 뿌려줄 외운단어를 개수를 카운트한다.
		int i, learn = 0, id = 0;
		int wordSize = wordList.size();

		for (i = 0; i < wordSize; i++) {
			if (wordList.get(i).getLearn().equals("1"))
				learn++;

			if (i != 0 && (i + 1) % 100 == 0) {
				listItems.add(new StudyListItem(id++, "title", 100, learn));
				learn = 0;
			}
		}

		// 나머지가 0이라는것은 더이상 추가할게 없다.
		if (i % 100 != 0) {
			listItems.add(new StudyListItem(id, "title", i % 100, learn));
		}
		
	}

	@Override
	public void onBackPressed() {
		ImageLoader.getInstance().stop();
		super.onBackPressed();
	}

	// 단어정보를 가지고있는 고유의 테이블에 접근하여 DB정보를 불러온다.
	public void loadDatabase(String tableName) {
		Log.v(TAG, "loadDatabase()");

		dbAdapter.open();
		Cursor cursor = dbAdapter.fetchAll(tableName);
		cursor.moveToPosition(0);

		while (!cursor.isAfterLast()) {
			// id, word, pronunciation, mean, memo, lear
			wordList.add(new Words(cursor.getString(0), cursor.getString(1),
					cursor.getString(2), cursor.getString(3), cursor
							.getString(4), cursor.getString(5)));
			cursor.moveToNext();
		}

		cursor.close();
		dbAdapter.close();

	}

	class StudyAdapter extends BaseAdapter {
		private LayoutInflater inflater = null;
		private List<StudyListItem> listItems = null;
		private ViewHolder viewHolder = null;
		private Context context = null;

		StudyAdapter(Context context, List<StudyListItem> listItems) {
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_study, null);

				viewHolder = new ViewHolder();
				viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_name);
				viewHolder.tvProgress = (TextView) convertView.findViewById(R.id.tv_progress);
				viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.mycourse_progressbar);
				viewHolder.tvWordSize = (TextView) convertView.findViewById(R.id.tv_word_size);
				viewHolder.tvLearn = (TextView) convertView.findViewById(R.id.tv_learn);
				viewHolder.ivType = (ImageView) convertView.findViewById(R.id.iv_type);
				viewHolder.btnStart = (Button) convertView.findViewById(R.id.btn_start);
				// 버튼에 position 값을 태그한다.
	            viewHolder.btnStart.setTag(position);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			viewHolder.tvTitle.setText(String.valueOf(listItems.get(position).getId() + 1));
			int progressMax = listItems.get(position).getWordSize();
			int progressCurrent = listItems.get(position).getLearn();
			viewHolder.tvProgress.setText(String.valueOf((progressCurrent * 100) / progressMax) + "%");
			viewHolder.tvWordSize.setText(String.valueOf(listItems.get(position).getWordSize()));
			viewHolder.tvLearn.setText(String.valueOf(listItems.get(position).getLearn()));
			
			viewHolder.progressBar.setMax(progressMax);
			viewHolder.progressBar.setProgress(progressCurrent);
			
			// 나중에 스텝별로 이미지를 넣을 예정
			viewHolder.ivType.setImageResource(R.drawable.ic_step_test);
			
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
					Log.v(TAG, "setOnTouchListener position = "	+ position);
		
					// onImageGridClick
					Intent intent = new Intent(context, SimpleImageActivity.class);
					intent.putExtra(Constants.Extra.FRAGMENT_INDEX,	ImageGridFragment.INDEX);
		
					// 학습할 데이터들을 새롭게 set해준다.
					utility.getChapterWordList().clear();
					startPos = position * 100;
					wordSize = listItems.get(position).getWordSize();
		
					for (int i = startPos; i < startPos + wordSize; i++) {
						utility.getChapterWordList().add(
								wordList.get(i));
					}
		
					// request code 1 = gridFragment;
					startActivityForResult(intent, 1);

					break;

				default:
					break;
				}
			}
		};

		// getView의 속도를 위해서 사용한다
		class ViewHolder {
			public int position;
			public TextView tvTitle = null;
			public TextView tvProgress = null;
			public ProgressBar progressBar = null;
			public TextView tvWordSize = null;
			public TextView tvLearn = null;
			public ImageView ivType = null;
			public Button btnStart = null;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {

				// chapterList에서 변경된 부분을 wordList에 반영한다
				int cnt = 0;
				for (int i = startPos; i < startPos + wordSize; i++) {
					wordList.get(i).setLearn(
							utility.getChapterWordList().get(cnt++).getLearn());
				}
				// 학습이 끝난 후 DB에 학습정보를 저장한다. (startPos ~ endPos) 
				// ( 0 ~ 99 ), ( 100 ~ 199) 100단위로 끊어진다.
				updateDatabaseLearnState(vocabularyId, startPos, startPos + wordSize);
				
				// 리스트 아이템을 초기화해준다.
				initListItems();
				// view를 업데이트 해준다
				studyAdapter.notifyDataSetChanged();
			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}
	}

	public void updateDatabaseLearnState(String tableName, int whereStart,
			int whereEnd) {
		// _id 가 whereStart ~ whereEnd인 부분을 update
		Log.v(TAG, "updateDatabaseLearnState() whereStart = " + whereStart);
		Log.v(TAG, "updateDatabaseLearnState() whereEnd = " + whereEnd);

		String[] fields = { "learn" };
		int whereId;
		dbAdapter.open();
		dbAdapter.setBeginTransaction();
		
		for (int i = whereStart; i < whereEnd; i++) {
			String values[] = { wordList.get(i).getLearn() };
			whereId = i + 1;
			dbAdapter.update(tableName, fields, values, whereId);
		}
		
		dbAdapter.setTransactionSuccessful();
		dbAdapter.setEndTransaction();
		
		Log.v(TAG, "updateDatabaseLearnState() close");
		dbAdapter.close();
	}

}
