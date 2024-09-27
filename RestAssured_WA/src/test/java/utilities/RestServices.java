package utilities;


import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.*;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;


import io.restassured.RestAssured;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestServices extends Global {
	//BusinessActions businessActions = new BusinessActions();

	
	
	@Test
	public Response postMethod(String baseURI, String resourcePath, String contentType, String userName,
			String password, String inputPayload) {
		logger.info("Entered in to postMethod method.");
		// BaseURL
		RestAssured.baseURI = baseURI;
		// Request
		RequestSpecification httpRequest = RestAssured.given().relaxedHTTPSValidation();
		// ContentType
		httpRequest.contentType(contentType);
		//httpRequest.auth().basic(userName, password);
		//httpRequest.auth().preemptive().basic(userName, password);
		httpRequest.body(inputPayload);
		// Response
		Response response = httpRequest.post(resourcePath);
		getStatusLine(response);
		logger.info("Exit from postMethod method.");
		return response;
	}
	
	@Test
	public Response putMethod(String baseURI, String resourcePath, String contentType, String userName,
			String password, String inputPayload) {
		
		
		logger.info("Entered in to putMethod method.");
		// BaseURL
		RestAssured.baseURI = baseURI;
		// Request
		RequestSpecification httpRequest = RestAssured.given().relaxedHTTPSValidation();
		// ContentType
		httpRequest.contentType(contentType);
		//httpRequest.auth().basic(userName, password);
		//httpRequest.auth().preemptive().basic(userName, password);
		httpRequest.body(inputPayload);
		// Response
		Response response = httpRequest.put(resourcePath);
		getStatusLine(response);
		logger.info("Exit from putMethod method.");
		return response;
	}
	
	@Test
	public Response deleteMethod(String baseURI, String resourcePath, String contentType, String userName,
			String password, String inputPayload) {
		
		
		logger.info("Entered in to deleteMethod method.");
		// BaseURL
		RestAssured.baseURI = baseURI;
		// Request
		RequestSpecification httpRequest = RestAssured.given().relaxedHTTPSValidation();
		// ContentType
		httpRequest.contentType(contentType);
		//httpRequest.auth().basic(userName, password);
		//httpRequest.auth().preemptive().basic(userName, password);
		httpRequest.body(inputPayload);
		// Responses
		Response response = httpRequest.delete(resourcePath);
		getStatusLine(response);
		logger.info("Exit from deleteMethod method.");
		return response;
	}
	
	@Test
	public Response getMethod(String baseURI, String resourcePath, String contentType, String userName,
			String password, String inputPayload) {
		
		
		logger.info("Entered in to getMethod method.");
		// BaseURL
		RestAssured.baseURI = baseURI;
		// Request
		RequestSpecification httpRequest = RestAssured.given().relaxedHTTPSValidation();
		// ContentType
		httpRequest.contentType(contentType);
		//httpRequest.auth().basic(userName, password);
		//httpRequest.auth().preemptive().basic(userName, password);
		//httpRequest.body(inputPayload);
		// Response
		Response response = httpRequest.get(resourcePath);
		getStatusLine(response);
		logger.info("Exit from getMethod method.");
		return response;
	}
	


	@AfterTest
	public void responseCodeValidation(Response response) {
		logger.info("Entered in to responseCodeValidation method.");
		String expectedResponseCode="";
		int actualResponseCode;
		expectedResponseCode = inputPayloadElementsMap.get(expectedHttpResponseCode);
		actualResponseCode = response.getStatusCode();

		if (StringUtils.isEmpty(expectedResponseCode)) {
			failureReason += "Expected_API_HttpResponse code is null OR Element not available in test data.";
		} else if (StringUtils.isEmpty(Integer.toString(actualResponseCode))) {
			failureReason += "Actual API HTTP Response code is null.";
		} else if (!StringUtils.isEmpty(Integer.toString(actualResponseCode))
				&& !StringUtils.isEmpty(expectedResponseCode)) {
			//expectedResponseCode = Integer.parseInt(inputPayloadElementsMap.get(expectedHttpResponseCode));
			if (Integer.parseInt(expectedResponseCode) != actualResponseCode)
				failureReason += "Received incorrect API HttpResponse code. Exp Response Code: " + expectedResponseCode
						+ ", Actual Response Code: " + actualResponseCode + ".";
		}
	}
	
	@AfterTest
	public void responseMessageValidation(Response response) {
		logger.info("Entered in to responsemsgValidation method.");
		String expectedResponseMsg="";
		String actualResponseMsg;
		expectedResponseMsg = inputPayloadElementsMap.get(expectedHttpResponseMsg);
		actualResponseMsg = response.getBody().asString();
		System.out.println(actualResponseMsg);

		if (StringUtils.isEmpty(expectedHttpResponseMsg)) {
			failureReason += "Expected_HTTP_Response Message is null OR Element not available in test data.";
		} else if (StringUtils.isEmpty(actualResponseMsg)){
			failureReason += "Actual HTTP Response code is null.";
		} else if (!StringUtils.isEmpty((actualResponseMsg)) 
				&& !StringUtils.isEmpty(expectedResponseMsg)) {
			if (!StringUtils.equalsIgnoreCase(expectedResponseMsg, actualResponseMsg))
				failureReason += "Received incorrect HttpResponse Msg. Exp Response Msg: " + expectedResponseMsg
						+ ", Actual Response Msg: " + actualResponseMsg + ".";
		}

	}

	public long responseTime(Response response) {
		long responseTimeOfOSB1 = 0;
		// long rs1 =0;
		responseTimeOfOSB1 = response.getTime();
		System.out.println(responseTimeOfOSB1);
		return responseTimeOfOSB1;
	}
	
	public static void getResponseTime(Response response) {
		System.out.println("The Response time is "+response.timeIn(TimeUnit.MILLISECONDS));
		System.out.println("The Response time is "+response.timeIn(TimeUnit.SECONDS));
	}
	public static void getResponseCode(Response response) {
		System.out.println("The Response code is "+response.getStatusCode());
	}
	
	public static void getResponseContentType(Response response){
        String contentType=null;
        contentType=response.contentType();
		   System.out.println("The content type of response "+contentType);
		}
	public static void getHeader(Response response){
		System.out.println("Response header is :- "+response.headers());
	}
	public static void getStatusLine (Response response) {
		System.out.println("The Response code is "+response.getStatusCode());
		System.out.println("The Response time is "+response.timeIn(TimeUnit.MILLISECONDS));
		System.out.println("The Response time is "+response.timeIn(TimeUnit.SECONDS));
		System.out.println(response.statusLine());
		System.out.println(response.getContentType());
		System.out.println("The content type of response "+response.contentType());
		System.out.println("Response header is :- "+response.headers());
		System.out.println(response.sessionId());
		System.out.println(response.getStatusLine());
		System.out.println(response.getBody().asString());
		System.out.println(response.body().asString());
		System.out.println(response.xmlPath().toString());
		
	}
	
	
}