package fragments.newspaper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import singleton.Utility;
import android.content.Context;
import android.util.Log;

/* 	xmlFileTransfer�� ���ؼ� result.xml�� �޾ƿԴ�. �� ���Ͽ� ����ִ� �Ź���������� ������ �Ľ��ؼ�
	ArrayList�� ����� ������ �ش� .																*/

public class xmlParseArticleList {
	static final String TAG = "xmlParseArticleList";
	
	private Utility utility = Utility.getInstance();
	private File xmlFile;
	private String fullPath;
	
	private Context context;
	xmlParseArticleList(Context context){
		this.context = context;
	}
	
	/*  result.xml �Ľ��ؼ� FTP������ �ִ� ������  Return ���ش� */
	public ArrayList<String> getArticleList(){
		/* ����� *.xml �̸��� �޾��� ArrayList */
		ArrayList<String> articleList = new ArrayList<String>();
		articleList.clear();
		
		try {
			fullPath = context.getFilesDir().getAbsolutePath() + "/xmls";
			xmlFile = new File(fullPath, utility.getFacebook_id() + "_result.xml");
			InputStream is = new FileInputStream(xmlFile);

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser parser = factory.newPullParser();

			parser.setInput(is, "UTF-8");

			int eventType = parser.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					if (parser.getName().equals("article")) {
						articleList.add(parser.getAttributeValue(0));
						//Log.v(TAG, "xmlParseArticleList = " +parser.getAttributeValue(0));
					}
				}
				eventType = parser.next();
			}
			
			/* ��縮��Ʈ �ٿ� �� Arraylist ��ȯ ���� ! */
			if(articleList.isEmpty()){
				Log.v(TAG, "xmlParseArticleList ��縮��Ʈ �ٿ��� ArrayList��ȯ ����! setArticleListDown(false) ");
				utility.setArticleListDown(false);
			}else{
				Log.v(TAG, "xmlParseArticleList ��縮��Ʈ �ٿ��� ArrayList��ȯ ����! setArticleListDown(true) ");
				utility.setArticleListDown(true);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return articleList;
	}
	
	
	
	/*  result.xml �Ľ��ؼ� FTP������ �ִ� ���鿡 ���Ե� �ܾ��  Return ���ش� */
	public ArrayList<String> getWordCountInfo(){
		/* ����� *.xml �� ���Ե� �ܾ�� ������ ArrayList */
		ArrayList<String> wordCountInfo = new ArrayList<String>();
		wordCountInfo.clear();
		
		try {
			fullPath = context.getFilesDir().getAbsolutePath() + "/xmls";
			xmlFile = new File(fullPath, utility.getFacebook_id() + "_result.xml");
			InputStream is = new FileInputStream(xmlFile);

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser parser = factory.newPullParser();

			parser.setInput(is, "UTF-8");

			int eventType = parser.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					if (parser.getName().equals("article")) {
						/* � �ܾ���� ���ԵǾ����� �˻� */
						wordCountInfo.add(parser.getAttributeValue(2));
						Log.v(TAG, "xmlParseArticleList = " +parser.getAttributeValue(1));
					}
				}
				eventType = parser.next();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return wordCountInfo;
	}
}
