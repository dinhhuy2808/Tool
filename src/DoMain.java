import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class DoMain {
	public static void getHtml() {

		String testDetail = "http://www.hskonline.com/vi/1/4/test-start?lid=17#";
		String course = "http://www.hskonline.com/vi/{hskCount}/4";
		List<String> ImagesUrl = new ArrayList<>();
		org.jsoup.nodes.Document doc = null;
		String count[] = {"1","2","3","4","5","6"};
		Map<String, String> headers = new HashMap<String, String>();
		 headers.put("Accept"," text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
		 headers.put("Accept-Encoding"," gzip, deflate");
		 headers.put("Accept-Language"," vi,en-US;q=0.9,en;q=0.8");
		 headers.put("Cache-Control"," max-age=0");
		 headers.put("Connection"," keep-alive");
		 headers.put("Cookie"," _ga=GA1.2.123349348.1582639186; _gid=GA1.2.2020559366.1582639186; _ym_uid=1582639188995348126; _ym_d=1582639188; _ym_isad=2; _ym_visorc_53994931=w; advanced-frontend=aoml51hem3nia4ojrmu6mk7kp4; _identity-frontend=eb79959a2898f7a53636ea15106033440aca694760f367226adcab93aaf2b1d4a%3A2%3A%7Bi%3A0%3Bs%3A18%3A%22_identity-frontend%22%3Bi%3A1%3Bs%3A51%3A%22%5B635483%2C%22IAu0t8Qw6mcF-_CSwGjKrJ7iCrszUCo1%22%2C2592000%5D%22%3B%7D; _csrf-frontend=74a579e8036e2a655edd7192ede206a4dae83ad26ae3f323326b56b6a55fb30aa%3A2%3A%7Bi%3A0%3Bs%3A14%3A%22_csrf-frontend%22%3Bi%3A1%3Bs%3A32%3A%22BpVRjyON4AbJje6INEC6itwHPd8A0S5C%22%3B%7D");
		 headers.put("Host"," www.hskonline.com");
		 headers.put("Upgrade-Insecure-Requests"," 1");
		 headers.put("User-Agent"," Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36");
		try {
			FileOutputStream outputStream;
			for(String i : count) {
				String url = course.replace("{hskCount}", i);
				doc = Jsoup.connect(url).headers(headers).get();
				Elements elements = doc.getElementsByClass("paper t1");
				int j = 0;
				for(Element e : elements) {
					String urlTest = "http://www.hskonline.com"+e.attr("href").replace("test?id", "test-start?lid");
					doc = Jsoup.connect(urlTest).headers(headers).get();
					try {
						outputStream = new FileOutputStream("test-"+i+"-"+j+".txt");
						 byte[] strToBytes = doc.toString().getBytes();
						    outputStream.write(strToBytes);
						 
						    outputStream.close();
							System.out.println(doc.data());
					} catch (IOException e2) {
						e2.printStackTrace();
					}
					j++;
//					vi/1/4/test-start?lid=17
//					/vi/1/4/test?id=17		
				}
			}
			doc = Jsoup.connect("http://www.hskonline.com/vi/1/4/test-start?lid=17#").headers(headers).get();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
		}
//		Elements masthead = doc.getElementsByClass("audio circle-audio");
//		Elements masthead = doc.select("div[data-media]");
//		for(Element e : masthead) {
//			System.out.println(e.attr("data-media"));
//		}
		
	   
	
	}
	
	public static void parseHtml() throws IOException {
//		những thẻ nằm trong field-body sẽ là phần trả lời. field-heading thuộc phần để bài, field-footer chứa answer (field-answer) và tỉ lệ(field-counter)
		String classes[] = {"field-image","field-audio","field-items","field-children"};
		FileOutputStream outputStream;
		for (int i = 1 ; i <= 6 ; i++) {
			for (int j = 0 ; j <= 9 ; j++) {
				 String text = new String(Files.readAllBytes(Paths.get("Tests/test-"+i+"-"+j+".txt")), StandardCharsets.UTF_8);
				 Document doc = Jsoup.parse(text);
				 Elements elements = doc.getElementsByClass("item");
				 for (Element e : elements) {
					 Elements audio = e.select("div[data-media]");
					 String audioHtml="<audio controls>\r\n" + 
					 		"	  <source src=\"{audioSource}\" type=\"audio/mpeg\">\r\n" + 
					 		"	Your browser does not support the audio element.\r\n" + 
					 		"	</audio></br><p>-----------------------------------------------------</p>";
					 if (audio.size() > 1) {
						 for (Element a : audio) {
							 String audioTemp = audioHtml;
							 audioTemp = audioTemp.replace("{audioSource}", a.attr("data-media").split("\":\"")[1].replace("\"", "").replace("{", "").replace("}", ""));
							 Element newAudio = new Element(audioTemp);
							 a.parent().append(audioTemp);
							 a.remove();
						}
					 } else if (audio.size()==1) {
						 String audioTemp = audioHtml;
						 audioTemp = audioTemp.replace("{audioSource}", audio.get(0).attr("data-media").split("\":\"")[1].replace("\"", "").replace("{", "").replace("}", ""));
						 audio.get(0).parent().append(audioTemp);
						 audio.get(0).remove();
					 }
					 
				 }
				 try {
						outputStream = new FileOutputStream("testAfterParse-"+i+"-"+j+".html");
						 byte[] strToBytes = doc.getElementsByClass("item").toString().getBytes();
						    outputStream.write(strToBytes);
						 
						    outputStream.close();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
			}
		}

	}
	
	public static void parseHtmlToElearningProject() throws IOException {
		FileOutputStream outputStream;
		for (int i = 1 ; i <= 6 ; i++) {
			for (int j = 0 ; j <= 9 ; j++) {
				String text = new String(Files.readAllBytes(Paths.get("testAfterParse-"+i+"-"+j+".html")), StandardCharsets.UTF_8);
				Document doc = Jsoup.parse(text);
				Elements elements = doc.getElementsByClass("item");
				for (Element e : elements) {
					
//					Element header = e.getElementsByClass("field-heading").get(0);
					Elements bodies = e.getElementsByClass("field-body");
					if (bodies.size() == 1) {
						Element body = bodies.get(0);
						Elements headers = body.getElementsByClass("field-heading");
							if (headers.size()>0 && headers.get(0).getElementsByClass("field-option").size()>0) {
								for (Element header:headers) {
									Elements optionsElement = header.getElementsByClass("field-option");
									Element newBody = new Element("div").addClass("field-body");
									newBody.html(StringUtil.join(optionsElement, ""));
									header.getElementsByClass("field-items").remove();
									newBody.appendTo(header.parent());
								}
							} else {
								Elements optionsElement = body.getElementsByClass("field-option");
								body.html(StringUtil.join(optionsElement, ""));
							}
						
						
						
						try {
							e.getElementsByClass("field-counter").get(0).remove();
							} catch (IndexOutOfBoundsException e2) {
								// TODO: handle exception
								System.out.println(i +"-"+j+"--"+e.html());
							}
					} else {
						bodies.remove(0);
						for (Element body : bodies) {
							Elements optionsElement = body.getElementsByClass("field-option");
							body.html(StringUtil.join(optionsElement, ""));
							try {
								e.getElementsByClass("field-counter").get(0).remove();
								} catch (IndexOutOfBoundsException e2) {
									// TODO: handle exception
									System.out.println(i +"-"+j+"--"+e.html());
								}
						}
					}
					e.children().get(0).children().stream().forEach(element -> {
						if (!element.className().contains("field-heading")
								&& !element.className().contains("field-body")
								&& !element.className().contains("field-footer")) {
							element.remove();
						}
					});
				}
				try {
					outputStream = new FileOutputStream("ElearningProject/test-"+i+"-"+j+".html");
					 byte[] strToBytes = doc.getElementsByClass("item").toString().getBytes();
					    outputStream.write(strToBytes);
					 
					    outputStream.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) {
		try {
			parseHtmlToElearningProject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
