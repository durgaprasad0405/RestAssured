package functionalLib;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.LinkedHashMap;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import io.restassured.response.Response;
import utilities.CommonUtilities;
import utilities.DataBaseOps;
import utilities.ExcelOps;
import utilities.Global;
import utilities.RestServices;

public class BusinessActions extends Global {

	ExcelOps excelOps = new ExcelOps();
	CommonUtilities commonUtils = new CommonUtilities();
	String warningLightDesc = "wb";
	DataBaseOps dbOps = new DataBaseOps();
	RestServices restServices = new RestServices();

	public String generateInputPayload(int rowNum, String columnName) {
		logger.info("Entered in to generateInputPayload method.");
		String inputElementName = null;
		String inputElementValue = null;
		String inputPayload = "";
		String payload_vin = null;
		try {
			inputPayload = commonUtils.textFileRead(inputXMLTemplatesFolder,
					excelOps.getCellData(tdSheet, rowNum, columnName));
			// payload_vin=getVin(rowNum);
			// inputPayload = inputPayload.replace("payload_vin", payload_vin);
			for (int inputElementColumn = 1; inputElementColumn < tdSheet.getRow(0)
					.getLastCellNum(); inputElementColumn++) {
				inputElementName = excelOps.getCellData(tdSheet, 0, inputElementColumn);
				inputElementValue = excelOps.getCellData(tdSheet, rowNum, inputElementColumn);

				inputPayload = inputPayload.replace(inputElementName, inputElementValue);

				if (inputElementName.equalsIgnoreCase("payload_user_name"))
					inputPayload = inputPayload.replace("payload_password",
							commonUtils.getProperties(environment + "." + inputElementValue + ".password"));
			}
		} catch (Exception e) {
			logger.error("Error in generateInputPayload method: " + e.getMessage());
			logger.error("Trace: " + e);
		}
		logger.info("Exit from generateInputPayload method.");
		return inputPayload;
	}

	public double validateResponseTime(double serviceResponseTime, String sqlTable) throws SQLException {
		double responseTime = 0, seconds = 1000;
		double logAuditResponseTime = 0;
		String columnValue = null;
		if (!StringUtils.isEmpty(sqlTable)) {
			columnValue = dbOps.getSQLColumnValueByTable(sqlTable);
			if (!StringUtils.isEmpty(columnValue))
				logAuditResponseTime = Double.parseDouble(columnValue);
		}
		responseTime = (logAuditResponseTime + serviceResponseTime) / seconds;
		if (responseTime >= 5) {
			failureReason += "Responce time is more than expected (5 Sec), i.e: " + serviceResponseTime + " Seconds";
			System.out.println("FAIL DUE TO RESPONSE TIME");
		}
		// }
		return responseTime;
	}

	public void validateResponseTime(double serviceResponseTime) throws SQLException {
		if (serviceResponseTime >= 5) {
			failureReason += "Responce time is more than expected (5 Sec), i.e: " + serviceResponseTime + " Seconds";
			System.out.println("FAIL DUE TO RESPONSE TIME");
		}
	}

	public String convertTimeMilliSecondsToSeconds(String i) {
		String j = "";
		double seconds = 1000;
		if (!StringUtils.isEmpty(i)) {
			j = Double.toString((Double.valueOf(i) / seconds));
		} else {
			j = i;
		}
		return j;

	}

	/**
	 * method Name : getDBResultForIgniteServices purpose : To get the DB result for
	 * the ignite Services.] param : response (Response object of the POST Call)
	 * throws : FileNotFoundException, IOException, ParseException
	 * 
	 * @return : dbResponse
	 * @author : Narasimha Pulavarthi
	 * @Creation Date : 01-Mar-2021
	 * @Updated By : Srikanth Akula
	 * @Update Date : 27-Jul-2021
	 * @Change : Modified dbResultSet.getString("error_code") -->
	 *         dbResultSet.getString("tdi_error_code") and
	 *         dbResultSet.getString("error_message") -->
	 *         dbResultSet.getString("tdi_error_message") as per the latest Query in SQL
	 *         TestData sheet.
	 * @version : 1.0
	 *
	 */


