package fragments.newspaper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import singleton.Utility;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class xmlArticleDownloader extends AsyncTask<String, Integer, Integer> {
	private static final String TAG = "xmlArticleDownloader";
	private Utility utility = Utility.getInstance();
	private FTPClient mFTP = new FTPClient();
	private Context context;
	private File downloadFile;
	
	private String articleName;
	private String fullPath;
	
	/* xml을  ftp에 올린 후 result값을 받아올때까지 기다린다 이 때 사용하는 flag */
	private boolean downloadResult = false;
	
	public xmlArticleDownloader(Context context, String articleName){
		this.context = context;
		this.articleName = articleName;
	}
	
	@Override
	protected void onPreExecute(){
		super.onPreExecute();
    	/* Connect to FTP Server */
	}
	
	@Override
	protected Integer doInBackground(String... strings) {
		try {   
			fullPath = context.getFilesDir().getAbsolutePath() + "/xmls";
			downloadFile = new File(fullPath, "article.xml");
			
			Log.v(TAG, " xmlArticleDownloader fullPath = " + fullPath);
			
			mFTP.connect("210.118.64.248", 36735);
			mFTP.enterLocalPassiveMode();
			mFTP.login("igotit", "tjssm2014!@");
			mFTP.setFileType(FTP.BINARY_FILE_TYPE);
			mFTP.setFileTransferMode(FTP.BINARY_FILE_TYPE);
			mFTP.enterLocalPassiveMode();
			
			try {
				/* ftp 종료전에 *_result.xml 파일을 다운로드 한다. */
				FileOutputStream fos = new FileOutputStream(downloadFile);
				downloadResult = mFTP.retrieveFile("/articles/" + articleName, fos);
				fos.close();
			} catch (Exception e) {
				Log.v(TAG, "xmlArticleDownloader retrieveFile Exception = " + e);
			}
			if(downloadResult){
				Log.v(TAG, "xmlArticleDownloader retrieveFile 성공! ");
				utility.setArticleDown(true);
			}else {
				Log.v(TAG, "xmlArticleDownloader retrieveFile 실패! ");
				utility.setArticleDown(false);
			}
            
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	protected void onProgressUpdate(Integer...values){
		super.onProgressUpdate(values);
		Log.v(TAG, "xmlArticleDownloader onProgressUpdate");
	}
	
	@Override
	protected void onPostExecute(Integer result){
		Log.v(TAG, "xmlArticleDownloader onPostExecute");
		try {
    		Log.v(TAG, "xmlArticleDownloader onPostExecute4");
			mFTP.disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onCancelled(Integer result){
		Log.v(TAG, "xmlArticleDownloader onCancelled");
		super.onCancelled(result);
		try {
			/* ftp 종료 */
			mFTP.disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}