package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang3.StringUtils;

public class DataBaseOps extends Global {

	CommonUtilities commonUtils = new CommonUtilities();
	ExcelOps excelOps = new ExcelOps();

	public Connection getConnection2() {
		logger.info("Entered in to (DB) getConnection method.");
		Connection connection = null;
		String dbServiceName = null;
		String host = null;
		String port = null;
		String username = null;
		String password = null;
		try {
			dbServiceName = commonUtils.getProperties(environment + ".serviceName");
			host = commonUtils.getProperties(environment + ".host");
			port = commonUtils.getProperties(environment + ".port");
			username = commonUtils.getProperties(environment + ".username");
			password = commonUtils.getProperties(environment + ".password");

			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection(
					"jdbc:oracle:thin:@//" + host + ":" + port + "/" + dbServiceName + "", username, password);
		} catch (Exception e) {
			failureReason = "Error in connecting to the Database";
			logger.error("Error in (DB) getConnection method: " + e.getMessage());
			logger.error("Trace: " + e);
			logger.error("Error in (DB) getConnection method. Please check credetails.");
			System.exit(0);
		}
		if (connection == null) {
			failureReason += "Error in connecting to the Database";
			logger.error(failureReason + "in " + serviceName + " method. Hence exiting from execution.");
			System.exit(0);
		}
		logger.info("Exit from (DB) getConnection method.");
		return connection;
	}

	public Connection getConnection(String dbName) {
		logger.info("Entered in to (DB) getConnection method.");
		Connection connection = null;
		String dbServiceName = null;
		String host = null;
		String port = null;
		String username = null;
		String password = null;
		try {
			dbServiceName = commonUtils.getProperties(environment + "." + dbName + ".serviceName");
			host = commonUtils.getProperties(environment + "." + dbName + ".host");
			port = commonUtils.getProperties(environment + "." + dbName + ".port");
			username = commonUtils.getProperties(environment + "." + dbName + ".username");
			password = commonUtils.getProperties(environment + "." + dbName + ".password");

			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection(
					"jdbc:oracle:thin:@//" + host + ":" + port + "/" + dbServiceName + "", username, password);
		} catch (Exception e) {
			failureReason = "Error in connecting to the Database";
			logger.error("Error in (DB) getConnection method: " + e.getMessage());
			logger.error("Trace: " + e);
			System.exit(0);
		}
		if (connection == null) {
			failureReason += "Error in connecting to the " + dbName + " Database";
			logger.error(failureReason + "in " + serviceName + " method. Hence exiting from execution.");
			System.exit(0);
		}
		logger.info("Exit from (DB) getConnection method.");
		return connection;
	}

