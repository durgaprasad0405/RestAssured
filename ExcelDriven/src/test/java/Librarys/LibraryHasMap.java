package Librarys;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;



public class LibraryHasMap {
	
	@Test
	public void name() throws IOException {
		
		
		HashMap<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("name", "Rest Assured");
		jsonMap.put("isbn", "Rm4567");
		jsonMap.put("aisle", "224");
		jsonMap.put("author", "Prasad");
		
		System.out.println("******************Post Request**********************");
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		Response postResponse = given().log().all()
				.header("Content-Type", "application/json")
				.body(jsonMap)
				.when().post("/Library/Addbook.php")
				.then().log().all().assertThat().statusCode(200).extract().response();
		
//		String postRes = postResponse.asString();
		JsonPath js = ReusableMethod.rawToJson(postResponse);
		String id = js.get("ID");
		System.out.println(id);
		
		System.out.println("******************Get Request**********************");
		
		Response getResponse = given().log().all()
				.queryParam("id", id)
				.when().get("/Library/GetBook.php")
				.then().log().all().assertThat().statusCode(200).extract().response();
//		String getRes = getResponse.asString();
		JsonPath js1 = ReusableMethod.rawToJson(getResponse);
		System.out.println(js1);
		
		System.out.println("******************Delete Request**********************");
		
		Response deleteResponse = given().log().all()
				.header("Content-Type", "application/json")
				.body("{\r\n"
						+ "    \"ID\":\""+id+"\"\r\n"
						+ "}")
				.when().delete("/Library/DeleteBook.php")
				.then().log().all().assertThat().statusCode(200).extract().response();
//		String deleteRes = deleteResponse.asString()
		JsonPath js2 = ReusableMethod.rawToJson(deleteResponse);
		System.out.println(js2);
	}
	

}
