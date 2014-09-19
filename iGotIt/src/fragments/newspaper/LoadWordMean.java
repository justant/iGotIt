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
		
		/* ��ü ������ ���ÿ� dbAdapter ���� */
		dbAdapter = new DatabaseManager(context);
	}
	
	String getRawMean (String word){
		String rawMean = null;
		rawMean = LoadMeandData(word);
		return rawMean;
	}
	
	String getMean(String word){
		Log.v(TAG, "LoadWordMean getMean �̰��� ã�ƶ� word="+word);
		/* DB�� ����ִ� ���Խ��� Ŀġ�� ���� raw ������ */
		String rawMean = null;
		/* ���Խ��� ���ļ� ��¥ mean */
		
		rawMean = LoadMeandData(word);
		
		/* rawMean null �̶�� ���� �ܾ ���ٴ� �� */ 
		if(rawMean == null){
			return null;
		}

		String mean = MakeRealMean(rawMean);
		
		return mean;
	}
	
	private String MakeRealMean(String rawMean){
		/* ���Խ��� ���ؼ� ��¥ �ǹ̸� ã�´� */
		
		return null;
	}
	
	private String LoadMeandData(String word) {
		/* DB open! */
		dbAdapter.open();
		
		/* �ҹ��ڷ� �ٲ��༭ �˻��Ѵ� */
		Cursor cursor = dbAdapter.fetchDicMean("dictionary", word.toLowerCase());
		
		/* ī��Ʈ�� 0 �̶�� ���� �ܾ ���ٴ� �� */
		if(cursor.getCount() == 0){
			return null;
		}
		
		cursor.moveToFirst();
		String rawMean = cursor.getString(0);
		Log.v(TAG, "LoadWordMean LoadMeandData rawMean = " + rawMean);
		
		return rawMean;
	}
}
