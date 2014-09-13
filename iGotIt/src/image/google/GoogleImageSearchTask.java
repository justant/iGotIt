package image.google;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class GoogleImageSearchTask extends AsyncTask<Void, Void, ArrayList<GoogleImage>> {
    private static final String TAG = GoogleImageSearchTask.class.getName();
    private static final String RESULT_KEY = "result";
    private ApiHttpUrlConnection mApi;

    private Handler mHandler;

    public GoogleImageSearchTask(Handler handler, ApiHttpUrlConnection connection) {
        this.mHandler = handler;
        this.mApi = connection;
    }

    @Override
    protected ArrayList<GoogleImage> doInBackground(Void... params) {
        ArrayList<GoogleImage> result = new ArrayList<GoogleImage>();
        Log.d(TAG, "doInBackground");
        if (!isCancelled()) {
            if (mApi != null) {
                HttpURLConnection connection = mApi.getHttpURLConnection();
                try {
                    connection.connect();
                    int statusCode = connection.getResponseCode();
                    if (statusCode != HttpURLConnection.HTTP_OK) {
                        return result;
                    }
                    result = new JsonGoogleImageParser().parseGoogleImages(connection.getInputStream());
                } catch (Exception e) {
                	// ??¹ºÁö ¸ð¸£°Ú³× ÀÌ³ð
                	SearchState.getInstance().setHasError(true);
                    return result;
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<GoogleImage> images) {
        Log.d(TAG, "onPostExecute");

        if (mHandler != null) {
            Message message = Message.obtain();
            Bundle data = new Bundle();
            data.putParcelableArrayList(RESULT_KEY, images);
            message.setData(data);
            mHandler.sendMessage(message);
        }
    }
}