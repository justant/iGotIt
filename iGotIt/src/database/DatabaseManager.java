package database;

import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager {
	private static final String TAG = "DataBaseManager";
	private static final String DATABASE_NAME = "iGotIt.sqlite";
	private static final int DATABASE_VERSION = 1;

	private SQLiteHelper mDBHelper;
	private SQLiteDatabase mDB;
	private final Context mContext;

	public DatabaseManager(Context context) {
		this.mContext = context;
	}
	// DB를 만들거나 업그레이드 할 때 사용한다.
	private static class SQLiteHelper extends SQLiteOpenHelper {
		SQLiteHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// 이미 DB가 만들어 졌으므로 필요 없다
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.v(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			// 이미 DB를 업데이트할 일이 없다.
			onCreate(db);
		}
	}
	
	public DatabaseManager open() throws SQLException {
		mDBHelper = new SQLiteHelper(mContext);
		mDB = mDBHelper.getWritableDatabase();
		// 최적화를 위해 동기화 off
		mDB.execSQL("PRAGMA synchronous = OFF");

		Log.v(TAG, "database open");
		return this;
	}

	public void close() {
		mDBHelper.close();
	}
	
	// Transaction 시작. 
	public void setBeginTransaction(){
		mDB.beginTransaction();
	}
	
	// Transaction 성공.
	public void setTransactionSuccessful(){
		mDB.setTransactionSuccessful();
	}
	
	// Transaction 종료.
	public void setEndTransaction(){
		mDB.endTransaction();
	}
	
	// insert 구현
	// delete 구현
	
	// update 구현
	public void update(String tableName, String[] fields, String[] values) {
		// 필드와 값의 갯수가 같지 않으면 오류 발생
		if(fields.length != values.length){
			Log.v(TAG, "update : fields.length is not equal to values.length");
			return;
		}
		
		int i;
		String sql = "UPDATE " + tableName + " SET ";
		for(i = 0; i<fields.length - 1; i++){
			sql += fields[i] + " = '" + values[i] + "', ";
		}
		sql += fields[i] + " = '" + values[i] + "'";
		mDB.execSQL(sql);
		
		Log.v(TAG, "update sql = " + sql);
	}
	
	public void update(String tableName, String[] fields, String[] values, String where) {
	
	}
		
		
		
//		if (tableName.equals("word") && fieldNumber == 4) {
//			/* fields[0] = word_ID, fields[1] = knowlevel, fields[2] = knowtime, fields[3] = knowfile */
//			Log.v(TAG, "SQL(UPDATE) TABLE NAME = " + tableName + "fields[0]="+fields[0]+", fields[1]="+fields[1]+", fields[2]="+fields[2]+", fields[3]="+fields[3]);
//			mDB.execSQL("UPDATE " + tableName + " SET " + "knowlevel='" + Integer.parseInt(fields[1])
//						+ "', knowtime='" + fields[2] + "', knowfile='" + fields[3] + "' " + "WHERE word_ID=" + fields[0]);
//		
//		}	
	 
	
	// fetch
	
	// tableName의 모든 DB를 fetch한다.
	public Cursor fetchAll(String tableName) {
		Log.v(TAG, " fetchAll data , tableName : " + tableName);
		return mDB.rawQuery("SELECT * FROM " + tableName, null);
	}

}
