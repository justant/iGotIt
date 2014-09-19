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

/* 	xmlFileTransfer를 통해서 result.xml을 받아왔다. 이 파일에 들어있는 신문기사파일의 정보를 파싱해서
	ArrayList를 만들고 리턴해 준다 .																*/

public class xmlParseArticleList {
	static final String TAG = "xmlParseArticleList";
	
	private Utility utility = Utility.getInstance();
	private File xmlFile;
	private String fullPath;
	
	private Context context;
	xmlParseArticleList(Context context){
		this.context = context;
	}
	
	/*  result.xml 파싱해서 FTP서버에 있는 기사들을  Return 해준다 */
	public ArrayList<String> getArticleList(){
		/* 기사의 *.xml 이름을 받아줄 ArrayList */
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
			
			/* 기사리스트 다운 후 Arraylist 변환 성공 ! */
			if(articleList.isEmpty()){
				Log.v(TAG, "xmlParseArticleList 기사리스트 다운후 ArrayList변환 실패! setArticleListDown(false) ");
				utility.setArticleListDown(false);
			}else{
				Log.v(TAG, "xmlParseArticleList 기사리스트 다운후 ArrayList변환 성공! setArticleListDown(true) ");
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
	
	
	
	/*  result.xml 파싱해서 FTP서버에 있는 기사들에 포함된 단어수  Return 해준다 */
	public ArrayList<String> getWordCountInfo(){
		/* 기사의 *.xml 에 포함된 단어수 정보의 ArrayList */
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
						/* 어떤 단어들이 포함되었는지 검색 */
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
