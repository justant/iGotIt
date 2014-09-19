package fragments.newspaper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.Random;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import singleton.Utility;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/*	외운 단어의 정보는 file.xml에 있다. 이 단어 정보를 FTP 파일 서버로 보내주고 result.xml 이라는 파일을 받아오는데
	이곳에는 내가 외운단어들이 가장 많이 포함된 기사들의 (제목과 포함된 단어수) 정보가 있다.							*/

public class xmlFileTransfer extends AsyncTask<String, Integer, Integer> {
	private static final String TAG = "xmlFileTransfer";
	private Utility utility = Utility.getInstance();
	private FTPClient mFTP = new FTPClient();
	private Context context;
	private File uploadFile;
	private File downloadFile;
	
	private String fullPath;
	private String random;
	/* xml을  ftp에 올린 후 result값을 받아올때까지 기다린다 이 때 사용하는 flag */
	private boolean downloadResult = false;
	private boolean uploadResult = false;
	
	public xmlFileTransfer(Context context){
		this.context = context;
	}
		
	@Override
	protected void onPreExecute(){
		super.onPreExecute();
		
    	/* Connect to FTP Server */
		
	}
	
	/* 단어의 제한시간 5.0초! */
	@Override
	protected Integer doInBackground(String... strings) {
		try {   
			fullPath = context.getFilesDir().getAbsolutePath() + "/xmls";
			uploadFile = new File(fullPath, utility.getFacebook_id() + ".xml");
			downloadFile = new File(fullPath, utility.getFacebook_id() + "_result.xml");
			
			Log.v(TAG, " xmlFileTransfer fullPath = " + fullPath);
			
			mFTP.connect("210.118.64.248", 36735);
			mFTP.enterLocalPassiveMode();
			mFTP.login("igotit", "tjssm2014!@");
			mFTP.setFileType(FTP.BINARY_FILE_TYPE);
			mFTP.setFileTransferMode(FTP.BINARY_FILE_TYPE);
			mFTP.enterLocalPassiveMode();
			
			/* Prepare file to be uploaded to FTP Server */
            FileInputStream fis = new FileInputStream(uploadFile);
            
            /* 1-100000 랜덤값 생성 */
            Random rand = new Random();
            long randomNum = rand.nextInt((100000 - 1) + 1) + 1;
            random = Long.toString(randomNum);
            
            /* 사용자 아이디_4자리 난수.xml  파일 업로드 */
            uploadResult = mFTP.storeFile(utility.getFacebook_id() + ".xml", fis);
            fis.close();
            
            /* upload 성공하면 기사관련정보를 받아올때까지 기다린다. 그리고 기사관련정보를 다운로드 한다. */
            if(uploadResult){
            	Log.v(TAG, "FTP upload Success !!!");
            	
            	/*  *_result.xml 파일의 사이즈가 0이 아니라면? 이제 파일을 다운받아도 좋다! */
            	long fileSize = 0;
            	FTPFile file = null;
            	
            	/* 0.5초씩 20번 = 10초만 검색해주고 그 이상을 넘어가면 아웃! */
            	for(int i=0; i<20; i++){
            		Log.v(TAG, "xmlFileTransfer Thread i = " + i);
            		
            		Thread.sleep(500);
            		try {
            			file = mFTP.mlistFile("/result/" + utility.getFacebook_id() + "_result.xml");
            			fileSize = file.getSize();
            			Log.v(TAG, "xmlFileTransfer *_result.xml SIZE = " + fileSize);
            			if(fileSize > 0) {
            				/* ftp 종료전에 *_result.xml 파일을 다운로드 한다. */
            				FileOutputStream fos = new FileOutputStream(downloadFile);
            	    		downloadResult = mFTP.retrieveFile("/result/" + utility.getFacebook_id() + "_result.xml", fos);
            	    		fos.close();
            	    		
            	    		/* 기사리스트 다운로드 성공 ! */
            	    		if(downloadResult){
            	    			Log.v(TAG, " xmlFileTransfer 기사리스트 다운로드 성공 randomnumber=" + random );
            	    			utility.setArticleListDown(true);
            	    		}
            				break;
            			}
            		}catch (Exception e){
            			Log.v(TAG, "xmlFileTransfer mlistFile Exception = " + e);
            		}
            	}
            }else {
            	Log.v(TAG, "FTP upload Fail...");
            }
            
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	protected void onProgressUpdate(Integer...values){
		super.onProgressUpdate(values);
		Log.v(TAG, "xmlFileTransfer onProgressUpdate");
	}
	
	@Override
	protected void onPostExecute(Integer result){
		Log.v(TAG, "xmlFileTransfer onPostExecute");
		try {
			
    		Log.v(TAG, "xmlFileTransfer onPostExecute4");
			
			mFTP.disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onCancelled(Integer result){
		Log.v(TAG, "xmlFileTransfer onCancelled");
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