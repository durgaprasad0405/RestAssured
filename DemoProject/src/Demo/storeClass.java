package Demo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class storeClass {
	
	public static void main(String[] args) throws IOException {
		
		RestAssured.baseURI="https://demoqa.com";
		
//		String getPlaceResponse = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", "1e5f9112add1f9875d413a5a3375f653")
//				.when().log().all().get("/maps/api/place/get/json").then().log().all().assertThat().statusCode(200).extract().response().asString();
//				
//				JsonPath js1 =new JsonPath(getPlaceResponse);
//				String actualAddress =js1.getString("address");
//				System.out.println(actualAddress);
		//contentType(ContentType.JSON)
		given().header("Content-Type", "application/json").relaxedHTTPSValidation()
		.body(new String(Files.readAllBytes(Paths.get("C://Users//draghava//Downloads//Payload.json"))))
		.when().post("/Account/v1/User")
		.then().log().all().statusCode(201);
		
		
	}

}
