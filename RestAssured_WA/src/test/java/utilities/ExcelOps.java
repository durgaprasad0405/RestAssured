package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import functionalLib.BusinessActions;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelOps extends Global {

	public static XSSFWorkbook rWorkbook;
	public static XSSFSheet rSheet;
	public static XSSFRow rRowHead;
	public static XSSFCell rCell;

	// @Test
	public void excelWrite1() throws IOException, RowsExceededException, WriteException {
		logger.info("Entered in to excelWrite method.");
		WritableSheet wsheet;
		String sampleFileName = CurrentDirectory.concat("/testartifacts/DTCFile2.xls");
		WritableWorkbook wworkbook;
		wworkbook = Workbook.createWorkbook(new File(sampleFileName));
		wsheet = wworkbook.createSheet("TestSheet", 1);
		Label label = new Label(1, 1, "Narasimha");
		wsheet.addCell(label);
		wworkbook.write();
		wworkbook.close();
		logger.info("Exit from excelWrite method.");
	}

	public void setCellValue(String excelFileName, String sheetName, int rowNumber, int cellNum, String inputVal)
			throws FileNotFoundException, IOException, RowsExceededException, WriteException {
		logger.info("Entered into excelWriteData");
		// String excelFileName;
		excelFileName = CurrentDirectory.concat("/testartifacts/TestData.OCIDev/" + excelFileName);
		File outPutFile;
		rWorkbook = excelFileRead(excelFileName);
		if (rWorkbook != null) {
			rSheet = rWorkbook.getSheet(sheetName);

			rSheet.getRow(rowNumber).getCell(cellNum).setCellValue(inputVal);
			outPutFile = new File(excelFileName);
			FileOutputStream outputStream = new FileOutputStream(outPutFile);
			rWorkbook.write(outputStream);
			outputStream.close();
			logger.info("Exit from excelWriteData.");
		} else {

			logger.info("Excel is not present");
		}
	}

	// @Test
	// @SuppressWarnings("resource")
	public void createRow(String excelFileName, String sheetName, int rowNum, String[] headers)
			throws FileNotFoundException, IOException {
		logger.info("Entered in to createRow method.");
		File outPutFile = null;
		File srcFile = new File(excelFileName);
		if (!srcFile.exists()) {
			rWorkbook = new XSSFWorkbook();
			logger.info("Creating an excelworkbook in CreateRow method.");

		} else {
			rWorkbook = new XSSFWorkbook(new FileInputStream(excelFileName));
		}

		if (rWorkbook.getSheet(sheetName) == null)
			rSheet = rWorkbook.createSheet(sheetName);
		else
			rSheet = rWorkbook.getSheet(sheetName);
		rRowHead = rSheet.createRow(rowNum);
		for (int i = 0; i < headers.length; i++) {
			rRowHead.createCell(i).setCellValue(headers[i]);
		}
		outPutFile = new File(excelFileName);
		FileOutputStream outputStream = new FileOutputStream(outPutFile);
		rWorkbook.write(outputStream);
		outputStream.close();
		logger.info("Exit from createRow method.");
	}

	public XSSFWorkbook excelFileRead(String inputFile) { // , String inputSheet
		logger.info("Entered in to excelFileRead method.");
		FileInputStream fileInputStream = null;
		XSSFWorkbook workbook = null;
		try {
			File inputDataFile = new File(inputFile);
			fileInputStream = new FileInputStream(inputDataFile);
			workbook = new XSSFWorkbook(fileInputStream);
		} catch (IOException e) {
			logger.error("Error in excelFileRead method: " + e.getMessage());
			logger.error("Trace: " + e);
		}
		logger.info("Exit from excelFileRead method.");
		return workbook;
	}

	public void excelRead1(XSSFSheet sheet) {
		logger.info("Entered in to excelRead method.");
		String inputFileName = CurrentDirectory.concat("/testartifacts/DTCFile3.xlsx");
		XSSFWorkbook workbook = null;
		String elementName = null;
		String elementValue = null;
		int testCaseNumber = 1;
		int lastCellNumber = 0;
		// LinkedHashMap<String, String> inputPayloadElementsMap = new
		// LinkedHashMap<String, String>();

		try {
			workbook = excelFileRead(inputFileName);
			sheet = workbook.getSheet("TestSheet1");
			// DataFormatter dataformatter = new DataFormatter();
			lastCellNumber = sheet.getRow(0).getLastCellNum();
			for (int j = 0; j < lastCellNumber; j++) {
				elementName = sheet.getRow(0).getCell(j).getStringCellValue();
				Cell cell1 = sheet.getRow(testCaseNumber).getCell(j);
				elementValue = dataformatter.formatCellValue(cell1);
				inputPayloadElementsMap.put(elementName, elementValue);
			}
		} catch (Exception e) {
			logger.error("Error in excelRead method: " + e.getMessage());
			logger.error("Trace: " + e);
		}
		logger.info("Exit from excelRead method.");
	}

	/**
	 * method Name : getTestData purpose : getTestData is a common method used for
	 * all the Telematics Services. Using this method to return a HashMap from the
	 * TestData sheet. param : sheet (Inputs the Testdata Sheet of the service),
	 * rowNum(Rownum of the particular Testcase in TestData sheet) throws :
	 * Exception
	 * 
	 * @return : Null
	 * @author : Narasimha Pulavarthi
	 * @Creation Date : 01-Mar-2021
	 * @Updated By : Srikanth Akula
	 * @Update Date : 28-Jul-2021
	 * @Change : Included code to look at "constructIgniteErrorMessage" method
	 * @version : 1.0
	 *
	 */
	public LinkedHashMap<String, String> getTestData(XSSFSheet sheet, int rowNum,
			LinkedHashMap<String, String> rowDataMap) {
		String elementName = null;
		String elementValue = null;

		BusinessActions businessActions = new BusinessActions();
		for (int inputElementColumn = 0; inputElementColumn < sheet.getRow(0).getLastCellNum(); inputElementColumn++) {
			elementName = getCellData(sheet, 0, inputElementColumn);
			elementValue = getCellData(sheet, rowNum, inputElementColumn);
			rowDataMap.put(elementName, elementValue);

		}
		return rowDataMap;
	}

	public String getCellData(XSSFSheet sheet, int rowNum, int colNum) {
		// logger.info("Entered in to getCellData(int,int) method.");
		String str = null;
		Cell cell = sheet.getRow(rowNum).getCell(colNum);
		str = dataformatter.formatCellValue(cell);
		// logger.info("Exit from getCellData(int,int) method.");
		return str;
	}

	public String getCellData(XSSFSheet sheet, int rowNum, String colVal) {
		// logger.info("Entered in to getCellData(int,String) method.");
		String str = null;
		Cell cell = sheet.getRow(rowNum).getCell(getColNumByColName(sheet, colVal));
		str = dataformatter.formatCellValue(cell);
		// logger.info("Exit from getCellData(int,String) method.");
		return str;
	}

	public int getColNumByColName(XSSFSheet sheet, String strCoulmn) {
		// logger.info("Entered in to getCoulmnNumber (Sheet,String) method.");
		int colnNum = 0;
		for (colnNum = 0; colnNum < sheet.getRow(0).getLastCellNum(); colnNum++) {
			if (sheet.getRow(0).getCell(colnNum).getStringCellValue().equals(strCoulmn)) {
				break;
			}
		}
		// logger.info("Exit from getCoulmnNumber (Sheet,String) method.");
		return colnNum;
	}

	public int getColNumByColName(String excelFileName, String sheet, String strCoulmn) throws IOException {
		// logger.info("Entered in to getCoulmnNumber (Sheet,String) method.");
		rWorkbook = excelFileRead(inputTestDataFolder + environment + "/" + excelFileName);
		rSheet = rWorkbook.getSheet(sheet);
		int colnNum = 0;
		for (colnNum = 0; colnNum < rSheet.getRow(0).getLastCellNum(); colnNum++) {
			if (rSheet.getRow(0).getCell(colnNum).getStringCellValue().equals(strCoulmn)) {
				break;
			}
		}
		rWorkbook.close();
		// logger.info("Exit from getCoulmnNumber (Sheet,String) method.");
		return colnNum;
	}
	
	public int getRowNumByCelVal(String excelFileName, String sheet, String cellVal) throws IOException {
		// logger.info("Entered in to getCoulmnNumber (Sheet,String) method.");
		rWorkbook = excelFileRead(inputTestDataFolder + environment + "/" + excelFileName);
		rSheet = rWorkbook.getSheet(sheet);
		int rowNum;
		int colNum = 0;
		outerloop: for (rowNum = 1; rowNum < rSheet.getLastRowNum(); rowNum++) {
			for (colNum = 0; colNum < rSheet.getRow(0).getLastCellNum(); colNum++) {
				//dataformatter.formatCellValue(cell)
				//String val1=rSheet.getRow(rowNum).getCell(colNum).getStringCellValue();
				if (rSheet.getRow(rowNum).getCell(colNum).getStringCellValue().equals(cellVal)) {
					break outerloop;
				}
			}
		}
		rWorkbook.close();
		return rowNum;
	}

	public String getFinalResult() throws Exception {
		logger.info("Entered in to getFinalResult (String) method.");
		String FinalResult = "";
		try {
			rSheet = rWorkbook.getSheet(serviceName);
			int lastRowNumber = rSheet.getLastRowNum();
			for (int rowNumber = 1; rowNumber <= lastRowNumber; rowNumber++) {
				Cell cell = rSheet.getRow(rowNumber).getCell(getColNumByColName(rSheet, "TestScriptResult"));
				FinalResult = dataformatter.formatCellValue(cell);
				if (FinalResult.equalsIgnoreCase("FAIL"))
					break;
			} // End of for loop
			if (!FinalResult.equalsIgnoreCase("FAIL"))
				FinalResult = "PASS";
		} catch (Exception e) {
			logger.error("Error in getFinalResult method: " + e.toString());
			logger.error("Trace: " + e);
		}
		logger.info("Exit from getFinalResult (String) method.");
		return FinalResult;
	}

	public int getLastRowCountatSpecificColumn(XSSFSheet xlSheet, String columnName) {
		int rowCount = 0;
		Map<String, Integer> columnHeadingsInsqlSheet = getHeadingsInExcelSheet(xlSheet);
		for (Row row : xlSheet) {
			if (row.getCell(columnHeadingsInsqlSheet.get(columnName)) != null)
				rowCount++;
		}
		rowCount = rowCount - 1;
		return rowCount;
	}

	public Map<String, Integer> getHeadingsInExcelSheet(XSSFSheet excelSheet) {
		int colNum = excelSheet.getRow(0).getLastCellNum();
		Map<String, Integer> columnHeading = new HashMap<String, Integer>();
		if (excelSheet.getRow(0).cellIterator().hasNext()) {
			for (int j = 0; j < colNum; j++) {
				columnHeading.put(excelSheet.getRow(0).getCell(j).getStringCellValue(), j);
			}
		}
		return columnHeading;
	}

}