	public LinkedHashMap<String, String> getDBResultForDDIServices(Response response, String service1, String service2)
			throws SQLException {
		String teleWCDDI = "teleWCDDI", teleDDIServiceRouter = "teleDDIServiceRouter",
				wliTotalResponseTime = "wliTotalResponseTime";

		LinkedHashMap<String, String> dbResponse = new LinkedHashMap<String, String>();

		dbOps.validateDBResult();

		dbOps.executeQueryBySpecificSQLTable("TELE_DATA_INTERFACE_TBL", apiDBConnection);

		while (dbResultSet.next()) {
			//dbResponse.put("dbhttpresponse", dbResultSet.getString("http_response"));

			dbResponse.put("dbErrorCode", dbResultSet.getString("error_code"));
			dbResponse.put("dbErrorMessage", dbResultSet.getString("error_message"));
		}

		if (StringUtils.equalsIgnoreCase(inputPayloadElementsMap.get("ResponseTimeCalcFlag"), "Yes")) {
			dbOps.executeQueryBySpecificSQLTable("SP313887742_SOAINFRA.wli_qs_report_data", apiDBConnection);
			while (dbResultSet.next()) {
				dbResponse.put(teleWCDDI, convertTimeMilliSecondsToSeconds(dbResultSet.getString(teleWCDDI)));
				if (!StringUtils.isEmpty(dbResultSet.getString(teleWCDDI)))
					validateResponseTime(
							Double.parseDouble(convertTimeMilliSecondsToSeconds(dbResultSet.getString(teleWCDDI))));
				dbResponse.put(teleDDIServiceRouter,
						convertTimeMilliSecondsToSeconds(dbResultSet.getString(teleDDIServiceRouter)));
				dbResponse.put(wliTotalResponseTime,
						convertTimeMilliSecondsToSeconds(dbResultSet.getString(wliTotalResponseTime)));

				if (!StringUtils.isEmpty(service1))
					dbResponse.put(service1, convertTimeMilliSecondsToSeconds(dbResultSet.getString(service1)));
				if (!StringUtils.isEmpty(service2))
					dbResponse.put(service2, convertTimeMilliSecondsToSeconds(dbResultSet.getString(service2)));
			}
		} else if (StringUtils.equalsIgnoreCase(inputPayloadElementsMap.get("ResponseTimeCalcFlag"), "No")) {
			if (response.getStatusCode() != 204) {
				dbOps.executeQueryBySpecificSQLTable("LOG_AUDIT", apiDBConnection);
				while (dbResultSet.next()) {
					dbResponse.put(teleWCDDI, convertTimeMilliSecondsToSeconds(dbResultSet.getString(teleWCDDI)));
					if (!StringUtils.isEmpty(dbResultSet.getString(teleWCDDI)))
						validateResponseTime(
								Double.parseDouble(convertTimeMilliSecondsToSeconds(dbResultSet.getString(teleWCDDI))));
					dbResponse.put(teleDDIServiceRouter,
							convertTimeMilliSecondsToSeconds(dbResultSet.getString(teleDDIServiceRouter)));
					dbResponse.put(wliTotalResponseTime,
							convertTimeMilliSecondsToSeconds(dbResultSet.getString(wliTotalResponseTime)));

					if (!StringUtils.isEmpty(service1))
						dbResponse.put(service1, convertTimeMilliSecondsToSeconds(dbResultSet.getString(service1)));
					if (!StringUtils.isEmpty(service2))
						dbResponse.put(service2, convertTimeMilliSecondsToSeconds(dbResultSet.getString(service2)));
				}

			} else {
				String totalResponseTime = "";
				double r = 0;
				dbOps.executeQueryBySpecificSQLTable("LOG_AUDIT", apiDBConnection);
				while (dbResultSet.next()) {
					dbResponse.put(teleDDIServiceRouter,
							convertTimeMilliSecondsToSeconds(dbResultSet.getString(teleDDIServiceRouter)));
					totalResponseTime = convertTimeMilliSecondsToSeconds(dbResultSet.getString(wliTotalResponseTime));
					if (!StringUtils.isEmpty(service1))
						dbResponse.put(service1, convertTimeMilliSecondsToSeconds(dbResultSet.getString(service1)));
					if (!StringUtils.isEmpty(service2))
						dbResponse.put(service2, convertTimeMilliSecondsToSeconds(dbResultSet.getString(service2)));
				}
				/*
				 * dbOps.executeQueryBySpecificSQLTable("wli_qs_report_data", wliDBConnection);
				 * while (dbResultSet.next()) { dbResponse.put(teleWCDDI,
				 * convertTimeMilliSecondsToSeconds(dbResultSet.getString(teleWCDDI))); if
				 * (!StringUtils.isEmpty(dbResultSet.getString(teleWCDDI))) {
				 * validateResponseTime(
				 * Double.parseDouble(convertTimeMilliSecondsToSeconds(dbResultSet.getString(
				 * teleWCDDI)))); r = Double.valueOf(totalResponseTime) +
				 * Double.valueOf(convertTimeMilliSecondsToSeconds(dbResultSet.getString(
				 * teleWCDDI))); dbResponse.put(wliTotalResponseTime, Double.toString(r)); }
				 * else { dbResponse.put(wliTotalResponseTime, totalResponseTime); } }
				 */
			}

		}
		return dbResponse;
	}

	public String hexToBase64Convertion(String HexaValue) {
		logger.info("Entered in to hexToBase64Convertion method.");
		String encodedHexB64 = null;
		byte[] decodedHex;
		try {
			decodedHex = Hex.decodeHex(HexaValue);
			encodedHexB64 = Base64.encodeBase64String(decodedHex);
		} catch (DecoderException e) {
			logger.error("Error in hexToBase64Convertion method: " + e.getMessage());
			logger.error("Trace: " + e);
		}
		logger.info("Exit from hexToBase64Convertion method.");
		return encodedHexB64;
	}

