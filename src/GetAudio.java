import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GetAudio {
	public static void main(String[] args) throws IOException {
		// Log.d(LOG_TAG, "url.openStream()");

		Workbook  workbook = null;
		Row row = null;
		try {
			InputStream inputExcelStream = new FileInputStream(new File(
					"Tu-Dien-Website.xlsx"));

			workbook = new XSSFWorkbook(inputExcelStream);
			List<String> files = getAllFilesNameInFolder("C:\\GURKHA_DEV\\eLearning\\audio\\audio");
			Sheet sheet = workbook.getSheetAt(0);
			int lastRow = sheet.getLastRowNum();
			for (int i = /* totalWords+1 */1; i <= 10; i++) {
				row = sheet.getRow(i);
				String hantu = row.getCell(1) == null ? "" : row.getCell(1).getStringCellValue().replace("\'", "\\'");
				if (!files.contains(hantu.trim()+".mp3")) {
					URL url = new URL("https://vtudien.com/doc/trung/"+hantu+".mp3");
					InputStream inputStream = url.openStream();
					FileOutputStream fileOutputStream = new FileOutputStream("hantu.mp3");

					int c;

					while ((c = inputStream.read()) != -1) {
						fileOutputStream.write(c);
						c++;
					}
				}
			}
			// }
		} catch (IOException | NullPointerException e) {
			// TODO Auto-generated catch block
			System.out.println(row.getRowNum());
			System.out.println(e.getMessage());
		}

	}
	
	public static List<String> getAllFilesNameInFolder(String path) {
		File[] files = new File(path).listFiles();
		return Arrays.asList(files).stream().map(file -> file.getName()).collect(Collectors.toList());
	}
}
