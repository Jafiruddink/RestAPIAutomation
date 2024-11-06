package utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

	public ArrayList<String> GetData(String sheetName, String testCaseName) throws IOException {
		FileInputStream fis = new FileInputStream("C:\\Users\\jafir\\eclipse-workspace\\RestAPIAutomation\\src\\test\\resources\\Testdata\\TestData.xlsx");
		
		XSSFWorkbook workBook = new XSSFWorkbook(fis);
		int sheets = workBook.getNumberOfSheets();
		
		ArrayList<String> listOfCellValue = new ArrayList<String>();
		
		for(int i=0; i<sheets; i++) {
			if(workBook.getSheetAt(i).getSheetName().equalsIgnoreCase(sheetName)) { //get the matched sheet
				XSSFSheet sheet = workBook.getSheetAt(i);
				Iterator<Row> rows = sheet.iterator(); //Sheet is collection of rows
				Row firstRow = rows.next();
				Iterator<Cell> cells = firstRow.cellIterator(); //row is collection of cells
				
				int k=0;
				int column =0;
				while(cells.hasNext()) {
					Cell value = cells.next();
					if(value.getStringCellValue().equalsIgnoreCase("TestCase")) {
						column = k;
					}
					k++;
				}
				
				while(rows.hasNext()) {
					Row r = rows.next();
					if(r.getCell(column).getStringCellValue().equalsIgnoreCase(testCaseName)) {
						Iterator<Cell> c = r.cellIterator();
						
						while(c.hasNext()) {
							Cell cv = c.next();
							if(cv.getCellType() == CellType.STRING)
								listOfCellValue.add(cv.getStringCellValue());
							else
								listOfCellValue.add(NumberToTextConverter.toText(cv.getNumericCellValue()));
						}
					}
				}
			}
		}
		return listOfCellValue;
	}
}
