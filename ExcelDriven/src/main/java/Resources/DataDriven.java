package Resources;

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

public class DataDriven {
	
	//Identify TestCases coloum by scanning the entire 1st row
	//once coloumn id identified then scan entire testcase coloum to identify purchase testcase row
	//After you grab purchase testcase row  = pull all the data of that row and feed into test

	public ArrayList<String> getData(String testCaseName) throws IOException {
		
		ArrayList<String> array = new ArrayList<String>();
		
		FileInputStream files = new FileInputStream("C:\\Users\\draghava\\Downloads\\HasMap.xlsx");
		XSSFWorkbook workBook = new XSSFWorkbook(files);
		
		int sheets = workBook.getNumberOfSheets();
		for (int i=0; i<sheets; i++) {
			if (workBook.getSheetName(i).equalsIgnoreCase("TestData1")) {
				XSSFSheet sheet = workBook.getSheetAt(i);
				System.out.println(sheets);
				System.out.println(i);
				Iterator<Row> rows=sheet.iterator();
				Row firstrow=rows.next();
				Iterator<Cell> cell=firstrow.cellIterator();
				int k=0;
				int coloumn=0;
				while(cell.hasNext()) {
					Cell value=cell.next();
					if(value.getStringCellValue().equalsIgnoreCase("TestCase")) {
						coloumn=k;
					}
					k++;
				}
				System.out.println(coloumn);
				
				//Once coloumn is identified then scan entire testcase coloum to identify purchase testcase row
				
				while (rows.hasNext()) {
					Row row = rows.next();
					if(row.getCell(coloumn).getStringCellValue().equalsIgnoreCase(testCaseName)) {
						
						// After you  grab the purchase teatcase row = pull all the data of that row and feed into test
						
						Iterator<Cell> cv = row.cellIterator();	
						while (cv.hasNext()) {
							Cell cells = cv.next();
							if (cells.getCellType()==CellType.STRING) {
								array.add(cells.getStringCellValue());
							}
							else {
								array.add(NumberToTextConverter.toText(cells.getNumericCellValue()));
							}
						}
					}
				}
			}
		}
		return array;
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		//fileInputStream argument
		

	}
}