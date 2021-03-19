import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class YoyoChinesePinyinChart {
	public static void main(String[] args) throws IOException {
		String text = new String(Files.readAllBytes(Paths.get("D:\\Tax\\eLearning\\src\\shared\\components\\pinyin-chart\\pinyin-chart.component-copy.html")),
				"UTF-8");
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
//		System.out.println(madarinMap);
		
		for (String key : madarinMap.keySet()) {
			List<String> value = madarinMap.get(key);
			text = text.replace(key.trim()+"1", value.get(0))
			.replace(key.trim()+"2", value.get(1))
			.replace(key.trim()+"3", value.get(2))
			.replace(key.trim()+"4", value.get(3));
		}
		
		String unescapedString = org.jsoup.parser.Parser.unescapeEntities(text, strictMode);
		Document doc = Jsoup.parse(unescapedString, "UTF-8");
		Elements elements = doc.getElementsByClass("example");
		System.out.println(text);
		/*try {
			Writer out = new BufferedWriter(new OutputStreamWriter(
				    new FileOutputStream("D:\\Tax\\eLearning\\src\\shared\\components\\pinyin-chart\\pinyin-chart.component-copy.html"), "UTF-8"));
			  out.write(doc.html());

			  out.close();
		} catch (IOException e2) {
			e2.printStackTrace();
		}*/
	}
}
