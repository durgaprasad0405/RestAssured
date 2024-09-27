package testScripts;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.gson.Gson;

import functionalLib.BusinessActions;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import utilities.CommonUtilities;
import utilities.DataBaseOps;
import utilities.ExcelOps;
import utilities.Global;
import utilities.Report;
import utilities.RestServices;

public class UpdateLocation extends Global {

	ExcelOps excelOps = new ExcelOps();
	BusinessActions businessActions = new BusinessActions();
	RestServices restServices = new RestServices();
	CommonUtilities commonUtils = new CommonUtilities();
	DataBaseOps dbOps = new DataBaseOps();
	Report report = new Report();
	ResultSet rs = null;
	Date systemDate = new Date();

	public UpdateLocation() throws FileNotFoundException, IOException, ParseException {
		logger.info("Entered in to " + serviceName + " method.");
		Response response = null;
		String baseURI, contentType, userName, password, inputPayload;
		String dbErrorCode = null, dbErrorMessage = null, executionFlag = null, randomId = "", actResponseCode = "",
				teleWCDDIHIST = "", teleServiceUsageDataService = "", teleWCDDI = "", teleDDIServiceRouter = "",
				wliTotalResponseTime = "";
		String[] resultSheetHeaders = null;
		int rowNum = 0;
		

		workbook = excelOps.excelFileRead(inputTestDataFolder + environment + "/" + testScriptFile);
		baseURI = commonUtils.getProperties(environment + ".endpointUrl");
		contentType = commonUtils.getProperties(environment + ".contentType");
		userName = commonUtils.getProperties(environment + ".ddi_username");
		password = commonUtils.getProperties(environment + ".ddi_password");
		
		try {
			resultSheetHeaders = commonUtils.getProperties("ddiHeaders").split(",");
			resultSheetHeaders = commonUtils.appendStringToStringArray(resultSheetHeaders,
					"teleWCDDIHIST Response Time (sec)");
			resultSheetHeaders = commonUtils.appendStringToStringArray(resultSheetHeaders,
					"teleServiceUsageDataService Response Time (sec)");
			randomId = commonUtils.dateWithMilliSeconds(systemDate);

			excelOps.createRow(resultFile, serviceName, 0, resultSheetHeaders);
		} catch (Exception e1) {
			logger.error("Error in " + serviceName + " method: " + e1.getMessage());
			logger.error("Trace: " + e1);
		}

		for (rowNum = 1; rowNum <= tdSheet.getLastRowNum(); rowNum++) {
			logger.info("Execution started for TestCase No:" + rowNum);
			executionFlag = excelOps.getCellData(tdSheet, rowNum, "ExecutionFlag");
			event_id = serviceName + "_" + randomId;

			if ("Yes".equalsIgnoreCase(executionFlag)) {
				try {
				
					inputPayloadElementsMap = excelOps.getTestData(tdSheet, rowNum, inputPayloadElementsMap);
					inputPayload = businessActions.generateInputPayload(rowNum, templateName);
					System.out.println(inputPayload);
					
					resourcePath = resourcePath+"?key="+inputPayloadElementsMap.get("payload_key")+"&place_id="+inputPayloadElementsMap.get("payload_place_id");
					
					response = restServices.putMethod(baseURI, resourcePath, contentType, userName, password,
							inputPayload);
					String resp = response.asString();
					
					//  create Gson object
			        Gson gson = new Gson();
			        //read JSON to a java HashMap
			        HashMap<String, Object> mapLocation = gson.fromJson(resp, HashMap.class); 
			        System.out.println( mapLocation.containsValue("Address successfully updated"));
			     
			        
					
					actResponseCode = String.valueOf(response.getStatusCode());
					restServices.responseCodeValidation(response);
					
					
					
					
					
			
				} catch (Exception e) {
					failureReason += "Exception in " + serviceName + " service. Reason:- " + e.getMessage();
					logger.error("Exception in " + serviceName + " service. Reason:- " + e);
				}
				if (!failureReason.isEmpty()) {
					resultSheetHeaders = new String[] { String.valueOf(rowNum), inputPayloadElementsMap.get("TestCase"),
							event_id, inputPayloadElementsMap.get(expectedHttpResponseCode), actResponseCode,
							// String.valueOf(serviceResponseTime)
							dbErrorCode, dbErrorMessage, "FAIL", failureReason, wliTotalResponseTime, teleWCDDI,
							teleDDIServiceRouter, teleWCDDIHIST, teleServiceUsageDataService };
					excelOps.createRow(resultFile, serviceName, rowNum, resultSheetHeaders);
					report.failTest(inputPayloadElementsMap.get("TestCase"), failureReason);
					logger.info("Execution status of Test Case number: '" + rowNum + "' is : FAIL. Failure reason is: "
							+ failureReason);
					failureReason = "";
				} else {
					resultSheetHeaders = new String[] { String.valueOf(rowNum), inputPayloadElementsMap.get("TestCase"),
							event_id, inputPayloadElementsMap.get(expectedHttpResponseCode), actResponseCode,
							// String.valueOf(serviceResponseTime)
							dbErrorCode, dbErrorMessage, "PASS", failureReason, wliTotalResponseTime, teleWCDDI,
							teleDDIServiceRouter, teleWCDDIHIST, teleServiceUsageDataService };
					excelOps.createRow(resultFile, serviceName, rowNum, resultSheetHeaders);
					report.passTest(inputPayloadElementsMap.get("TestCase"));
					logger.info("Execution status of Test Case number: '" + rowNum + "' is : PASS ");
				}
				
			} else {
				resultSheetHeaders = new String[] { String.valueOf(rowNum),
						excelOps.getCellData(tdSheet, rowNum, "TestCase"), "", "", "", dbErrorCode, dbErrorMessage,
						"Not Executed", failureReason };
				excelOps.createRow(resultFile, serviceName, rowNum, resultSheetHeaders);
				// report.skipTest(excelOps.getCellData(rowNum, "TestCase"));
				logger.info("Execution status of Test Case number: '" + rowNum + "' is : Execution Not required ");
			}
			teleWCDDIHIST = "";
			teleServiceUsageDataService = "";
			teleWCDDI = "";
			teleDDIServiceRouter = "";
			wliTotalResponseTime = "";

		}
		
		logger.info("Exit from " + serviceName + " method.");
	}

}
