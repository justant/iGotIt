package fragments.newspaper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import singleton.Utility;
import android.content.Context;
import android.util.Log;

public class xmlParseArticle {
	static final String TAG = "xmlParseArticle";
	private Utility utility = Utility.getInstance();
	
	private File xmlFile;
	private String fullPath;
	private Context context;
	
	xmlParseArticle(Context context){
		this.context = context;
	}
	
	/* article.xml�� �Ľ��ؼ� Article ��ü�� Return ���ش� */
	public ArticleObject getArticle(){
		ArticleObject article = new ArticleObject();
		
		try {
			fullPath = context.getFilesDir().getAbsolutePath() + "/xmls";
			xmlFile = new File(fullPath, "article.xml");
			InputStream is = new FileInputStream(xmlFile);

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser parser = factory.newPullParser();

			parser.setInput(is, "UTF-8");

			int eventType = parser.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					try{
						if(parser.getName().equals("title")) {
							article.setTitle(parser.getAttributeValue(0));
						}
						if(parser.getName().equals("newsdate")) {
							article.setNewsdate(parser.getAttributeValue(0));
						}
						if(parser.getName().equals("content")) {
							article.setContent(parser.getAttributeValue(0));
						}
						}catch(Exception e){
							/* ���� ���� ���� */
							Log.v(TAG, "article content Exception e1 = " + e);
							return null;
						}
//					Log.v(TAG, "article title = " + article.getTitle());
//					Log.v(TAG, "article newsdate = " + article.getNewsdate());
//					Log.v(TAG, "article content = " + article.getContent());
				}
					try{
						eventType = parser.next();
					}catch(Exception e){
						Log.v(TAG, "article content Exception e2 = " + e);
						return null;
					}
			}
			
			/* ��� �ٿ� �� ArticleObject ��ȯ ���� ! */
			if(article.getTitle() == null){
				Log.v(TAG, "xmlParseArticle ��� �ٿ��� ArticleObject ��ȯ ����! setArticleDown(false) ");
				utility.setArticleDown(false);
			}else{
				Log.v(TAG, "xmlParseArticle ��� �ٿ��� ArticleObject ��ȯ ����! setArticleDown(true) ");
				utility.setArticleDown(true);
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
		
		return article;
	}
}
