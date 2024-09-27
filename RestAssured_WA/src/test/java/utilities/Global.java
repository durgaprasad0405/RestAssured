package utilities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class Global {

	// Excel related global variables
	public static XSSFWorkbook workbook;
	//public static XSSFWorkbook vinWorkbook;
	public static XSSFSheet tdSheet;
	public static XSSFSheet sqlSheet;
	//public static XSSFSheet vinSheet;
	public static XSSFRow rowHead;

	// DB related global variables
	public static Connection apiDBConnection; //DB Connection Details
	public static ResultSet dbResultSet = null;
	public static String soaDB ="soaDB"; //DB Name
	public static String queryNoPrefix = "Query.No";

	// Log4j related global variables
	public static Logger logger;

	// Reports related global variables
	public static ExtentReports extentReport;
	public static ExtentTest extentTest;
	public static ExtentTest extentTests;

	// Dates related global variables
	static Date systemDate = new Date();
	DataFormatter dataformatter = new DataFormatter();
	private String json;

	// Path's related Global variables
	public static String CurrentDirectory = System.getProperty("user.dir");
//	public static String inputTestSetFileName = CurrentDirectory.concat("/testartifacts/TestSet/TestSet.xlsx");
	public static String inputTestSetFileName = CurrentDirectory.concat("/testartifacts/TestSet/TestSet1.xlsx");
	public static String inputTestDataFolder = CurrentDirectory.concat("/testartifacts/TestData.");//"C:\Users\draghava\OneDrive - Capgemini\Documents\All Programs\RestAssured\RestAssured_WA\testartifacts\TestData.OCIDev\PostBook.xlsx"
	public static String testResultsFolder = CurrentDirectory.concat("/testartifacts/TestResults/");
	public static String inputXMLTemplatesFolder = CurrentDirectory.concat("/testartifacts/inputXMLTemplates/");
	public static String propertyFile = CurrentDirectory.concat("/testartifacts/TestSet/app.properties");
	public static String privateKey = CurrentDirectory.concat("/testartifacts/TestSet/private.ppk");
	public static String extReportFile = CurrentDirectory.concat("/reports/TestExecutionSummary-");
	public static String resultFile = "" + testResultsFolder + "Test_Result_File_";
	public static String restAssuredWALogFile = "restAssuredWALogFile" + systemDate;

	// Prefix's related Global variables
	public static String columnPrefix = "payload_";
	public static String templateName = "Template_Name";
	public static String expectedHttpResponseCode = "Expected_HTTP_RESPONSE";
	public static String expectedHttpResponseMsg = "Expected_HTTP_Message";
	public static String expectedStatusMessage = "Expected_Status_Message";
	public static String genericErrorMessage = "payload_tdi_error_message";
	
	//new added lines
		public static String ExpectedAPIHTTPRESPONSE = "Expected_API_HttpResponse";
		public static String ExpectedDBHTTPRESPONSE = "Expected_DB_HttpResponse";
//		public static String ExpectedDBStatus = "Expected_DB_Status";
//		public static String ExpectedDBErrorCode = "Expected_DB_ErrorCode";
//		public static String ExpectedDBErrorMessage = "Expected_DB_ErrorMessage";


		

	
	// Reusable Strings

	public static String serviceName;
	//public static String market;
	public static String environment;
	public static String serviceType;
	public static String testScriptFile;
	public static String event_id;
	public static String trans_id;
	public static String primaryKey;
	public static String primaryKeyVal;
	public static String failureReason = "";
	public static String resourcePath;
	public static String vinFile;
	public static String Source_trasaction_id;

	public static LinkedHashMap<String, String> inputPayloadElementsMap = new LinkedHashMap<String, String>();
	
	
}