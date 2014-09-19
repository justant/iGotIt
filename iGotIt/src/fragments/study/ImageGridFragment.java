package fragments.study;

import image.google.ApiHttpUrlConnection;
import image.google.GoogleImage;
import image.google.ImageCache;
import image.google.Search;
import image.google.SearchState;
import image.google.GoogleImageSearchTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import singleton.Utility;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.cengalabs.flatui.FlatUI;
import com.cengalabs.flatui.views.FlatButton;
import com.cengalabs.flatui.views.FlatTextView;
import com.example.igotit.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ImageGridFragment extends AbsListViewBaseFragment implements Callback{
	public static final String TAG = "ImageGridFragment";
	private Utility utility = Utility.getInstance();
	
	public static final int INDEX = 1;
	private static final String PAGE_KEY = "page";
    private static final String QUERY_KEY = "query";
    public static final String FOOTER_KEY = "show_footer";
    // request minimum 50 elements
    public static final int MIN_ITEMS = 50;
    public static final String RESULT_KEY = "result";
    
    private static Handler mHandler;
    
	private FlatButton btnSkip;
	private FlatButton btnIGotIt;
	private FlatTextView textViewWord;
	private FlatTextView textViewMean;
	
	// tts
	private TextToSpeech mTTS;
	
	// ���� �˻� ��Ȳ ( �������� �����Ѵ� )
	private SearchState mSearchState;
	
	// �ܾ�("query")�� ���� �̹��� url ������ ������ �ִ�.
    private ImageAdapter imageAdapter;
    //private ImageView mLoading;
    private Animation mAnimation;
    private ExecutorService mExecutor;
    
    // ȭ���� �ƹ��͵� �ǵ��� ������ ����
    private boolean initState;
    
    // ���� �� ��° �ܾ ȭ�鿡 �����ִ��� �˷��ִ� ����
    private int wordPosition;
	DisplayImageOptions options;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.placeholder)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.error)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fr_image_grid, container, false);
		
		setUpFragment(rootView);
		mExecutor = Executors.newSingleThreadExecutor();
		imageAdapter = new ImageAdapter();
		
		listView = (GridView) rootView.findViewById(R.id.study_grid);
		((GridView) listView).setAdapter(imageAdapter);
		
		// ȭ�鿡 ó���� ���� �ܾ�, �ܾ� �� �׸��� �̹����� ����Ѵ�.
		screenRefresh(wordPosition);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// �ϴ��� ����
				// startImagePagerActivity(position);
			}
		});
		
		return rootView;
	}
	
	// Skip�� iGotIt ��ư�� Ŭ���� �� ������ ���� ��ũ���� refresh ���ش�.
	private void screenRefresh(int pos){
		// �ܾ� [����]
//		textViewWord.setText(
//				utility.getChapterWordList().get(pos).getWord()
//				+ " [" + utility.getChapterWordList().get(pos).getPronuciation() + "]" );
		// �ܾ�
		textViewWord.setText(
				utility.getChapterWordList().get(pos).getWord());

		// �� ( '|' �̰��� ����� �������ش�.)
		String mean = utility.getChapterWordList().get(pos).getMean();
		String result = "";
		if(mean != null){
			StringTokenizer token = new StringTokenizer(mean, "|");
			for(int i = 0; i < token.countTokens() - 1; i++){
				result += token.nextToken() + ", ";
			}
			result += token.nextToken();
		}
		textViewMean.setText(result);
		//textViewMean.setText("textViewMean.setText(utility.getChapterWordList().get(pos).getMean());");
		
		//textViewMean.setText(utility.getChapterWordList().get(pos).getMean());
		// �ش� �ܾ� �̹��� �˻�
		getImageUrlInformation(utility.getChapterWordList().get(pos).getWord());
	}

	private void setUpFragment(View rootView) {
		// Flat UI
		FlatUI.initDefaultValues(this.getActivity());
		FlatUI.setDefaultTheme(FlatUI.ORANGE);
		
		// �ؽ�Ʈ ��ü ����
		textViewWord = (FlatTextView) rootView.findViewById(R.id.grid_word);
		textViewMean = (FlatTextView) rootView.findViewById(R.id.grid_mean);
		textViewWord.setOnTouchListener(touchListener);
		
		// ��ư ��ü ����
		btnSkip = (FlatButton) rootView.findViewById(R.id.btn_skip);
		btnIGotIt = (FlatButton) rootView.findViewById(R.id.btn_igotit);
		btnSkip.setOnClickListener(clickListener);
		btnIGotIt.setOnClickListener(clickListener);
		
		
		// �ε� �̹���
		//mLoading = (ImageView) rootView.findViewById(R.id.loading_image);
		//mImageList = new ArrayList<GoogleImage>();
		mHandler = new Handler(this);
		
        mAnimation = new RotateAnimation(0.0F, 360F, 1, 0.5F, 1, 0.5F);
        mAnimation.setDuration(700L);
        mAnimation.setRepeatCount(Animation.INFINITE);
        setSearchState();
        
        // true��� ȭ���� �ƹ��͵� �ǵ��� ���� ����.
        initState = true;
        wordPosition = 0;
        
        // tts setting
        mTTS = new TextToSpeech(this.getActivity(), ttsListener);
	}
	
	private void setSearchState() {
        this.mSearchState = SearchState.getInstance();
    }

	public class ImageAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		// ���� �˻��� ���� �ִٸ� �� ������ �˻��ϱ� ���� ĳ��
		private LruCache<String, Bitmap> mImageCache;

		public ImageAdapter() {
			inflater = LayoutInflater.from(getActivity());
			this.mImageCache = ImageCache.getInstance().getLruCache();
		}
		
		@Override
		public int getCount() {
			return utility.getGoogleImageList().size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			View view = convertView;
			if (view == null) {
				view = inflater.inflate(R.layout.item_grid_image, parent, false);
				holder = new ViewHolder();
				assert view != null;
				holder.imageView = (ImageView) view.findViewById(R.id.image);
				holder.imageView.setBackgroundResource(R.drawable.placeholder);
				holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			ImageLoader.getInstance()
					.displayImage(utility.getGoogleImageList().get(position).getTbUrl(), holder.imageView, options, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							holder.progressBar.setProgress(0);
							holder.progressBar.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
							holder.progressBar.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
							holder.progressBar.setVisibility(View.GONE);
						}
					}, new ImageLoadingProgressListener() {
						@Override
						public void onProgressUpdate(String imageUri, View view, int current, int total) {
							holder.progressBar.setProgress(Math.round(100.0f * current / total));
						}
					});

			return view;
		}
	}

	static class ViewHolder {
		ImageView imageView;
		ProgressBar progressBar;
	}
	
	// tts ������
	private OnInitListener ttsListener = new OnInitListener() {

		@Override
		public void onInit(int status) {
			// TODO Auto-generated method stub
			// isInit = status == TextToSpeech.SUCCESS;

		}
	};
	
	// ��ġ ������ ( �ܾ �������� tts ����)
	OnTouchListener touchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			Log.v(TAG,"touchListener");
			
			switch(view.getId()){
			
			case R.id.grid_word :
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					Log.v(TAG,"touch grid_word");
					// tts
					playTTS();
				}
				break;
			}

			return false;
		}
	};
	
	// ��ư ������
	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			Log.v(TAG,"clickListener");
				
			switch(view.getId()){
			
			case R.id.btn_skip:
				Log.v(TAG, "btn_skip Click!");
				stopCurrentTask();
				utility.getChapterWordList().get(wordPosition).setLearn("0");
				
				wordPosition = findNextPosition(wordPosition);
				if(wordPosition != -1)screenRefresh(wordPosition);
				else {
					// ������ �������� ���̻� �ܿ�� ����.
					finishFragment(getActivity());
				}
				
				break;
				
			case R.id.btn_igotit:
				Log.v(TAG, "btn_igotit Click!");
				stopCurrentTask();
				utility.getChapterWordList().get(wordPosition).setLearn("1");
				
				wordPosition = findNextPosition(wordPosition);
				if(wordPosition != -1)screenRefresh(wordPosition);
				else {
					// ������ �������� ���̻� �ܿ�� ����.
					finishFragment(getActivity());
					
				}
				
				break;			
			}
		}
	};
	
	// fragment ����
	public void finishFragment(Activity act){
		Intent returnIntent = new Intent();
		act.setResult(act.RESULT_OK, returnIntent);
		act.finish();
	}
	
	// ���� �ܿ� �ܾ��� ��ġ�� ã���ش�.
	public int findNextPosition(int pos){
		int i;
		for(i = pos + 1; i < utility.getChapterWordList().size(); i++){
			// learn ���°� "0" �϶� break
			if(utility.getChapterWordList().get(i).getLearn().equals("0"))break;
		}
		// ��� �ܿ� ���¶��
		if(i == utility.getChapterWordList().size()) return -1;
		
		return i;
	}
	
	// ������� ������, �ڵ鷯 ��� ����
	public void stopCurrentTask(){
		// ���� �ް��ִ� �̹����� ��� stop
		ImageLoader.getInstance().stop();
		// ���� �����忡 �����ִ� Ǯ ��� stop				
		mExecutor.shutdownNow();
		mExecutor = Executors.newSingleThreadExecutor();
        mHandler.removeCallbacksAndMessages(null);
	}
	
	public void getImageUrlInformation(String word){
		utility.getGoogleImageList().clear();
		mSearchState.reset();
		String query = word.toString();
		mSearchState.setCurrentSearch(new Search(-1, query, -1));
		
		try{
			if (isNetworkAvailable()) {
	            downloadImagePagesFor(query);
	        } else {
	            Toast.makeText(getActivity(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
	        }	
		} catch (IllegalArgumentException e) {
            Log.e(TAG, "Insert Error ", e);
        }
	}
	
	public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo != null) && activeNetworkInfo.isConnected();
    }
	
	
	private void downloadImagePagesFor(String query) {
        //mLoading.setVisibility(View.VISIBLE);
        //mLoading.startAnimation(mAnimation);
        int count = mSearchState.getPagination().getCountPagesForItemIndex(MIN_ITEMS);
        for (int i = 0; i <= count; i++) {
            String[] params = new String[] { query, mSearchState.getPagination().getStartForNextPage().toString() };
            getMoreResults(params);
        }
    }
	
	private void getMoreResults(String[] params) {
        try {
            GoogleImageSearchTask searchTask = new GoogleImageSearchTask(mHandler, new ApiHttpUrlConnection(params));
            searchTask.executeOnExecutor(mExecutor);
        } catch (IOException e) {
            mSearchState.setHasError(true);
            Log.e(TAG, "Error on API Request", e);
        }
    }
	
	@Override
    public void onResume() {
        super.onResume();
        imageAdapter.notifyDataSetChanged();
    }
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String word = utility.getChapterWordList().get(wordPosition).getWord();
        if (word != null && !TextUtils.isEmpty(word)) {
            outState.putString(QUERY_KEY, word.toString());
        }
        outState.putInt(PAGE_KEY, mSearchState.getPagination().getCurrentPage());
        outState.putParcelableArrayList(RESULT_KEY, utility.getGoogleImageList());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.mSearchState.reset();
        this.mSearchState = null;
        this.listView.setAdapter(null);
        this.imageAdapter = null;
        this.mExecutor.shutdownNow();
        mHandler.removeCallbacksAndMessages(null);
        // tts ����
 		mTTS.stop();
 		mTTS.shutdown();
    }
    
	@Override
	public boolean handleMessage(Message msg) {
        Log.v(TAG, "Handle msg");
        ArrayList<GoogleImage> resultList = msg.getData().getParcelableArrayList(RESULT_KEY);
        if (resultList != null) {
        	utility.getGoogleImageList().addAll(resultList);
            // gridview setadapter
	            if (imageAdapter != null) {
	            	imageAdapter.notifyDataSetChanged();
	            }
        }
        
        //setFooterVisibility(View.INVISIBLE);
        //mLoading.setVisibility(View.GONE);
        //mLoading.clearAnimation();
        return true;
    }
	
	private void playTTS(){
		Log.v(TAG, "playTTS");
		mTTS.setLanguage(Locale.US);									//��� ����.
		mTTS.setPitch(1.0f);
		mTTS.setSpeechRate(1.0f);
		//mTTS.setPitch(mPitch.getProgress()/10.0f);					//pitch ����.
		//mTTS.setSpeechRate(mRate.getProgress()/10.0f);				//rate ����.
		mTTS.speak(utility.getChapterWordList().get(wordPosition).getWord()
				, TextToSpeech.QUEUE_FLUSH, null);	//�ش� ���� �ؽ�Ʈ ���� ���
	}
}

