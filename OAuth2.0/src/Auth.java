
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
 
import static io.restassured.RestAssured.given;

import Pojo.GetCourses;
 
public class Auth {
 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RestAssured.baseURI="https://rahulshettyacademy.com";
		
		String response =given().relaxedHTTPSValidation().formParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com" )
		       .formParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
		       .formParams("grant_type", "client_credentials")
		       .formParams("scope", "trust").log().all()
		       
		.when().post("/oauthapi/oauth2/resourceOwner/token").then().log().all().assertThat().statusCode(200).extract().asString();
		
		System.out.println(response);
		
		JsonPath js = new JsonPath(response);
		String token = js.get("access_token");
		 System.out.println(token);
		
 
//---------------------------------------------------------------------------------Access Token---------------------------------------------------------------------------
	
		
		 GetCourses gc =given().relaxedHTTPSValidation().queryParam("access_token", token).
		 when().log().all().get("/oauthapi/getCourseDetails").as(GetCourses.class);
				// when().log().all().get("/oauthapi/getCourseDetails").asString();
				
		 //System.out.println(response2);
		
	 System.out.println(gc.getInstructor());
//
	 System.out.println(gc.getLinkedIn());
 
		
		
		
 
	}
 
}