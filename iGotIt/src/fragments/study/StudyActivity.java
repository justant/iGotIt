package fragments.study;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import singleton.Utility;
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

	// wordList�߿� �ʿ��� �ܾ�鸸 �̾Ƽ� utility.chapterList�� ����� �ش�.
	// �̶� wordList�� start index�� end index�� ǥ���ϴµ� �ʿ��ϴ�.
	private int startPos = 0;
	private int wordSize = 0;

	// ���� ���µ� db ���̺��� �̸�
	private String vocabularyId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_study);

		// SQLite Adapter
		wordList = new ArrayList<Words>();
		this.dbAdapter = new DatabaseManager(this);

		// getIntent()�� ���ؼ� �������̺��ȣ(ex:3102968)�� �ҷ��´�.
		Intent intent = getIntent();
		vocabularyId = intent.getStringExtra("vocabularyId");
		loadDatabase(vocabularyId);

		// �ܾ 100���� ������.
		listItems = new ArrayList<StudyListItem>();

		// listItems �ʱ�ȭ
		initListItems();
		
		listView = (ListView) findViewById(R.id.studyListView);
		studyAdapter = new StudyAdapter(this, listItems);
		listView.setAdapter(studyAdapter);

	}
	
	// listItems �ʱ�ȭ
	private void initListItems() {
		listItems.clear();
		// ����Ʈ�� �ѷ��� �ܿ�ܾ ������ ī��Ʈ�Ѵ�.
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

		// �������� 0�̶�°��� ���̻� �߰��Ұ� ����.
		if (i % 100 != 0) {
			listItems.add(new StudyListItem(id, "title", i % 100, learn));
		}
		
	}

	@Override
	public void onBackPressed() {
		ImageLoader.getInstance().stop();
		super.onBackPressed();
		
	}

	// �ܾ������� �������ִ� ������ ���̺� �����Ͽ� DB������ �ҷ��´�.
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
		private int progressMax;
		private int progressCurrent;
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
				// ��ư�� position ���� �±��Ѵ�.
	            viewHolder.btnStart.setTag(position);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			viewHolder.tvTitle.setText(String.valueOf(listItems.get(position).getTitle()));
			progressMax = listItems.get(position).getWordSize();
			progressCurrent = listItems.get(position).getLearn();
			viewHolder.tvProgress.setText(String.valueOf((progressCurrent * 100) / progressMax) + "%");
			viewHolder.tvWordSize.setText(String.valueOf(listItems.get(position).getWordSize()));
			viewHolder.tvLearn.setText(String.valueOf(listItems.get(position).getLearn()));
			
			viewHolder.progressBar.setMax(progressMax);
			viewHolder.progressBar.setProgress(progressCurrent);
			
			// ���߿� ���ܺ��� �̹����� ���� ����
			viewHolder.ivType.setImageResource(R.drawable.ic_step_test);
			
			viewHolder.btnStart.setOnClickListener(buttonClickListener);

			return convertView;
		}

		private View.OnClickListener buttonClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				switch (view.getId()) {

				// Start ��ư Ŭ��
				case R.id.btn_start:

					if(progressCurrent < progressMax){
						// ��ư�� � position�� ��ư���� ������ ���� �ҷ��´�.
						int position= (Integer)view.getTag();
						Log.v(TAG, "setOnTouchListener position = "	+ position);
			
						// onImageGridClick
						Intent intent = new Intent(context, SimpleImageActivity.class);
						intent.putExtra(Constants.Extra.FRAGMENT_INDEX,	ImageGridFragment.INDEX);
			
						// �н��� �����͵��� ���Ӱ� set���ش�.
						utility.getChapterWordList().clear();
						startPos = position * 100;
						wordSize = listItems.get(position).getWordSize();
			
						String mean;
						String result;
						int cnt = 0;
						for (int i = startPos; i < startPos + wordSize; i++) {
							utility.getChapterWordList().add(wordList.get(i));
							
							// �� ( '|' �̰��� ����� �������ش�.)
							mean = utility.getChapterWordList().get(cnt).getMean();
							result = "";
							if(mean != null){
								StringTokenizer token = new StringTokenizer(mean, "|");
								for(int j = 0; j < token.countTokens() - 1; j++){
									result += token.nextToken() + ", ";
								}
								result += token.nextToken();
							}
							
							utility.getChapterWordList().get(cnt++).setMean(result);
						}
			
						// request code 1 = gridFragment;
						startActivityForResult(intent, ImageGridFragment.INDEX);

						break;
					}else {
						Log.v(TAG, " ��� �ܿ����ϴ� ");
						// ����ٰ� ���̾�α� â�� ����� ���� �ʱ�ȭ �Ұųİ� ����ڿ��� �����.
					}

				default:
					break;
				}
			}
		};

		// getView�� �ӵ��� ���ؼ� ����Ѵ�
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

		if (requestCode == ImageGridFragment.INDEX) {
			if (resultCode == RESULT_OK) {

				// chapterList���� ����� �κ��� wordList�� �ݿ��Ѵ�
				int cnt = 0;
				for (int i = startPos; i < startPos + wordSize; i++) {
					wordList.get(i).setLearn(utility.getChapterWordList().get(cnt++).getLearn());
				}
				// �н��� ���� �� DB�� �н������� �����Ѵ�. (startPos ~ endPos) 
				// ( 0 ~ 99 ), ( 100 ~ 199) 100������ ��������.
				updateDatabaseLearnState(vocabularyId, startPos, startPos + wordSize);
				updateDatabaseVocaInfo(vocabularyId);
				// ����Ʈ �������� �ʱ�ȭ���ش�.
				initListItems();
				// view�� ������Ʈ ���ش�
				studyAdapter.notifyDataSetChanged();
			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}
	}
	
	// Mycourse ȭ�鿡�� ������ ������ db�� ������Ʈ �Ѵ�.
	public void updateDatabaseVocaInfo(String tableName){
		int learn = 0;
		for(int i = 0; i < wordList.size(); i++){
			if(wordList.get(i).getLearn() == "1") learn++;
		}
		String[] fields = { "learn" };
		String values[] = { String.valueOf(learn) };
		dbAdapter.open();
		dbAdapter.update(tableName, fields, values, 1);
		dbAdapter.close();
	}
	
	// � �ܾ �ܿ����� db�� �����Ѵ�.
	public void updateDatabaseLearnState(String tableName, int whereStart,
			int whereEnd) {
		// _id �� whereStart ~ whereEnd�� �κ��� update

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
