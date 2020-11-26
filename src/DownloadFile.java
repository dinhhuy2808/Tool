import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class DownloadFile {
	public static void main2(String[] args) throws IOException {
		// Create a new trust manager that trust all certificates
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };
		// Activate the new trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
		}
		URL website = new URL(
				"https://f.vdrive.vn/lib/893cb265-1f38-4230-aa9c-7b38ed3220a6/file/Format-BaiThi.xlsx?dl=1");
//		URL url = new URL("https://hostname:port/file.txt");
		URLConnection connection = website.openConnection();
		InputStream is = connection.getInputStream();

		Workbook workbook = new XSSFWorkbook(is);
		Sheet firstSheet = workbook.getSheetAt(0);
		Iterator<Row> iterator = firstSheet.iterator();

		while (iterator.hasNext()) {
			Row nextRow = iterator.next();
			Iterator<Cell> cellIterator = nextRow.cellIterator();

			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				System.out.print(cell.getStringCellValue());
				System.out.print(" - ");
			}
			System.out.println();
		}

		workbook.close();
		is.close();
//	    Files.copy(in, Paths.get("test-download.xlsx"), StandardCopyOption.REPLACE_EXISTING);
	}

	public static void main(String[] args) throws IOException {
		/*
		 * String url = "https://f.vdrive.vn/f/d9f2c8256d7345a2b039/?dl=1";
		 * 
		 * try { downloadUsingNIO(url, "sitemap.xlsx");
		 * 
		 * downloadUsingStream(url, "sitemap_stream.xlsx"); } catch (IOException e) {
		 * e.printStackTrace(); }
		 */
		String text = new String(
				Files.readAllBytes(Paths.get("C:/TaxProject/PinyinChar.html")));
		Document doc = Jsoup.parse(text);
		for(Element element: doc.getElementsByTag("td")) {
			String innerTest = element.text().trim();
			if(!innerTest.isEmpty()) {
				downloadAudioFile(innerTest);
			}
		}
	}

	private static void downloadUsingStream(String urlStr, String file) throws IOException {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };
		// Activate the new trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
		}
		URL url = new URL(urlStr);
		BufferedInputStream bis = new BufferedInputStream(url.openStream());
		FileOutputStream fis = new FileOutputStream(file);
		byte[] buffer = new byte[1024];
		int count = 0;
		while ((count = bis.read(buffer, 0, 1024)) != -1) {
			fis.write(buffer, 0, count);
		}
		fis.close();
		bis.close();
	}

	private static void downloadUsingNIO(String urlStr, String file) throws IOException {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };
		// Activate the new trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
		}
		URL url = new URL(urlStr);
		ReadableByteChannel rbc = Channels.newChannel(url.openStream());
		FileOutputStream fos = new FileOutputStream(file);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();
		rbc.close();
	}

	private static void downloadAudioFile(String audioName) {
		for (int i=1;i<=4;i++) {
			try (BufferedInputStream inputStream = new BufferedInputStream(
					new URL(String.format("https://yabla.vo.llnwd.net/media.yabla.com/chinese_static/audio/alicia/%s%d.mp3",
							audioName, i)).openStream());
					FileOutputStream fileOS = new FileOutputStream(String.format("C:\\TaxProject\\upload\\public\\audios\\PinyinChart\\%s%d.mp3", audioName, i))) {
				byte data[] = new byte[1024];
				int byteContent;
				while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
					fileOS.write(data, 0, byteContent);
				}
			} catch (IOException e) {
				// handles IO exceptions
			}
		}
		
	}
}
