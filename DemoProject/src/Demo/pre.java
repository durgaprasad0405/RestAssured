package Demo;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;

public class pre {
	
	public static void main(String[] args) {
		
		RestAssured.baseURI= "https://rahulshettyacademy.com";
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json").body("{\r\n"
				+ "  \"location\": {\r\n"
				+ "    \"lat\": -38.383494,\r\n"
				+ "    \"lng\": 33.427362\r\n"
				+ "  },\r\n"
				+ "  \"accuracy\": 50,\r\n"
				+ "  \"name\": \"Frontline house\",\r\n"
				+ "  \"phone_number\": \"(+91) 983 893 3937\",\r\n"
				+ "  \"address\": \"29, side layout, cohen 09\",\r\n"
				+ "  \"types\": [\r\n"
				+ "    \"shoe park\",\r\n"
				+ "    \"shop\"\r\n"
				+ "  ],\r\n"
				+ "  \"website\": \"http://google.com\",\r\n"
				+ "  \"language\": \"French-IN\"\r\n"
				+ "}\r\n"
				+ "")
		.when().post("/maps/api/place/add/json")
		 .then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP")).header("Server", "Apache/2.4.52 (Ubuntu)").extract().response().asString();
 
//		System.out.println(response);
//	   
//		JsonPath js = new JsonPath(response);
//		String placeid = js.getString("place_id");
//		
//		System.out.println(placeid);
//		
//		
//		// Update the Address PUT Method
//		String UpdatedAddress = "80 Summer walk, USA";
//		
//		given().queryParam("Key", "qaclick123").header("Content-Type", "application/json").body("{\r\n"
//				+ "\"place_id\":\""+placeid+"\",\r\n"
//				+ "\"address\":\""+UpdatedAddress+"\",\r\n"
//				+ "\"key\":\"qaclick123\"\r\n"
//				+ "}\r\n"
//				+ "")
//		.when().put("/maps/api/place/update/json").then().log().all().assertThat().statusCode(200).body("msg",equalTo("Address successfully updated"));
//		
//		
//		
//		
//		String getPlaceResponse = given().log().all().queryParam("Key", "qaclick123").queryParam("place_id", placeid)
//		.when().log().all().get("/maps/api/place/get/json").then().log().all().assertThat().statusCode(200).extract().response().asString();
//		
//		JsonPath js1 =new JsonPath(getPlaceResponse);
//		String actualAddress =js1.getString("address");
//		System.out.println(actualAddress);
		
	}

}
