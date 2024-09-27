import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;

import Pojo.Api;
import Pojo.GetCourses;
import Pojo.WebAutomation;

public class oAuthTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		String[] courseTitle= {"Selenium Webdriver Java", "Cypress", "Protractor"};
//		
//		System.setProperty("webdriver.chrome.driver", "");
		
		String[] courseTitles = {"Selenium Webdriver Java", "Cypress", "Protractor"};
		
		String url="https://rahulshettyacademy.com/getCourse.php?state=durgaprasad&code=4%2F0AeaYSHArmMiwRKGsOaEF7hu06ItW2e8pchNUZh5cPLIPfgz01oGM0EB-SASa2OogFEKAHQ&scope=email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&authuser=0&prompt=none";
		String partialcode=url.split("code=")[1];
		String code=partialcode.split("&scope")[0];
		System.out.println(code);
		
		String accessTokenResponse=given().urlEncodingEnabled(false).log().all()
		.queryParams("code", code)
		.queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
		.queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
		.queryParams("grant_type", "authorization_code")
		.when().log().all()
		.post("https://www.googleapis.com/oauth2/v4/token").asString();
		
		JsonPath js=new JsonPath(accessTokenResponse);
		String accessToken=js.getString("access_token");
		System.out.println(accessToken);
		
		
		GetCourses gc=given().log().all()
		.queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON)
		.when().get("https://rahulshettyacademy.com/getCourse.php").as(GetCourses.class);
		
		System.out.println(gc.getLinkedIn());
		System.out.println(gc.getInstructor());
		System.out.println(gc.getCourses().getApi().get(1).getCourseTitle());

//		System.out.println(gc);
//		List<Api> apicourses = gc.getCourses().getApi();
//		for (int i = 0; i <apicourses.size(); i++) {
//			if (apicourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")) {
//				System.out.println(apicourses.get(i).getPrice());
//			}
//		}
//		
//		//Get the all courses titles in the webAutomation
//		ArrayList<String> al = new ArrayList<String>();
//		List<WebAutomation> wa = gc.getCourses().getWebAutomation();
//		for (int j = 0; j < wa.size(); j++) {
//			al.add(wa.get(j).getCourseTitle());
//		}
//		List<String> expectedList= Arrays.asList(courseTitles);
//		
//		Assert.assertTrue(al.equals(expectedList));
	}

}
