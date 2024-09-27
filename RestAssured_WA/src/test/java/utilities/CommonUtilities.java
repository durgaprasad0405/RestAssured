package utilities;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasXPath;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.xml.sax.SAXException;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.ByNameAndTextRecSelector;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;
import org.xmlunit.diff.ElementSelector;
import org.xmlunit.diff.ElementSelectors;

import com.google.gson.Gson;

import driver.DriverScript;
import io.restassured.RestAssured;
import io.restassured.authentication.AuthenticationScheme;
import io.restassured.authentication.CertificateAuthSettings;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.authentication.OAuthSignature;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class CommonUtilities extends Global {

	public static void setup() {
		System.out.println("Entered in to setup method");
		logger = Logger.getLogger("WebServicesLog");
		PropertyConfigurator.configure("Log4j.properties");
		logger.setLevel(Level.DEBUG);
	}

	public void createFolder(String newFolder) {
		logger.info("Entered in to createFolder method.");
		File directory = new File(String.valueOf(newFolder));
		if (!directory.exists()) {
			directory.mkdir();
			logger.info("Created a folder. Folder name:" + newFolder);
		}
		logger.info("Exit from createFolder method.");
	}

	public static void serviceRedirection() {// throws NoSuchMethodException, SecurityException, ClassNotFoundException
												// {
		logger.info("Entered in to serviceRedirection method.");
		ClassLoader classLoader = DriverScript.class.getClassLoader();
		// Object instance;
		// Method method;
		try {
			Class<?> type = classLoader.loadClass("testScripts." + serviceName);
			type.getConstructor().newInstance();
			/* If calling method is not constructor then we have to use below */
			// instance = type.getConstructor().newInstance();
			// method = type.getMethod(serviceName);
			// method.invoke(instance);
		} catch (Exception e) {
			logger.error("Error in serviceRedirection method: " + e.getMessage());
			logger.error("Trace: " + e);
		}
		logger.info("Exit from serviceRedirection method.");
	}

	public String getProperties(String key) {
		logger.info("Entered in to getProperties method.");
		Properties properties = new Properties();
		FileInputStream fso;
		String propertyValue = null;
		try {
			fso = new FileInputStream(propertyFile);
			properties.load(fso);
			propertyValue = properties.getProperty(key);
		} catch (Exception e) {
			logger.error("Error in getProperties method: " + e.getMessage());
			logger.error("Trace: " + e);
		}
		logger.info("Exit from getProperties method.");
		return propertyValue;
	}

	@SuppressWarnings("resource")
	public String textFileRead(String fileLocation, String fileName) throws IOException {
		logger.info("Entered in to textFileRead method.");
		StringBuilder sb = null;
		InputStream inputFile;
		try {
			inputFile = new FileInputStream(fileLocation + "/" + fileName + ".txt");
			BufferedReader readTextFile = new BufferedReader(new InputStreamReader(inputFile));
			String line = readTextFile.readLine();
			sb = new StringBuilder();
			while (line != null) {
				sb.append(line).append("\n");
				line = readTextFile.readLine();
			}
		} catch (Exception e) {
			logger.error("Error in textFileRead method: " + e.getMessage());
			logger.error("Trace: " + e);
		}
		logger.info("Exit from textFileRead method.");
		return sb.toString();
	}

	public String getFileNameFromPath(String FilePath) {
		Path p = Paths.get(FilePath);
		String fileName = p.getFileName().toString();
		return fileName;
	}

	public String convertDate(String inputDate) throws ParseException {
		logger.info("Entered in to convertDate method.");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
		String formattedTime = outputFormat.format(simpleDateFormat.parse(inputDate));
		logger.info("Exit from convertDate method.");
		return formattedTime;
	}

	public String dateWithMilliSeconds(Date systemDate) throws ParseException {
		logger.info("Entered in to dateWithMilliSeconds method.");
		SimpleDateFormat dateFormatWithMilliSeconds = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");
		String dateWithMilliSeconds = dateFormatWithMilliSeconds.format(systemDate);
		logger.info("Exit from dateWithMilliSeconds method.");
		return dateWithMilliSeconds;
	}

	public String dateTimeWithT() throws ParseException {
		logger.info("Entered in to dateWithMilliSeconds method.");
		Date systemDate = new Date();
		SimpleDateFormat dateFormatWithMilliSeconds = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		String dateWithMilliSeconds = dateFormatWithMilliSeconds.format(systemDate);
		logger.info("Exit from dateWithMilliSeconds method.");
		return dateWithMilliSeconds;
	}

	public int getRandomNumber1() throws Exception {
		logger.info("Entered in to getRandomNumber method.");
		Random rnd = new Random();
		int randomNumber = 100000 + rnd.nextInt(900000);
		logger.info("Exit from getRandomNumber method.");
		return randomNumber;
	}

	public String getDateFormat(String dateFormat) {
		logger.info("Entered in to getDateFormat method.");
		SimpleDateFormat dateFormatWithSeconds = new SimpleDateFormat(dateFormat);
		Date fileCreateDate = new Date();
		String fileCreationDate = dateFormatWithSeconds.format(fileCreateDate);
		logger.info("Exit from getDateFormat method.");
		return fileCreationDate;
	}

	public void compareXML(String outputXML, String actualResultXML, String parentElementTagName)
			throws SAXException, IOException {

		try {
			ElementSelector es = ElementSelectors.conditionalBuilder().whenElementIsNamed(parentElementTagName)
					.thenUse(new ByNameAndTextRecSelector()).elseUse(ElementSelectors.byNameAndText).build();
			Diff myDiff = DiffBuilder.compare(outputXML).withTest(actualResultXML)
					.withNodeMatcher(new DefaultNodeMatcher(es)).ignoreWhitespace().ignoreComments().checkForSimilar()
					.build();
			Iterable<Difference> itearates = myDiff.getDifferences();
			for (Difference difference : itearates) {
				System.out.println("DIF---->" + difference);
				failureReason += difference.toString();
			}
		} catch (AssertionError e) {
			// TODO Auto-generated catch block
			failureReason += e.toString();
			System.out.println("test  " + e.toString());
		}
	}

	public String[] appendStringToStringArray(String[] stringArray, String newString) {
		int N = stringArray.length;
		stringArray = Arrays.copyOf(stringArray, N + 1);
		stringArray[N] = newString;
		return stringArray;
	}
	
	public HashMap<String, Object>  jsonTOMap (String resp){

	//  create Gson object
	    Gson gson = new Gson();
	    //read JSON to a java HashMap
	    HashMap<String, Object> mapLocation = gson.fromJson(resp, HashMap.class);
		return mapLocation;
	}
	
	public static void validateGetMethByJsonPath(Response response, String inputVal,String contentType, String resourcePath, String jsonPath, String jsonValue) {

		 response = given().log().all().contentType(contentType)
					.when().get(resourcePath)
					.then().assertThat().log().all()
					.body(jsonPath, equalTo(jsonValue)).extract().response();
	}
	
	public static void validatePutMethByJsonPath(Response response,String inputPayload, String inputVal,String contentType, String resourcePath, String jsonPath, String jsonValue) {

		 response = given().log().all().contentType(contentType).body(inputPayload)
					.when().put(resourcePath)
					.then().assertThat().log().all()
					.body(jsonPath, equalTo(jsonValue)).extract().response();
	}
	
	public static void validateDelMethByJsonPath(Response response,String inputPayload, String inputVal,String contentType, String resourcePath, String jsonPath, String jsonValue) {

		 response = given().log().all().contentType(contentType).body(inputPayload)
					.when().delete(resourcePath)
					.then().assertThat().log().all()
					.body(jsonPath, equalTo(jsonValue)).extract().response();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void validationByXmlPath(String baseURI, String inputPayload, String xmlPath, String xmlValue) {
		HashMap headermap = new HashMap<Object, Object>();
		//headermap.put("Content-Type", "application/soap+xml; charset=utf-8");
		//headermap.put("Content-Type", "text/xml");
		headermap.put("Content-Type", "text/xml; charset=UTF-8");

		 RestAssured.given().log().all()
				.headers(headermap).
				body(inputPayload).
				post(baseURI)
				.then().assertThat().body(hasXPath(xmlPath, containsString(xmlValue)));
		
	}
	
//	public static void validationByXsd(Response response, String contentType, String resourcePath) {
//		 response = given().log().all().contentType(contentType)
//					.when().get(resourcePath).then().assertThat().body(matchesXsd(xsd));
//	}
	
	
	
	public static void fullBodyMatching(String contentType) {
		given().log().all().contentType(contentType)
		.when().get(resourcePath).then().assertThat().body(equalTo("something"));
	}
	
	@SuppressWarnings("deprecation")
	public static void parseXmlData(String response) {
		
		 XmlPath xmlPath = new XmlPath(response);
         String value = xmlPath.get("value");
         
	}
	
	public static void parseJsonData(String response) {
		
		JsonPath jsonPath = new JsonPath(response);
        int value = jsonPath.getInt("value");
        
	}
	
//	public static void basicAuth(String resourcePath) {
//		 given().auth().basic("username", "password").when().get(resourcePath).then().statusCode(200);
//	}
//	
//	public static void preemptiveAuth(String resourcePath) {
//	     given().auth().preemptive().basic("username", "password")
//	     .when().get(resourcePath);
//	}
	
	public static Response oauth(String resourcePath,String consumerKey, String consumerSecret,String accessToken, String secretToken,
            OAuthSignature signature) {
		Response response = given()
			    .auth()
			    .oauth(consumerKey, consumerSecret, accessToken, secretToken)
			    .when().get(resourcePath);
		return response
		;
	}
	
	public static ValidatableResponse ntlm(ValidatableResponse response, String resourcePath,String userName,
            String password,
            String workstation,
            String domain) {
				
			response= 	given().auth().ntlm(userName, password, workstation, domain).when().get(resourcePath).then().statusCode(200);
			return response;
	
	}
	
	public static ValidatableResponse form(ValidatableResponse response,String userName,
            String password,
            FormAuthConfig config) {
		response=given().auth().form(userName, password).when().get(resourcePath).then().statusCode(200);
		return response;
		
	}
	
	public static AuthenticationScheme certificate(String trustStorePath,
            String trustStorePassword,
            String keyStorePath,
            String keyStorePassword,
            CertificateAuthSettings certificateAuthSettings) {
				return null;
		
	}
	
	public static Response digest(Response response,String resourcePath,String userName,
            String password) {
		response= given()
			    .auth()
			    .digest(userName, password)
			    .when().get(resourcePath);
				return response;
					    	
	}
	
	public static Response oauth2(Response response,String resourcePath,String accessToken,
            OAuthSignature signature) {
		response= given()
	    .auth()
	    .oauth2(accessToken)
	    .when().get(resourcePath);
		return response;
		
	}
	
	
	
	
	


}
