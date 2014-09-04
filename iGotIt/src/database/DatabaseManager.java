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
	// DB�� ����ų� ���׷��̵� �� �� ����Ѵ�.
	private static class SQLiteHelper extends SQLiteOpenHelper {
		SQLiteHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// �̹� DB�� ����� �����Ƿ� �ʿ� ����
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.v(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			// �̹� DB�� ������Ʈ�� ���� ����.
			onCreate(db);
		}
	}
	
	public DatabaseManager open() throws SQLException {
		mDBHelper = new SQLiteHelper(mContext);
		mDB = mDBHelper.getWritableDatabase();
		// ����ȭ�� ���� ����ȭ off
		mDB.execSQL("PRAGMA synchronous = OFF");

		Log.v(TAG, "database open");
		return this;
	}

	public void close() {
		mDBHelper.close();
	}
	
	// Transaction ����. 
	public void setBeginTransaction(){
		mDB.beginTransaction();
	}
	
	// Transaction ����.
	public void setTransactionSuccessful(){
		mDB.setTransactionSuccessful();
	}
	
	// Transaction ����.
	public void setEndTransaction(){
		mDB.endTransaction();
	}
	
	// insert ����
	// delete ����
	
	// update ����
	public void update(String tableName, String[] fields, String[] values) {
		// �ʵ�� ���� ������ ���� ������ ���� �߻�
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
	
	// tableName�� ��� DB�� fetch�Ѵ�.
	public Cursor fetchAll(String tableName) {
		Log.v(TAG, " fetchAll data , tableName : " + tableName);
		return mDB.rawQuery("SELECT * FROM " + tableName, null);
	}

}