	public String stringToHexConvertion(String textValue) {
		logger.info("Entered in to stringToHexConvertion method.");
		String hexString = null;
		try {
			hexString = Hex.encodeHexString(textValue.getBytes("UTF-8"));
		} catch (Exception e) {
			logger.error("Error in stringToHexConvertion method: " + e.getMessage());
			logger.error("Trace: " + e);
		}
		logger.info("Exit from stringToHexConvertion method.");
		return hexString;
	}

	// @Test
	public String decimalToHexConvertion(int decimalValue) {
		logger.info("Entered in to decimalToHexConvertion method.");
		String hexString = null;
		try {
			hexString = Integer.toHexString(decimalValue);

			if (hexString.length() % 2 != 0)
				hexString = "0" + hexString;

		} catch (Exception e) {
			logger.error("Error in decimalToHexConvertion method: " + e.getMessage());
			logger.error("Trace: " + e);
		}
		logger.info("Exit from decimalToHexConvertion method.");
		return hexString;
	}

	// @Test
	public String latlonToHexConvertion(double decimalValue) {
		logger.info("Entered in to latlonToHexConvertion method.");
		int value = 0;
		value = (int) (decimalValue * 3600000);
		String hexString = null;
		try {
			hexString = Integer.toHexString(value);

			if (hexString.length() % 2 != 0)
				hexString = "0" + hexString;

		} catch (Exception e) {
			logger.error("Error in latlonToHexConvertion method: " + e.getMessage());
			logger.error("Trace: " + e);
		}
		logger.info("Exit from latlonToHexConvertion method.");
		return hexString;
	}

	// @Test
	// Initial method. Found an issue in this and written the below
	public String binaryToHexConvertion1(String binhexString) {
		logger.info("Entered in to binaryToHexConvertion method.");
		try {
			binhexString = new BigInteger(binhexString, 2).toString(16);

			if (binhexString.length() % 2 != 0)
				binhexString = "0" + binhexString;

		} catch (Exception e) {
			logger.error("Error in binaryToHexConvertion method: " + e.getMessage());
			logger.error("Trace: " + e);
		}
		logger.info("Exit from binaryToHexConvertion method.");
		return binhexString;
	}

	// @Test
	public String binaryToHexConvertion(String binhexString) {
		logger.info("Entered in to binaryToHexConvertion method.");
		int interval = 8, j = 0, lastIndex = 0, arrayLength = 0;
		String hexaString = null, finalString = "";
		try {
			arrayLength = (int) Math.ceil(((binhexString.length() / (double) interval)));
			String[] result = new String[arrayLength];
			lastIndex = result.length;

			for (int i = 0; i < lastIndex; i++) {
				result[i] = binhexString.substring(j, j + interval);
				j += interval;
				hexaString = new BigInteger(result[i], 2).toString(16);
				if (hexaString.length() % 2 != 0)
					hexaString = "0" + hexaString;
				finalString = finalString + hexaString;
			}
		} catch (Exception e) {
			logger.error("Error in binaryToHexConvertion method: " + e.getMessage());
			logger.error("Trace: " + e);
		}
		logger.info("Exit from binaryToHexConvertion method.");
		return finalString;

	}

	// @Test
	public String timeStamptoHexConvertion(String timeStampHex) {
		logger.info("Entered in to timeStamptoHexConvertion method.");
		String[] yearMonth = timeStampHex.split("-");
		String dateTime = yearMonth[2];
		String[] date = dateTime.split("T");
		String time = date[1];
		String[] hoursMinSec = time.split(":");

		timeStampHex = decimalToHexConvertion(Integer.parseInt(yearMonth[0]))
				+ decimalToHexConvertion(Integer.parseInt(yearMonth[1]))
				+ decimalToHexConvertion(Integer.parseInt(date[0]))
				+ decimalToHexConvertion(Integer.parseInt(hoursMinSec[0]))
				+ decimalToHexConvertion(Integer.parseInt(hoursMinSec[1]))
				+ decimalToHexConvertion(Integer.parseInt(hoursMinSec[2]))
				+ decimalToHexConvertion(Integer.parseInt(hoursMinSec[3]));
		logger.info("Exit from timeStamptoHexConvertion method.");
		return timeStampHex;
	}

	public String getMeterUnit(String meterUnit) {
		logger.info("Entered in to getMeterUnit method.");
		if ("MILES".equalsIgnoreCase(meterUnit))
			meterUnit = "01";
		else if ("KILOMETERS".equalsIgnoreCase(meterUnit))
			meterUnit = "00";
		else
			meterUnit = "11"; // Invalid Odometer Unit
		logger.info("Exit from getMeterUnit method.");
		return meterUnit;
	}
}