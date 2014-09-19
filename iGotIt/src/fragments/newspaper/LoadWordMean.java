package fragments.newspaper;

import database.DatabaseManager;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class LoadWordMean {
	private static final String TAG = "LoadWordMean";
	private DatabaseManager dbAdapter;
	private Context context;
	
	LoadWordMean(Context context){
		this.context = context;
		
		/* 객체 생성과 동시에 dbAdapter 선언 */
		dbAdapter = new DatabaseManager(context);
	}
	
	String getRawMean (String word){
		String rawMean = null;
		rawMean = LoadMeandData(word);
		return rawMean;
	}
	
	String getMean(String word){
		Log.v(TAG, "LoadWordMean getMean 이것을 찾아라 word="+word);
		/* DB에 들어있는 정규식을 커치지 않은 raw 데이터 */
		String rawMean = null;
		/* 정규식을 거쳐서 진짜 mean */
		
		rawMean = LoadMeandData(word);
		
		/* rawMean null 이라는 것은 단어가 없다는 뜻 */ 
		if(rawMean == null){
			return null;
		}

		String mean = MakeRealMean(rawMean);
		
		return mean;
	}
	
	private String MakeRealMean(String rawMean){
		/* 정규식을 통해서 진짜 의미를 찾는다 */
		
		return null;
	}
	
	private String LoadMeandData(String word) {
		/* DB open! */
		dbAdapter.open();
		
		/* 소문자로 바꿔줘서 검색한다 */
		Cursor cursor = dbAdapter.fetchDicMean("dictionary", word.toLowerCase());
		
		/* 카운트가 0 이라는 것은 단어가 없다는 뜻 */
		if(cursor.getCount() == 0){
			return null;
		}
		
		cursor.moveToFirst();
		String rawMean = cursor.getString(0);
		Log.v(TAG, "LoadWordMean LoadMeandData rawMean = " + rawMean);
		
		return rawMean;
	}
}
