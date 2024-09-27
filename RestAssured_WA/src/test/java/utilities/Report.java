package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class Report extends Global {

	
	ExtentHtmlReporter htmlReporter;
	static CommonUtilities commonUtils = new CommonUtilities();
	
	public void onStart(String extReportFile) {
		logger.info("Entered in to onStart method.");
		extentReport = new ExtentReports();
		try {
			htmlReporter = new ExtentHtmlReporter(extReportFile);
			htmlReporter.config().setDocumentTitle("REST ASSURED AUTOMATION");
			htmlReporter.config().setReportName("API - Test Execution Report");
			htmlReporter.config().setTheme(Theme.DARK);
			
			extentReport.attachReporter(htmlReporter);
			extentReport.setSystemInfo("User Name", "APT Team");
			extentReport.setSystemInfo("Owner", "CG_APT");
			extentReport.setSystemInfo("Environment", "APT_Dev");
		} catch (Exception e) {
			logger.error("Error in onStart method: " + e.getMessage());
			logger.error("Trace: " + e);
		}
		logger.info("Exit from onStart method.");
	}

	public void extentSummary() {
		logger.info("Entered in to extentSummary method.");
		extentTest = extentReport.createTest(serviceName);
		logger.info("Exit from extentSummary method.");
	}

	public void passTest(String testCaseName) {
		logger.info("Entered in to passTest method.");
		testCaseName = "Test Case: " + testCaseName + "<br />Result: PASS.";
		extentTest.log(Status.PASS, testCaseName);
		logger.info("Test Case: " + testCaseName + "<br />Result: PASS.");
		logger.info("Exit from passTest method.");
	}

	public void failTest(String testCaseName, String failureReason) {
		logger.info("Entered in to failTest method.");
		testCaseName = "<span style='font-weight:bold;'>Test Case:</span> " + testCaseName
				+ "<br /><span style='font-weight:bold;'>Result: FAIL.</span> <br /><span style='font-weight:bold;'>Failure Reason is: </span>"
				+ failureReason;
		extentTest.log(Status.FAIL, testCaseName);
		logger.info(testCaseName);
		logger.info("Exit from failTest method.");
	}

	public void skipTest(String testCaseName) {
		logger.info("Entered in to skipTest method.");
		try {
			testCaseName = "Test Case: " + testCaseName + "<br />Result: Not Executed.";
			extentTest.log(Status.SKIP, testCaseName);
			logger.info(testCaseName);
		} catch (Exception e) {
			logger.error("Error in skipTest method: " + e.getMessage());
			logger.error("Trace: " + e);
		}
		logger.info("Exit from skipTest method.");
	}

	public void endReport() {
		logger.info("Entered in to endReport method.");
		extentReport.flush();
		//extentReport.close();
		logger.info("Exit from endReport method.");
	}
}
