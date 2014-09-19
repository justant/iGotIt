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

/*	�ܿ� �ܾ��� ������ file.xml�� �ִ�. �� �ܾ� ������ FTP ���� ������ �����ְ� result.xml �̶�� ������ �޾ƿ��µ�
	�̰����� ���� �ܿ�ܾ���� ���� ���� ���Ե� ������ (����� ���Ե� �ܾ��) ������ �ִ�.							*/

public class xmlFileTransfer extends AsyncTask<String, Integer, Integer> {
	private static final String TAG = "xmlFileTransfer";
	private Utility utility = Utility.getInstance();
	private FTPClient mFTP = new FTPClient();
	private Context context;
	private File uploadFile;
	private File downloadFile;
	
	private String fullPath;
	private String random;
	/* xml��  ftp�� �ø� �� result���� �޾ƿö����� ��ٸ��� �� �� ����ϴ� flag */
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
	
	/* �ܾ��� ���ѽð� 5.0��! */
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
            
            /* 1-100000 ������ ���� */
            Random rand = new Random();
            long randomNum = rand.nextInt((100000 - 1) + 1) + 1;
            random = Long.toString(randomNum);
            
            /* ����� ���̵�_4�ڸ� ����.xml  ���� ���ε� */
            uploadResult = mFTP.storeFile(utility.getFacebook_id() + ".xml", fis);
            fis.close();
            
            /* upload �����ϸ� ������������ �޾ƿö����� ��ٸ���. �׸��� ������������ �ٿ�ε� �Ѵ�. */
            if(uploadResult){
            	Log.v(TAG, "FTP upload Success !!!");
            	
            	/*  *_result.xml ������ ����� 0�� �ƴ϶��? ���� ������ �ٿ�޾Ƶ� ����! */
            	long fileSize = 0;
            	FTPFile file = null;
            	
            	/* 0.5�ʾ� 20�� = 10�ʸ� �˻����ְ� �� �̻��� �Ѿ�� �ƿ�! */
            	for(int i=0; i<20; i++){
            		Log.v(TAG, "xmlFileTransfer Thread i = " + i);
            		
            		Thread.sleep(500);
            		try {
            			file = mFTP.mlistFile("/result/" + utility.getFacebook_id() + "_result.xml");
            			fileSize = file.getSize();
            			Log.v(TAG, "xmlFileTransfer *_result.xml SIZE = " + fileSize);
            			if(fileSize > 0) {
            				/* ftp �������� *_result.xml ������ �ٿ�ε� �Ѵ�. */
            				FileOutputStream fos = new FileOutputStream(downloadFile);
            	    		downloadResult = mFTP.retrieveFile("/result/" + utility.getFacebook_id() + "_result.xml", fos);
            	    		fos.close();
            	    		
            	    		/* ��縮��Ʈ �ٿ�ε� ���� ! */
            	    		if(downloadResult){
            	    			Log.v(TAG, " xmlFileTransfer ��縮��Ʈ �ٿ�ε� ���� randomnumber=" + random );
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
			/* ftp ���� */
			mFTP.disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}