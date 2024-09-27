package testScripts;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import functionalLib.BusinessActions;
import io.restassured.response.Response;
import utilities.CommonUtilities;
import utilities.DataBaseOps;
import utilities.ExcelOps;
import utilities.Global;
import utilities.RASoapService;
import utilities.Report;

public class GetEmployee extends Global {

	ExcelOps excelOps = new ExcelOps();
	BusinessActions businessActions = new BusinessActions();
	RASoapService soapServices = new RASoapService();
	CommonUtilities commonUtils = new CommonUtilities();
	//DataBaseOps dbOps = new DataBaseOps();
	Report report = new Report();
	ResultSet rs = null;
	Date systemDate = new Date();

	public GetEmployee() throws FileNotFoundException, IOException, ParseException {
		logger.info("Entered into " + serviceName + " method.");
		Response response = null;
		String baseURI, inputPayload;
		String executionFlag = null, actResponseCode = "", xmlResutInDB = "";
		String[] resultSheetHeaders = null;
		int rowNum = 0;
		double serviceResponseTime = 0;
		Map<String, String> xmlNodeValuesMap = null;

		workbook = excelOps.excelFileRead(inputTestDataFolder + environment + "/" + testScriptFile);
		baseURI = commonUtils.getProperties(environment + ".endpointUrl");
		baseURI = baseURI + resourcePath;
		//soaDBConnection = dbOps.getConnection(soaDB);
		//logger.info("Connected to Database in " + serviceName + " method.");
		try {
			resultSheetHeaders = commonUtils.getProperties("mdiHeaders").split(",");
			excelOps.createRow(resultFile, serviceName, 0, resultSheetHeaders);
		} catch (Exception e1) {
			logger.error("Error in " + serviceName + " method: " + e1.getMessage());
			logger.error("Trace: " + e1);
		}

		for (rowNum = 1; rowNum <= tdSheet.getLastRowNum(); rowNum++) {
			logger.info("Execution started for TestCase No:" + rowNum);
			executionFlag = excelOps.getCellData(tdSheet, rowNum, "ExecutionFlag");

			if ("Yes".equalsIgnoreCase(executionFlag)) {
				try {
					// Thread.sleep(10000);
					inputPayloadElementsMap = excelOps.getTestData(tdSheet, rowNum, inputPayloadElementsMap);
					inputPayload = businessActions.generateInputPayload(rowNum, templateName);
					System.out.println(inputPayload);
					response = soapServices.soapRequest(baseURI, inputPayload);
					xmlNodeValuesMap = soapServices.getXMLNodeValuesMap(response);
					//actResponseCode = xmlNodeValuesMap.get("StatusCode");
//					System.out.println(xmlNodeValuesMap.get("StatusCode"));
//					System.out.println(xmlNodeValuesMap.get("StatusMessage"));
				//	String statusMessage = xmlNodeValuesMap.get("return");
					int actResponseCode1 = response.getStatusCode();
					String actResponseCode2 = response.getStatusLine();
					System.out.println(xmlNodeValuesMap.get("name"));
					System.out.println(xmlNodeValuesMap.get("id"));
					
					String payload_TraceId = null;
					payload_TraceId = excelOps.getCellData(sqlSheet, 1, "Trace_Id");

					payload_TraceId = StringUtils.substringBetween(
							StringUtils.substringBetween(response.asString(), payload_TraceId, payload_TraceId), ">",
							"<");
					inputPayloadElementsMap.put("payload_TraceId", payload_TraceId);

					failureReason += soapServices.responseCodeValidation(String.valueOf(actResponseCode1),actResponseCode2);
					serviceResponseTime = soapServices.responseTime(response);
					serviceResponseTime = businessActions.validateResponseTime(serviceResponseTime,
							"");
//					if (StringUtils.isEmpty(failureReason)) {
//						dbOps.validateDBResult();
//						dbResultSet.beforeFirst();
//						while (dbResultSet.next()) {
//							xmlResutInDB = dbResultSet.getString("Result");
//						}
//						commonUtils.compareXML(response.asString(), xmlResutInDB,
//								inputPayloadElementsMap.get("payload_parentElementTagName"));
//					}

				} catch (Exception e) {
					failureReason += "Exception in " + serviceName + " service. Reason:- " + e.getMessage();
					logger.error("Exception in " + serviceName + " service. Reason:- " + e);
				}
				if (!failureReason.isEmpty()) {
					resultSheetHeaders = new String[] { String.valueOf(rowNum), inputPayloadElementsMap.get("TestCase"),
							inputPayloadElementsMap.get(expectedHttpResponseCode), actResponseCode,
							String.valueOf(serviceResponseTime), "FAIL", failureReason };
					excelOps.createRow(resultFile, serviceName, rowNum, resultSheetHeaders);
					report.failTest(inputPayloadElementsMap.get("TestCase"), failureReason);
					logger.info("Execution status of Test Case number: '" + rowNum + "' is : FAIL. Failure reason is: "
							+ failureReason);
					failureReason = "";
				} else {
					resultSheetHeaders = new String[] { String.valueOf(rowNum), inputPayloadElementsMap.get("TestCase"),
							inputPayloadElementsMap.get(expectedHttpResponseCode), actResponseCode,
							String.valueOf(serviceResponseTime), "PASS", failureReason };
					excelOps.createRow(resultFile, serviceName, rowNum, resultSheetHeaders);
					report.passTest(inputPayloadElementsMap.get("TestCase"));
					logger.info("Execution status of Test Case number: '" + rowNum + "' is : PASS ");
				}
			} else {
				resultSheetHeaders = new String[] { String.valueOf(rowNum),
						excelOps.getCellData(tdSheet, rowNum, "TestCase"), "", "", "", "Not Executed", failureReason };
				excelOps.createRow(resultFile, serviceName, rowNum, resultSheetHeaders);
				// report.skipTest(excelOps.getCellData(rowNum, "TestCase"));
				logger.info("Execution status of Test Case number: '" + rowNum + "' is : Execution Not required ");
			}
		}
		try {
			//soaDBConnection.close();
			//logger.info("Database Connection Closed in " + serviceName + " method.");
		} catch (Exception e) {
		//	logger.error("Error while closing the Database connection in " + serviceName + " method." + e);
		}
		logger.info("Exit from " + serviceName + " method.");
	}

}
