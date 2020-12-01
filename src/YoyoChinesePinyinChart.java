import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
		String text = new String(Files.readAllBytes(Paths.get("D://Tax//Yoyo-Chinese-Pinyin-Chart.htm")),
				"ISO-8859-1");
		System.out.println(text);
		Document doc = Jsoup.parse(text, "ISO-8859-1");
		Elements tdElements = doc.getElementsByTag("td");
		String audioTemp = "<span class=\"tooltiptext\"><div> <audio id=\"player\" src=\"%s\"/> <div> <a onclick=\"document.getElementById('player').play()\"> <i class='fa fa-volume-up'/>%s</a> </div> </div> <div> <audio id=\"player\" src=\"%s\"/> <div> <a onclick=\"document.getElementById('player').play()\"> <i class='fa fa-volume-up'/>%s</a> </div> </div> <div> <audio id=\"player\" src=\"%s\"/> <div> <a onclick=\"document.getElementById('player').play()\"> <i class='fa fa-volume-up'/>%s</a> </div> </div> <div> <audio id=\"player\" src=\"%s\"/> <div> <a onclick=\"document.getElementById('player').play()\"> <i class='fa fa-volume-up'/>%s</a> </div> </div></span>";
		String audioUrlTemp = "http://103.92.29.71:8081/audios/Pinyin/%s.mp3";
		for(Element td : tdElements) {
			
			if (td.attr("width").equals("68")) {
				td.removeAttr("style");
				td.addClass("head");
			} else if (td.attr("width").equals("52")) {
				td.removeAttr("style");
				td.addClass("cell");
				String textInner = td.text();
				if (textInner.matches("[a-zA-Z]+")) {
					td.attr("id", textInner);
//					System.out.println(textInner);
					td.attr("onClick", String.format("openTootip('%s', this)", textInner));
					String audioUrl = String.format(audioUrlTemp, textInner.trim());
					String audiohtml = String.format(audioTemp, String.format(audioUrlTemp, textInner.trim())+"1", " " + textInner +"1 "
							, String.format(audioUrlTemp, textInner.trim())+"2", " " + textInner +"2 "
							, String.format(audioUrlTemp, textInner.trim())+"3", " " + textInner +"3 "
							, String.format(audioUrlTemp, textInner.trim())+"4", " " + textInner +"4 ");
					td.html(td.html()+audiohtml);
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
				    new FileOutputStream("D://Tax//Yoyo-Chinese-Pinyin-Chart - Copy.htm"), "ISO-8859-1"));
			  out.write(doc.html());

			  out.close();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}
}
