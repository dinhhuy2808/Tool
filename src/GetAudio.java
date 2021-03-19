import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GetAudio {
	public static void main(String[] args) throws IOException {reName();}
	
	public static List<String> getAllFilesNameInFolder(String path) {
		File[] files = new File(path).listFiles();
		return Arrays.asList(files).stream().map(file -> file.getName()).collect(Collectors.toList());
	}
	
	public static void reName() throws UnsupportedEncodingException, IOException {
		boolean strictMode = true;
		String textChinese = new String(Files.readAllBytes(Paths.get("D:\\Tax\\eLearning\\src\\shared\\components\\pinyin-chart\\chinese-pinyin-chart.html")),
				"UTF-8");
		String unescapedStringChinese = org.jsoup.parser.Parser.unescapeEntities(textChinese, strictMode);
		Document docChinese = Jsoup.parse(unescapedStringChinese, "UTF-8");
		Elements elementsChinese = docChinese.getElementsByClass("click");
		Map<String, List<String>> madarinMap = new HashMap<String, List<String>>();
		for(Element element : elementsChinese) {
			String[] s = element.text().split(" ");
			madarinMap.put(s[0], Arrays.asList(s[1], s[2], s[3], s[4]));
		}
		
		System.out.println(madarinMap);
		String filePath =  "C:\\TaxProject\\upload\\public\\audios\\PinyinChart";
		List<File> files = Arrays.asList(new File(filePath).listFiles());
		for(File file : files) {
			String fileName = file.getName().split("\\.")[0];
			String validFileName = (fileName.endsWith("1") ||fileName.endsWith("2") ||
					fileName.endsWith("3") ||fileName.endsWith("4") )?fileName:"";
			if (!validFileName.isEmpty()) {
				int index = Integer.parseInt(validFileName.substring(validFileName.length()-1));
				String key = validFileName.substring(0, validFileName.length()-1);
				try {
					System.out.println(index + " " + key + " " + madarinMap.get(key).get(index-1));
					if (!file.renameTo(new File(filePath + "\\"+madarinMap.get(key).get(index-1)+".mp3"))) {
						throw new NullPointerException();
					}
				} catch (NullPointerException e) {
					System.out.println("--------------" + validFileName);
				}
				
			}
		}
	}
}
