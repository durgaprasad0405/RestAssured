package driver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.testng.annotations.Test;
//import org.junit.Test;

import utilities.CommonUtilities;
import utilities.ExcelOps;
import utilities.Global;
import utilities.Report;

public class DriverScript extends Global {
	static CommonUtilities commonUtils = new CommonUtilities();
	static Report report = new Report();
	//static EmailOps emailOps = null;
	static String emailContent = "";
	
	@SuppressWarnings("static-access")
	@Test
	public static void executeSuite() throws FileNotFoundException, IOException {
		commonUtils.setup();
		logger.info("Entered in to executeSuite method");
		int lastRow = 0;
		String executionFlag;
		String finalResult="";
		Date systemDate = new Date();
		ExcelOps excelOps = new ExcelOps();
		XSSFSheet testSetSheet = null;
		String[] headers = null;
		StringBuilder EmailBody = new StringBuilder();
		int Rownumber = 1;

		commonUtils.createFolder(testResultsFolder);
		try {
			resultFile = resultFile + commonUtils.dateWithMilliSeconds(systemDate) + ".xlsx";
			extReportFile = extReportFile+commonUtils.dateWithMilliSeconds(systemDate)+".html";
			headers = commonUtils.getProperties("resultSheetHeaders").split(",");
			excelOps.createRow(resultFile, "Execution Status", 0, headers);
			report.onStart(extReportFile);
		} catch (Exception e1) {
			logger.error("Error while creating a result file." + e1.getMessage());
			logger.error("Trace:" + e1);
		}
		workbook = excelOps.excelFileRead(inputTestSetFileName);
		testSetSheet = workbook.getSheet("WebServiceModules");
		lastRow = testSetSheet.getLastRowNum();
		
		EmailBody.append("<p>Hi,</p><p>Please find the below execution results for regression suite:</p>");
		EmailBody.append(
				"<table border=\"1\" layout: fixed style=\"width:600px\"><tr><th layout: fixed style=\"width:200px\">ServiceName</th><th layout: fixed style=\"width:200px\">Environment</th><th layout: fixed style=\"width:200px\">Result</th></tr></table>");
		for (int rowNum = 1; rowNum <= lastRow; rowNum++) {
			workbook = excelOps.excelFileRead(inputTestSetFileName);
			testSetSheet = workbook.getSheet("WebServiceModules");
			
			serviceName = excelOps.getCellData(testSetSheet, rowNum, "ServiceName");
			serviceType = excelOps.getCellData(testSetSheet, rowNum, "Service Type");
			resourcePath = excelOps.getCellData(testSetSheet, rowNum, "Resource Path");
			testScriptFile = excelOps.getCellData(testSetSheet, rowNum, "Script File");
			//market = excelOps.getCellData(testSetSheet, rowNum, "Market");
			environment = excelOps.getCellData(testSetSheet, rowNum, "Environment");
			executionFlag = excelOps.getCellData(testSetSheet, rowNum, "ExecutionFlag");
			
			workbook = excelOps.excelFileRead(inputTestDataFolder + environment + "/" + testScriptFile);
			tdSheet = workbook.getSheet(serviceName + "_TestData");
			sqlSheet = workbook.getSheet(serviceName + "_SQL");
			inputPayloadElementsMap.clear();
			failureReason="";
			event_id = "";
			

			if ("Yes".equalsIgnoreCase(executionFlag)) {
				try {
					
					report.extentSummary();
					logger.info("Execution started for " + serviceName);
					// Redirects to service that needs to be executed.
					commonUtils.serviceRedirection();
					//Capturing final result.
					finalResult = excelOps.getFinalResult();
					headers = new String[] { serviceName, serviceType, environment, finalResult };
					excelOps.createRow(resultFile, "Execution Status", Rownumber, headers);
					logger.info("Execution completed for " + serviceName + ". Final result is:" + finalResult);
				//	EmailBody.append(emailOps.emailContent(serviceName, environment, finalResult));
					Rownumber = Rownumber+1;
				} catch (Exception e) {
					logger.error("Error when validating serviceRediretion method: " + e.getMessage());
					logger.error("Trace: " + e);
				}
			} else {
				System.out.println("Execution Not required");
				headers = new String[] { serviceName, serviceType, environment, "Not executed" };
				//excelOps.createRow(resultFile, "Execution Status", rowNum, headers);
				logger.info(serviceName + " is not selected for execution.");
				//report.skipTest(serviceName);
			}
		}
		EmailBody.append("<p>Thanks & Regards,</p><p>Narasimha</p>");
		report.endReport();
		
		emailContent = EmailBody.toString();
		//emailOps.sendEmail(emailContent, resultFile.toString(),extReportFile);
		logger.info("Exit from executeSuite method");
	}

	public void finalMethod() {
		System.out.println("Execution Completed");
	}

	public static void main(String[] args) {
		try {
			executeSuite();
		} catch (Exception e) {
			logger.error("Trace: "+e);
		}
	}

}