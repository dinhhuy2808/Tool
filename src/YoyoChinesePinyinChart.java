import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

public class YoyoChinesePinyinChart {

	public static void main(String[] args) throws IOException {
		String text = new String(Files.readAllBytes(Paths.get("Pinyin//Yoyo-Chinese-Pinyin-Chart.htm")),
				"ISO-8859-1");
		System.out.println(text);
		Document doc = Jsoup.parse(text, "ISO-8859-1");
		Elements tdElements = doc.getElementsByTag("td");
		String audioTemp = "<span  class=\"tooltiptext\"><div> <audio id=\"player\" src=\"%s\"/> <div> <a onclick=\"document.getElementById('player').play()\"> <i class='fa fa-volume-up'/>%s</a> </div> </div> <div> <audio id=\"player\" src=\"%s\"/> <div> <a onclick=\"document.getElementById('player').play()\"> <i class='fa fa-volume-up'/>%s</a> </div> </div> <div> <audio id=\"player\" src=\"%s\"/> <div> <a onclick=\"document.getElementById('player').play()\"> <i class='fa fa-volume-up'/>%s</a> </div> </div> <div> <audio id=\"player\" src=\"%s\"/> <div> <a onclick=\"document.getElementById('player').play()\"> <i class='fa fa-volume-up'/>%s</a> </div> </div></span>";
		String audioTemp2 = "<span id=\"example\" class=\"btn example d-flex justify-content-center\" style=\"width: inherit;height: inherit;\" rel=\"popover\"\r\n" + 
				"		data-html=\"true\" \r\n" + 
				"		data-content='"+
				"<div><i style=\"cursor: pointer\" id=\"%s\" class=\"fa fa-volume-up\"> %s</i></br>" + 
				"<i style=\"cursor: pointer\" id=\"%s\" class=\"fa fa-volume-up\"> %s</i></br>" + 
				"<i style=\"cursor: pointer\" id=\"%s\" class=\"fa fa-volume-up\"> %s</i></br>" + 
				"<i style=\"cursor: pointer\" id=\"%s\" class=\"fa fa-volume-up\"> %s</i></div>'" +
				">" +
				"%s</span>";
		Map<String, String[]> map = new HashMap<String, String[]>();
		String[] a = {"","ā","á","ǎ", "à"};
		String[] o = {"","ō","ó","ǒ","ò"};
		String[] e = {"","ē","é","ě","è"};
		String[] i = {"","ī","í","ǐ","ì"};
		String[] u = {"","ū","ú","ǔ","ù"};
		String[] u2 = {"","ǖ","ǘ","ǚ","ǜ"};

		map.put("a", a);
		map.put("o", o);
		map.put("e", e);
		map.put("i", i);
		map.put("u", u);
		map.put("ü", u2);
		String audioUrlTemp = "http://103.92.29.71:8081/audios/Pinyin/%s.mp3";
		for(Element td : tdElements) {
			
			if (td.attr("width").equals("68") || td.attr("style").contains("background:#E5E5E5")) {
				td.removeAttr("style");
				td.addClass("head");
			} else if (td.attr("width").equals("52")) {
				String textInner = td.text();
				td.removeAttr("style");
				td.addClass("cell");
				td.getElementsByTag("p").get(0).remove();
				
				if (textInner.matches("[a-zA-Z]+")) {
					td.attr("id", textInner);
//					System.out.println(textInner);
//					td.attr("onClick", String.format("openTootip('%s', this)", textInner));
					String audioUrl = String.format(audioUrlTemp, textInner.trim());
					String audiohtml = String.format(audioTemp, String.format(audioUrlTemp, textInner.trim())+"1", " " + textInner +"1 "
							, String.format(audioUrlTemp, textInner.trim())+"2", " " + textInner +"2 "
							, String.format(audioUrlTemp, textInner.trim())+"3", " " + textInner +"3 "
							, String.format(audioUrlTemp, textInner.trim())+"4", " " + textInner +"4 ");
					String charR = getCharToReplace(textInner);
					String audiohtml2 = String.format(audioTemp2,
							textInner + "1",textInner.replace(charR, map.get(charR)[1]),
							textInner + "2",textInner.replace(charR, map.get(charR)[2]),
							textInner + "3",textInner.replace(charR, map.get(charR)[3]),
							textInner + "4",textInner.replace(charR, map.get(charR)[4]),
							textInner);
					td.html(td.html()+new String(audiohtml2.getBytes(),"ISO-8859-1"));
				}
				
				
			}
		}
		Element firstTableTr = doc.selectFirst("div.WordSection1 > table > tbody > tr");
		firstTableTr.attr("style", firstTableTr.attr("style")+";background: #00bfd8;");
		Element firstTableTrTd = firstTableTr.getElementsByTag("td").get(0);
		firstTableTrTd.attr("style", firstTableTrTd.attr("style")+";background: #FFF4DF;");
		
		Element firstTableTr2 = doc.selectFirst("div.WordSection2 > table > tbody > tr");
		firstTableTr2.attr("style", firstTableTr2.attr("style")+";background: #00bfd8;");
		Element firstTableTrTd2 = firstTableTr2.getElementsByTag("td").get(0);
		firstTableTrTd2.attr("style", firstTableTrTd2.attr("style")+";background: #FFF4DF;");
		
		Element firstTableTr3 = doc.selectFirst("div.WordSection3 > table > tbody > tr");
		firstTableTr3.attr("style", firstTableTr3.attr("style")+";background: #00bfd8;");
		Element firstTableTrTd3 = firstTableTr3.getElementsByTag("td").get(0);
		firstTableTrTd3.attr("style", firstTableTrTd3.attr("style")+";background: #FFF4DF;");
		try {
			Writer out = new BufferedWriter(new OutputStreamWriter(
				    new FileOutputStream("Pinyin//Yoyo-Chinese-Pinyin-Chart - Copy.htm"), "ISO-8859-1"));
			  out.write(doc.html());

			  out.close();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}
	
//	public static void main(String[] args) throws UnsupportedEncodingException, IOException {
//		String text = new String(Files.readAllBytes(Paths.get("C:\\TaxProject\\Bang-Pinyin-Co-Ban.htm")), "ISO-8859-1");
//		System.out.println(text);
//		Document doc = Jsoup.parse(text, "ISO-8859-1");
//		Elements tdElements = doc.getElementsByTag("td");
//		for (Element td : tdElements) {
//			td.attr("class", "cell-basic");
//		}
//		try {
//			Writer out = new BufferedWriter(new OutputStreamWriter(
//					new FileOutputStream("C:\\TaxProject\\Bang-Pinyin-Co-Ban.htm"), "ISO-8859-1"));
//			out.write(doc.html());
//
//			out.close();
//		} catch (IOException e2) {
//			e2.printStackTrace();
//		}
//	}
	
	
	public static void main2(String[] args) throws UnsupportedEncodingException, IOException {
		String text = new String(Files.readAllBytes(Paths.get(
				"C:\\TaxProject\\TaxELearning-Face\\src\\shared\\components\\pinyin-chart\\pinyin-chart.component.html")),
				"ISO-8859-1");
		Map<String, String[]> map = new HashMap<String, String[]>();
		String[] a = {"","ā","á","ǎ", "à"};
		String[] o = {"","ō","ó","ǒ","ò"};
		String[] e = {"","ē","é","ě","è"};
		String[] i = {"","ī","í","ǐ","ì"};
		String[] u = {"","ū","ú","ǔ","ù"};
		String[] u2 = {"","ǖ","ǘ","ǚ","ǜ"};

		map.put("a", a);
		map.put("o", o);
		map.put("e", e);
		map.put("i", i);
		map.put("u", u);
		map.put("ü", u2);
		System.out.println(text);
		Document doc = Jsoup.parse(text, "ISO-8859-1");
		Elements tdElements = doc.getElementsByClass("cell");
		for (Element td : tdElements) {
			Elements iElements = td.getElementsByTag("i");
			int countI = 1;
			for (Element iEle : iElements) {
				String textInner = td.attr("id").trim();
				iEle.attr("id",textInner+(countI));
				String charR = getCharToReplace(textInner);
				iEle.text(textInner.replace(charR,map.get(charR)[countI++]));
			}
		}
		try {
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
					"C:\\TaxProject\\TaxELearning-Face\\src\\shared\\components\\pinyin-chart\\pinyin-chart.component-copy.html"),
					"ISO-8859-1"));
			out.write(doc.html());

			out.close();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}
	
	private static String getCharToReplace(String word) {
		if (word.contains("a")) {
			return "a";
		}
		if (word.contains("e")) {
			return "e";
		}
		if (word.contains("o")) {
			return "o";
		}
		if (word.contains("i")) {
			return "i";
		}
		if (word.contains("u")) {
			return "u";
		}
		if (word.contains("ü")) {
			return "ü";
		}
		return "";
	}
}