	public void executeQuery(Connection connection, String sqlQuery) throws SQLException {
		logger.info("Entered in to executeQuery method.");
		Statement statement = null;
		dbResultSet = null;
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			dbResultSet = statement.executeQuery(sqlQuery);
		} catch (SQLException e) {
			logger.error("Error in executeQuery method: " + e.getMessage());
			logger.info("Resultset might be null. Please verify the sql query again. SQL Query is:-->" + sqlQuery);
			logger.error("Trace: " + e);
			if (dbResultSet == null) {
				System.exit(0);
			}
		}
		logger.info("Exit from executeQuery method.");
		// return dbResultSet;
	}

	public String getSQLColumnValueByQuery(String sqlQuery) throws SQLException {
		logger.info("Entered in to getSQLColumnValue method.");
		String columnValue = null;
		// ResultSet resultSet = null;
		try {
			executeQuery(apiDBConnection, sqlQuery);
			while (dbResultSet.next()) {
				columnValue = dbResultSet.getString(1);
			}
		} catch (Exception e) {
			logger.error("Error in getSQLColumnValue method: " + e.getMessage());
			logger.info("Query when moved to catch section:- " + sqlQuery);
			logger.error("Trace: " + e);
		}
		logger.info("Exit from getSQLColumnValue method.");
		return columnValue;
	}

	public String getSQLColumnValueByTable(String sqlTable) throws SQLException {
		logger.info("Entered in to getSQLColumnValue method.");

		String sqlQuery, sqlCondition, sqlMatch = null, columnValue = null;
		int sqlRowNo = 1, numberOfSqls;
		// ResultSet rs = null;
		numberOfSqls = excelOps.getLastRowCountatSpecificColumn(sqlSheet, "SQL Queries");

		for (int sqlNumber = sqlRowNo; sqlNumber <= numberOfSqls; sqlNumber++) {
			String tableInsqlSheet = excelOps.getCellData(sqlSheet, sqlNumber, "Tables");
			if (sqlTable.equalsIgnoreCase(tableInsqlSheet)) {
				sqlMatch = "Found the req table";
				sqlQuery = excelOps.getCellData(sqlSheet, sqlNumber, "SQL Queries");
				sqlTable = excelOps.getCellData(sqlSheet, sqlNumber, "Tables");
				sqlCondition = excelOps.getCellData(sqlSheet, sqlNumber, "SQLFilterCondition");
				sqlQuery = buildSqlQuery(sqlQuery, sqlCondition);
				try {
					executeQuery(apiDBConnection, sqlQuery);
					dbResultSet.beforeFirst();
					while (dbResultSet.next()) {
						columnValue = dbResultSet.getString(1);
					}
				} catch (Exception e) {
					failureReason += "Exception in getSQLColumnValue. Reason:- " + e.getMessage();
					logger.error("Exception in getSQLColumnValue. Reason:- " + e);
				}
			}
		} // End of sqlNumber for loop
		if (sqlMatch == null)
			failureReason = "No matching sqlQuery was found in test data sql sheet";
		logger.info("Exit from getSQLColumnValue method.");
		return columnValue;
	}

	public String buildSqlQuery(String sqlQuery, String sqlCondition) {
		logger.info("Entered in to buildSqlQuery method.");
		int counter = sqlCondition.split("\\,", -1).length - 1;
		String eventCondition = null;
		String condition = null;
		for (int c = 0; c <= counter; c++) {
			condition = sqlCondition.split(",")[c];
			if (!StringUtils.isEmpty(condition))
				eventCondition = inputPayloadElementsMap.get(condition);
			if (eventCondition != null) {
				// This is to handle boolean values.
				if (eventCondition.equals("true"))
					eventCondition = "Y";
				else if (eventCondition.equals("false"))
					eventCondition = "N";
				sqlQuery = sqlQuery.replace(condition, eventCondition);
			}
		}
		if (sqlQuery.contains("payload_event_id") && event_id != null) {
			sqlQuery = sqlQuery.replace("payload_event_id", event_id);
		}
		logger.info("Exit from buildSqlQuery method.");
		return sqlQuery;

	}

	public String getPrimaryKeyOfTable(String sqlTable) {
		logger.info("Entered in to recCountValidation method.");
		ResultSet primaryKey = null;
		String primaryKeyColumnName = null;
		try {
			primaryKey = apiDBConnection.getMetaData().getPrimaryKeys(null, null, sqlTable);
			while (primaryKey.next()) {
				primaryKeyColumnName = primaryKey.getString("COLUMN_NAME");
			}
		} catch (SQLException e) {
			logger.error("Error in getPrimaryKeyOfTable method: " + e.getMessage());
			logger.error("Trace: " + e);
		}
		logger.info("Exit from recCountValidation method.");
		return primaryKeyColumnName;
	}

	public String recCountValidation(String expRecCount) {
		logger.info("Entered in to recCountValidation method.");
		int actRecCount = 0;
		String result = "";
		try {
			while (dbResultSet.next()) {
				actRecCount++;
			}
			dbResultSet.beforeFirst();
			if (!expRecCount.equals(Integer.toString(actRecCount)))
				result = ":-Record count mismatch in table. ExpRecCount:-" + expRecCount + ". ActRecCount:-"
						+ actRecCount + ".";
		} catch (SQLException e) {
			logger.error("Error in recCountValidation method: " + e.getMessage());
			logger.error("Trace: " + e);
		}
		logger.info("Exit from recCountValidation method.");
		return result;
	}

	public String dataValidationInDB(String sqlTable) {
		logger.info("Entered in to dataValidationInDB method.");
		int colCount = 0;
		String colName = null;
		String expValue = null;
		String actValue = null;
		String dbErrorReason = "";
		String expdbValue=null;
		try {
			colCount = dbResultSet.getMetaData().getColumnCount();
			dbResultSet.beforeFirst();

			while (dbResultSet.next()) {
				for (int columnNum = 1; columnNum <= colCount; columnNum++) {
					colName = dbResultSet.getMetaData().getColumnName(columnNum);
					actValue = dbResultSet.getString(columnNum);
					expValue = inputPayloadElementsMap.get(columnPrefix + colName.toLowerCase());
					
					//Below is temporary fix.We are using statusCodeColumn as "Expected_HTTP_RESPONSE"
					//This issue might be introduced in recent changes. need to fix.
					if (StringUtils.equalsIgnoreCase("HTTP_RESPONSE_CODE", colName))
						//expValue=inputPayloadElementsMap.get(expectedHttpResponseCode);

//					if (StringUtils.equalsIgnoreCase("HTTP_RESPONSE_CODE", colName))
					expValue=inputPayloadElementsMap.get(ExpectedDBHTTPRESPONSE);

					
					
					if (!StringUtils.isEmpty(expValue)) {	
						if (colName.equalsIgnoreCase("TELEMATICS_FLAG") || colName.equalsIgnoreCase("TURBO")
								|| colName.equalsIgnoreCase("SUBSCRIPTION_FLAG")
								|| colName.equalsIgnoreCase("IS_FOTA") || colName.equalsIgnoreCase("IS_FOTA_FOR_DCM")
								 || colName.equalsIgnoreCase("IS_FOTA_FOR_THIS_DEVICE")) {
							if (expValue.equalsIgnoreCase("0") || expValue.equalsIgnoreCase("false")
									|| expValue.equalsIgnoreCase("FALSE")) {
								expValue = "N";
							} else if (expValue.equalsIgnoreCase("1") || expValue.equalsIgnoreCase("true")
									|| expValue.equalsIgnoreCase("TRUE")) {
								expValue = "Y";
							}
						} else if(colName.equalsIgnoreCase("TOTAL_DTC_COUNT")) {
							
						}  
						try {
							if (!expValue.equals(actValue)) {
								dbErrorReason += "Table Name: " + sqlTable + ":- Input value of " + colName
										+ " is NOT matched with the value in DB. Expected Value:" + expValue
										+ "; Actual Value:" + actValue + ";";
							}
						} catch (Exception e1) {
							logger.error("Exception when expected value is not matched in dataValidationInDB method: "
									+ e1.getMessage());
							logger.error("Trace: " + e1);
						}
					}
				}
			}

		} catch (SQLException e) {
			logger.error("Error in dataValidationInDB method: " + e.getMessage());
			logger.error("Trace: " + e);
		}
		logger.info("Exit from dataValidationInDB method.");
		return dbErrorReason;
	}

