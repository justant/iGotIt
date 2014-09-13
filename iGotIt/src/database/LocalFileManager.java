package database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class LocalFileManager {
	private static final String TAG = "LocalFile";
	private Context context;

	public LocalFileManager(Context context){
		this.context = context;
	}
	
	public Bitmap loadImageFile(String fileName){
		Log.v(TAG, "Load image file : name = " + fileName);
		Bitmap bitmapImage = null;
		try {
			bitmapImage = BitmapFactory.decodeFile(context.getFilesDir().getAbsolutePath()+ "/images/"+ fileName +".png");
		} catch (Exception e){
			Log.v(TAG, "There is no image file : name = " + fileName + ".png");
		}
		
		return bitmapImage;
	}
	
	public void SaveImageFile(Bitmap image, String fileName) {
	    String filePath = context.getFilesDir().getAbsolutePath() + "/images";
		Log.v(TAG, "Save image file : name = " + fileName);
		
		try {
			File dir = new File(filePath);
			if (!dir.exists()) {
				dir.mkdirs();
				Log.v(TAG, "There is no directory. so, make directory : name = /images");
			}

			OutputStream fileOut = null;
			File file = new File(filePath, fileName + ".png");
			if (file.exists()) {
				Log.v(TAG, "There is same fileName image. so, delete file : name = " + fileName);
				file.delete();
			}

			Log.v(TAG, "Create new image file : name = " + fileName);
			
			file.createNewFile();
			fileOut = new FileOutputStream(file);
			
			boolean result = image.compress(Bitmap.CompressFormat.PNG, 100, fileOut);

			Log.v(TAG, "Save image file result : " + result);
			
			fileOut.flush();
			fileOut.close();
		} catch (Exception e) {
			Log.e("saveToExternalStorage()", e.getMessage());
		}
	}
}
