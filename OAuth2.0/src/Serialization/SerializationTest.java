package Serialization;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.testng.Assert;

import Pojo.AddPlace;
import Pojo.Location;

public class SerializationTest {
	public static void main(String[] args) {
		
		RestAssured.baseURI="https://rahulshettyacademy.com";
		
		AddPlace addPlace = new AddPlace();
		
		Location l = new Location();
		l.setLat(-38.383494);
		l.setLat(33.427362);
		addPlace.setLocation(l);
		
		addPlace.setAccuracy(40);
		addPlace.setName("Kanna House");
		addPlace.setPhone_number("(+91) 983 893 3937");
		addPlace.setAddress("Kanna Colony, Kanna Street");
		
		List<String> myList = new ArrayList<String>();
		myList.add("Kanna park");
		myList.add("Kanna shop");
		addPlace.setTypes(myList);
		
		addPlace.setWebsite("http://Kanna.com");
		addPlace.setLanguage("Telugu-IN");
		
		String a= addPlace.toString();
		System.out.println(a);
		JsonPath js = new JsonPath(a);
		System.out.println(js);
		String a2 = String.valueOf(js);
		System.out.println(a2);
		JSONObject jo = new JSONObject(addPlace);
		System.out.println(jo);
		
		
		String response = given().log().all()
		.queryParam("key", "qaclick123").body(addPlace)
		.when().post("/maps/api/place/add/json")
		.then().assertThat().statusCode(200).log().all()
		.extract().response().asString();
		
		JsonPath js1 = new JsonPath(response);
		
		System.out.println(js1);
	}
}