// Execute sql query, verify record count and validate data based on input payload for a specific table.
	public void validateSQLQuery(String sqlTable) throws SQLException {

		String sqlQuery, sqlCondition, recCountResult, expRecCount = "";
		// ResultSet rs = null;
		for (int sqlNum = 1; sqlNum <= sqlSheet.getLastRowNum(); sqlNum++) {
			if (sqlTable.equalsIgnoreCase(excelOps.getCellData(sqlSheet, sqlNum, "Tables"))) {
				sqlQuery = excelOps.getCellData(sqlSheet, sqlNum, "SQL Queries");
				sqlCondition = excelOps.getCellData(sqlSheet, sqlNum, "SQLFilterCondition");
				expRecCount = inputPayloadElementsMap.get(sqlTable);
				sqlQuery = buildSqlQuery(sqlQuery, sqlCondition);
				System.out.println(sqlQuery);
				executeQuery(apiDBConnection, sqlQuery);
				dbResultSet.beforeFirst();
				System.out.println("SQL Query is:" + sqlQuery);
				recCountResult = recCountValidation(expRecCount);
				if (!recCountResult.isEmpty()) {
					failureReason += sqlTable + recCountResult + "QueryCondition:" + sqlQuery;
				} else {
					failureReason += dataValidationInDB(sqlTable);
				}
			}
		}
	}

