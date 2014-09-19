package fragments.newspaper;


import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

import singleton.Utility;
import android.content.Context;
import android.util.Log;

public class xmlGenerator{
	private static final String TAG = "xmlGenerator";
	private Utility utility = Utility.getInstance();
	
	private Context context; 
	private ArrayList<String> wordArray;
	
	public xmlGenerator(Context context, ArrayList<String> wordArray){
		this.context = context;
		this.wordArray = wordArray;
		Log.v(TAG, "ExcelConvert Start!");
	}
	
	public void start(){
		DOMSource source = null;
		try{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = docFactory.newDocumentBuilder();
		
			/* root elements (data) */
			Document document = (Document) documentBuilder.newDocument();
			Element rootElement = document.createElement("data");
			document.appendChild(rootElement);

			/* words elements (data의 자식) */
			Element words = document.createElement("words");
			rootElement.appendChild(words);
		
			/* word elements ( words의 자식) */
			
			for(int i=0; i<wordArray.size(); i++){
				Element word = document.createElement("word");
				word.setAttribute("value", wordArray.get(i));
				words.appendChild(word);
			}
		
			/* xml 파일로 만들기 */
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			source = new DOMSource(document);
			
			String fullPath = context.getFilesDir().getAbsolutePath() + "/xmls";
			File dir = new File(fullPath);
			
			/* 디렉토리가 없다면 새로 생성 */
			if(!dir.exists()){
				Log.v(TAG," xmlGenerator : There is no file");
				dir.mkdirs();
			}
			
			File file = new File(fullPath, utility.getFacebook_id() + ".xml");
			
			/* 동일한 이름의 파일이 있다면 삭제 */
			if(file.exists()){
				Log.v(TAG," xmlGenerator : File delete!");
				file.delete();
			}
			
			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);
			
		}catch (Exception  e){
			e.printStackTrace();
		}
	}	
}
