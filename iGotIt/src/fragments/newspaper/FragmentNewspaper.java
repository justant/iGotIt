package fragments.newspaper;

import java.util.ArrayList;

import singleton.Utility;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.igotit.R;

import database.DatabaseManager;

@SuppressLint("HandlerLeak")
public class FragmentNewspaper extends SherlockFragment {
	private static final String TAG = "FragmentNewspaper";
	private static int articleIndex = 0;
	private Utility utility = Utility.getInstance();
	
	private ProgressDialog progressDialog1;
	private ProgressDialog progressDialog2;

	/* �ܿ� �ܾ���� ����ִ� ArrayList */
	private ArrayList<String> word = new ArrayList<String>();
	private ArrayList<String> articleList = new ArrayList<String>();
	private ArrayList<String> articleWordInfo = new ArrayList<String>();
	
	private ArticleObject article;

	private DatabaseManager dbAdapter;

	private Button button_back;
	private Button button_refresh;
	private Button button_next;

	private TextView textview_newsdate;
	private TextView textview_contents;
	private Point touchPoint;

	private int touch_X, touch_Y;
	private int startWord, endWord;

	/* 0�̸� ���� 1�̸� ������, ��ư ������ ������ */
	private int directionButton=1;

	/* ��翡 null ���� */
	boolean nullManager = false;
	