// Execute sql query for a specific table and return dbresult (dbresult is defined Globally)
	public void executeQueryBySpecificSQLTable(String sqlTable, Connection connection) {

		String sqlQuery, sqlCondition, sqlMatch = null;
		int sqlRowNo = 1, numberOfSqls;
		numberOfSqls = excelOps.getLastRowCountatSpecificColumn(sqlSheet, "SQL Queries");

		for (int sqlNumber = sqlRowNo; sqlNumber <= numberOfSqls; sqlNumber++) {
			String tableInsqlSheet = excelOps.getCellData(sqlSheet, sqlNumber, "Tables");
			if (sqlTable.equalsIgnoreCase(tableInsqlSheet)) {
				sqlMatch = "Found the req table";
				sqlQuery = excelOps.getCellData(sqlSheet, sqlNumber, "SQL Queries");
				sqlTable = excelOps.getCellData(sqlSheet, sqlNumber, "Tables");
				sqlCondition = excelOps.getCellData(sqlSheet, sqlNumber, "SQLFilterCondition");
				sqlQuery = buildSqlQuery(sqlQuery, sqlCondition);
				try {
					executeQuery(connection, sqlQuery);
					dbResultSet.beforeFirst();
				} catch (Exception e) {
					failureReason += "Exception in initiateSQLQueryExecution. Reason:- " + e.getMessage();
					logger.error("Exception in initiateSQLQueryExecution. Reason:- " + e);
				}
			break;
			}
		} // End of sqlNumber for loop
		if (sqlMatch == null)
			failureReason = "No matching sqlQuery was found in test data sql sheet";
	}

// Execute sql query, verify record count and validate data based on input payload for a all queried given in sql sheet..
	public void validateDBResult() {

		String sqlQuery, sqlCondition, expRecCount, queryList;
		String sqlTable = null, recCountResult = "";
		int sqlRowNo = 1, numberOfSqls, queryCounter = 0;

		queryList = inputPayloadElementsMap.get(queryNoPrefix);
		if (!StringUtils.isEmpty(queryList)) {
			queryCounter = queryList.split("\\,", -1).length - 1;
			numberOfSqls = excelOps.getLastRowCountatSpecificColumn(sqlSheet, "SQL Queries");

			for (int q = 0; q <= queryCounter; q++) {
				String testDataSheetQueryNo = queryList.split(",")[q];
				sqlTable = null;
				for (int sqlNumber = sqlRowNo; sqlNumber <= numberOfSqls; sqlNumber++) {
					String sqlSheetQueryNo = excelOps.getCellData(sqlSheet, sqlNumber, queryNoPrefix);
					if (testDataSheetQueryNo.equalsIgnoreCase(sqlSheetQueryNo)) {
						sqlQuery = excelOps.getCellData(sqlSheet, sqlNumber, "SQL Queries");
						sqlTable = excelOps.getCellData(sqlSheet, sqlNumber, "Tables");
						sqlCondition = excelOps.getCellData(sqlSheet, sqlNumber, "SQLFilterCondition");
						expRecCount = inputPayloadElementsMap.get(sqlTable);
						sqlQuery = buildSqlQuery(sqlQuery, sqlCondition);
						System.out.println("SQL Query is: " + sqlQuery);

						try {
							Thread.sleep(2000);
							executeQuery(apiDBConnection, sqlQuery);
							dbResultSet.beforeFirst();
						} catch (Exception e) {
							failureReason += "Exception in initiateSQLQueryExecution. Reason:- " + e.getMessage();
							logger.error("Exception in initiateSQLQueryExecution. Reason:- " + e);
						}
						if (StringUtils.isEmpty(expRecCount))
							failureReason += "Expected Record count in the" + sqlTable
									+ "table is not provided in the test data sheet.";
						else {
							recCountResult = recCountValidation(expRecCount);
							if (!recCountResult.isEmpty())
								failureReason += sqlTable + recCountResult;
							else
								failureReason += dataValidationInDB(sqlTable);
						}
					}
				} // End of sqlNumber for loop
				if (sqlTable == null)
					failureReason = "No matching sqlQuery was found in test data sql sheet";
			} // End of query list rotation for loop
		} else {
			failureReason += "SQL Query list is empty in Test Data Sheet!!!";
		}
	}
}