	/* View ���������� ��� */
	private View mView;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_newspaper, null); // Fragment
		
		// ���̾ƿ�
		touchPoint = new Point();
	
		/* ��ư �� ������ ���� */
		button_back = (Button) view.findViewById(R.id.popsong_button_back);
		button_refresh = (Button) view
				.findViewById(R.id.popsong_button_refresh);
		button_next = (Button) view.findViewById(R.id.popsong_button_next);
	
		button_back.setOnClickListener(buttonListener);
		button_refresh.setOnClickListener(buttonListener);
		button_next.setOnClickListener(buttonListener);
	
		/* �ؽ�Ʈ�� ���� */
		textview_newsdate = (TextView) view.findViewById(R.id.textview_newsdata);
		textview_contents = (TextView) view.findViewById(R.id.textview_contents);
	
		/* Ŭ�� ������ ���� */
		textview_contents.setOnClickListener(clickListener);
	
		/* ��ġ ������ ���� */
		textview_contents.setOnTouchListener(touchListener);
	
		/* ���� Database�� Open! */
		this.dbAdapter = new DatabaseManager(this.getActivity());

		return view;
	}
	
	/* ��ư ������ */
	OnClickListener buttonListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			switch (view.getId()) {

			case R.id.popsong_button_back:
				Log.v(TAG, "NewspaperFragment button_back articleIndex = " + articleIndex);
				/* ��ư ���� ���� */
				directionButton=0;
				if(articleList.isEmpty()) {
					Log.v(TAG, "NewspaperFragment button_back articleList.isEmpty()");
					break;
				}
				if(articleIndex > 0) { articleIndex--; }
				else {articleIndex=articleList.size()-1;}
				/* ��縦 �̴� ������ */
				getArticleDownload();
				/* ��縦 �̴� �����带 �������ִ� �޴��� */
				timeArticleThread(getActivity());
				
				break;

			case R.id.popsong_button_refresh:
				Log.v(TAG, "NewspaperFragment button_refresh");
				
				/* �ε��� ���� �ʱ�ȭ */
				articleIndex = 0;
				utility.setArticleListDown(false);
				utility.setArticleDown(false);
				articleList.clear();
				
				generateXML(loadDatabase());
				
				/* Thread1 */
				xmlFileTransfer();
				/* Thread2 and Thread1 Manager */
				timeArticleListThread(getActivity());
				break;

			case R.id.popsong_button_next:
				Log.v(TAG, "NewspaperFragment button_next articleIndex = " + articleIndex);
				/* ��ư ���� ���� */
				directionButton=1;
				if(articleList.isEmpty()) {
					Log.v(TAG, "NewspaperFragment button_next articleList.isEmpty()");
					break;
				}
				if(articleIndex < articleList.size() - 1){ articleIndex++; }
				else {articleIndex = 0;}
				/* ��縦 �̴� ������ */
				getArticleDownload();
				/* ��縦 �̴� �����带 �������ִ� �޴��� */
				timeArticleThread(getActivity());
				
				break;
			}
		}
	};

	/* ��縮��Ʈ �ٿ�ε� ���� ���̾�α� */
	public void timeArticleListThread(final Context context){
		progressDialog1 = new ProgressDialog(context);
		progressDialog1.setTitle("Downloading a list of articles");
		progressDialog1.setMessage("Please wait while downloading...");
		progressDialog1.setIndeterminate(true);
		progressDialog1.setProgressStyle(R.style.NewDialog);
		progressDialog1.setCancelable(true);
		progressDialog1.show();
	
		new Thread(new Runnable() {
			public void run() {
				// TODO Auto-generated method stub
				progressDialog1.setCanceledOnTouchOutside(false);
				try {
					for(int i=0; i<200; i++){
						Thread.sleep(100);
						if(utility.isArticleListDown()){
							Thread.sleep(100);
							break;
						}
					}
				} catch (Throwable ex) {
					ex.printStackTrace();
				}
				/* ��縮��Ʈ �ٿ�޾����� ������ ��縦 ���� */
				handler1.sendEmptyMessage(1);
				
			}
		}).start();
	}
	
	Handler handler1 = new Handler(){
		public void handleMessage(Message msg){
			/* xml�� �ٲٱ� */
			getArticleListFromXML();
			progressDialog1.dismiss();
			/* ��縦 �̴� ������ */
			getArticleDownload();
			/* ��縦 �̴� �����带 �������ִ� �޴��� */
			timeArticleThread(getActivity());
		};
	};
	
	/* ��� �ٿ�ε� ���� ���̾�α� */
	public void timeArticleThread(final Context context){
		progressDialog2 = new ProgressDialog(context);
		progressDialog2.setTitle("Downloading a specific article");
		progressDialog2.setMessage("Please wait while downloading...");
		progressDialog2.setIndeterminate(true);
		progressDialog2.setProgressStyle(R.style.NewDialog);
		progressDialog2.setCancelable(true);
		progressDialog2.show();
	
		new Thread(new Runnable() {
			public void run() {
				progressDialog2.setCanceledOnTouchOutside(false);
				// TODO Auto-generated method stub
				try {
					for(int i=0; i<200; i++){
						Thread.sleep(100);
						Log.v(TAG, "progressDialog2 = utility.isArticleListDown" + utility.isArticleListDown());
						if(utility.isArticleListDown()){
							/* xml�� �ٲٱ� */
							Thread.sleep(500);

							break;
						}
					}
				} catch (Throwable ex) {
					ex.printStackTrace();
				}
				/* ��縦 �ٿ�޾����� ������ ��縦 �������� */
				handler2.sendEmptyMessage(1);
			}
		}).start();
	}
	
	Handler handler2 = new Handler(){
		public void handleMessage(Message msg){
			/* xml�� �ٲٱ� */
			progressDialog2.dismiss();
			/* xml���� ��縦 �̾Ƴ��� setText���� �Ѵ� */
			getArticleFromXML();
		};
	};
	
	private void setTextview() {
		/* ���� Ÿ��Ʋ ������ null �̸� ��� ���Ѵ� */		
		if(article.getTitle()==null || article.getContent()==null){
			Log.v(TAG,"newspaperFragment article.getTitle()==null || article.getContent()==null ");
			/* null ���� ������ ��� ������ ������ */
			new Thread(new Runnable() {
				public void run() {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(300);
						
						if (directionButton == 0) {
							button_back.performClick();
						} else if (directionButton == 1) {
							button_next.performClick();
						}
					} catch (Throwable ex) {
						ex.printStackTrace();
					}
				}
			}).start();
			
		} else {
			textview_contents.setText(article.toString());
			/* Gold �� */
			wordToSpanKnow(0xFFFFD700);
		}
		
		/* ����Ʈ ������ null �̸� ��� ���Ѵ� */		
		String newsDate = article.getNewsdate();
		if(newsDate != null){
			textview_newsdate.setText(extractDate(article.getNewsdate()));
		}else {
			textview_newsdate.setText("");
		}
	}

	private void getArticleFromXML() {
		xmlParseArticle xmlArticle = new xmlParseArticle(this.getActivity());
		article = xmlArticle.getArticle();
		/* �̰��� ���ܻ�Ȳ�̴� */
		if(article != null){
			setTextview();
		}
	}

	private void getArticleDownload() {
		Log.v(TAG, "NewspaperFragment getArticle() = " + articleList.get(articleIndex));
		new xmlArticleDownloader(this.getActivity(), articleList.get(articleIndex))
				.execute("article");
	}

	private void getArticleListFromXML() {
		xmlParseArticleList xmlList = new xmlParseArticleList(
				this.getActivity());
		articleList = xmlList.getArticleList();
		articleWordInfo = xmlList.getWordCountInfo();
		Log.v(TAG, "NewspaperFragment articleWordInfo = " + articleWordInfo.get(articleIndex));
//		for (int i = 0; i < articleList.size(); i++) {
//			Log.v(TAG,
//					"NewspaperFragment getArticleFromXML = "
//							+ articleList.get(i));
//		}
	}

	private void xmlFileTransfer() {
		new xmlFileTransfer(this.getActivity()).execute("news");
	}

	private void generateXML(ArrayList<String> word) {
		xmlGenerator generator = new xmlGenerator(this.getActivity(), word);
		generator.start();
	}

	private ArrayList<String> loadDatabase() {
//		/* word ArrayList �ʱ�ȭ */
//		word.clear();
//
//		/* DB open! */
//		dbAdapter.open();
//
//		/* SELECT word FROM word WHERE knowlevel > 0 */
//		Cursor cursor = dbAdapter.fetchKnowWord();
//		cursor.moveToFirst();
//
//		while (!cursor.isAfterLast()) {
//			word.add(cursor.getString(0));
//			Log.v(TAG, "NewspaperFramgnet cursor word = " + cursor.getString(0));
//			cursor.moveToNext();
//		}
//
//		/* HashSet ������ ���·� �����Ǹ鼭 �ߺ� ���ŵ� */
//		HashSet<String> hashSet = new HashSet<String>(word);
//
//		/* ArrayList �ٽ� ���� */
//		word.clear();
//		word = new ArrayList<String>(hashSet);
//
//		for (int i = 0; i < word.size(); i++) {
//			Log.v(TAG, "NewspapaerFragment HashSet word = " + word.get(i));
//		}
//
//		cursor.close();
//		dbAdapter.close();

		// ���� �ܿ� �ܾ�� test
		word.clear();
		word.add("because");
		word.add("concise");
		word.add("account");
		word.add("history");
		word.add("identify");
		word.add("easy");
		word.add("persist");
		word.add("row");
		word.add("learn");
		word.add("need");
		word.add("legal");

		return word;
	}

	/* ��ġ ������ */
	OnTouchListener touchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			Log.v(TAG, "MainActivity onTouch");

			touch_X = (int) event.getX();
			touch_Y = (int) event.getY();

			if (event.getAction() == MotionEvent.ACTION_MOVE) {

			}
			if (event.getAction() == MotionEvent.ACTION_DOWN) {

				/* Ŭ���� ȭ���� ��ǥ���� �޾ƿ´� */
				touch_X = (int) event.getX();
				touch_Y = (int) event.getY();

				/* Point ������ ��ȯ */
				touchPoint.set(touch_X, touch_Y);

				Log.v(TAG, "MainActivity onTouch_x = " + touchPoint.x);
				Log.v(TAG, "MainActivity onTouch_y = " + touchPoint.y);
			}

			return false;
		}
	};

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.v(TAG, "MainActivity onClick");
			/* ����Ŭ�� ���� */
			textview_contents.setEnabled(false);
			textview_contents.setClickable(false);

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					/* ����Ŭ�� ���� 1�ʰ� ���� Lock */
					textview_contents.setEnabled(true);
					textview_contents.setClickable(true);
				}
			}, 1000);

			String selection = findWord(textview_contents,
					((TextView) v).getSelectionStart());
			Log.v(TAG, "selection : " + selection);
			Log.v(TAG, "clickPoint_X = " + touchPoint.x);
			Log.v(TAG, "clickPoint_Y = " + touchPoint.y);

			/* �ܾ��� ���� �˻��غ��� ���� �ִٸ� popupâ�� ���� �����ش� */
			LoadWordMean laodWordMead = new LoadWordMean(getActivity());
			
			String mean = null;
			mean = laodWordMead.getRawMean(selection);
			if(mean != null){
				Log.v(TAG," �ǹ̰� �ִ�!");
				showWordMeanDialog(getActivity(), selection, mean);
			}
		}
	};

	// @Override
	// public void onWindowFocusChanged(boolean hasFocus) {
	// Log.v(TAG, "onWindowFocusChanged");
	//
	// if(wordClick){
	// /* 0xFFFFFF00 ��������� ���� */
	// wordToSpan(0xFFFFFF00);
	// wordClick = false;
	// }else {
	// /* 0xFFFFFF00 ��������� ���� */
	// wordToSpan(0x00FFFFFF);
	// wordClick = true;
	// }
	// }

	/* pop dialog */
	private void showWordMeanDialog(Context context, String word, String selection) {
		Log.v(TAG, "showWordMeanDialog()");
		final AlertDialog.Builder popDialog = new AlertDialog.Builder(getActivity());
		final LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);

		final View Viewlayout = inflater.inflate(R.layout.test_contents_wordmean_dialog,
				(ViewGroup) mView.findViewById(R.id.layout_wordmean_dialog));

		/* textview_wordmean ���� */
		final TextView textview_wordmean = (TextView) Viewlayout
				.findViewById(R.id.textview_wordmean);
		
		textview_wordmean.setText(selection);
		
		popDialog.setIcon(R.drawable.ic_circle_orange);
		popDialog.setTitle(word);
		popDialog.setView(Viewlayout);
		popDialog.create();
		popDialog.show();
	}

	/* Ŭ���� �����ǿ� �ִ� text�� word�� ���� */
	private String findWord(TextView textview, int position) {
		int strlen;
		/* ���� ��ç�ߴ��� �ٽ� ��������� ������ */
		wordToSpan(0x00FFFFFF);
		String textAll = textview.getText().toString();
		strlen = textAll.length();

		Log.v(TAG, "findWord textview " + textview);
		Log.v(TAG, "findWord position " + position);
		Log.v(TAG, "findWord strlen " + strlen);

		while (textAll.charAt(position) != ' ' && position > 0) {
			Log.v(TAG, "findWord startWord " + textAll.charAt(position));
			position--;
		}

		if (position <= 0) {
		} else {
			position++;
		}
		startWord = position;
		while (textAll.charAt(position) != ' ' && position < strlen) {
			Log.v(TAG, "findWord endWord " + textAll.charAt(position));
			position++;
		}
		endWord = position;
		wordToSpan(0xFF6A5ACD);		
		Log.v(TAG, "findWord = " + textAll.substring(startWord, endWord));
		return textAll.substring(startWord, endWord);
	}

	/* Ŭ���� �ܾ� �κп� ���� ������ */
	private void wordToSpan(int color) {
		Spannable WordToSpan = (Spannable) textview_contents.getText();
		WordToSpan.setSpan(new BackgroundColorSpan(color), startWord, endWord,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textview_contents.setText(WordToSpan);
	}
	
	/* �ܿ� �ܾ� �κп� ���� ������ */
	private void wordToSpanKnow(int color){
		String original = article.toString();
		Log.v(TAG, "NewspaperFragment articleWordInfo = " + articleWordInfo.get(articleIndex));
		/* �޸� �и��ڸ� �̿��ؼ� �ܿ�ܾ�鸸 �и��� ���� */
		String[] knowWords = articleWordInfo.get(articleIndex).split(",");
		Spannable WordToSpanKnow = (Spannable) textview_contents.getText();
		
		for(int i=0; i<knowWords.length; i++){
			String text = original;
			int index = text.indexOf(knowWords[i]);
			int substract = 0;
			Log.v(TAG, "NewspaperFragment wordToSpanKnow = " + knowWords[i]);
			while(index != -1){
				try{
					Log.v(TAG, "NewspaperFragment wordToSpanKnow index = "+index);
					Log.v(TAG, "NewspaperFragment wordToSpanKnow string = "+text);
				
					Log.v(TAG, "NewspaperFragment wordToSpanKnow original.charAt(index+substract-1) = "+original.charAt(index+substract-1));
					Log.v(TAG, "NewspaperFragment wordToSpanKnow original.charAt(index+substract+knowWords[i].length() = "+original.charAt(index+substract+knowWords[i].length()));
					char start = original.charAt(index+substract-1);
					char finish = original.charAt(index+substract+knowWords[i].length());
					if(original.charAt(index+substract-1)==' ' && original.charAt(index+substract+knowWords[i].length())==' '){
						Log.v(TAG, "NewspaperFragment wordToSpanKnow substring = "+original.substring(index+substract, index+substract+knowWords[i].length()));
						WordToSpanKnow.setSpan(new BackgroundColorSpan(color), index+substract, index+substract+knowWords[i].length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						textview_contents.setText(WordToSpanKnow);
						}
						text = text.substring(index+knowWords[i].length());
						substract += index+knowWords[i].length();
						index = text.indexOf(knowWords[i]);
				}catch (Exception e){
					Log.v(TAG, "WordToSpanKnow Exception e = " + e);
					break;
				}
			}
		}
	}
	
	/* �Ź���� ��¥ �����ϱ� */
	private String extractDate(String rawDate){
		String []monthString = {"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};
		
		int year;
		int month;
		int day;
		int lastString;
		
		/* rawDate�� �ΰ��̶�� */
		if(rawDate == null) return null;
		
		/* �켱 �ҹ��ڷ� �ٲ��ֱ� */
		rawDate.toLowerCase();
		
		/* last�� ���ԵȰ� ���ٸ� -1 ��ȯ*/
		lastString = rawDate.indexOf("last");
		if(lastString == -1){
			lastString = rawDate.indexOf("Last");
		}
		if(lastString == -1){
			lastString = rawDate.indexOf("--");
		}
		Log.v(TAG, " rawDate " + rawDate);
		if(lastString == -1){
			Log.v(TAG, "last�� --�� �Ѵ� ������ �ȵ� �̻��� �༮..");
			
			return null;
		}
		rawDate = rawDate.substring(0, lastString);
		
		return rawDate;
	}
	
}