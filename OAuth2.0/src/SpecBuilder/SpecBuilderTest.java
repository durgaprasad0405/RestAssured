package SpecBuilder;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

import Pojo.AddPlace;
import Pojo.Location;

public class SpecBuilderTest {
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
		
		//SpecBuilder creation 
		RequestSpecification reqSpec = new RequestSpecBuilder()
		.setBaseUri("https://rahulshettyacademy.com")
		.addQueryParam("key", "qaclick123")
		.setContentType(ContentType.JSON).build();
		
		ResponseSpecification resSpec = new ResponseSpecBuilder()
		.expectStatusCode(200).expectContentType(ContentType.JSON).build();
		
		RequestSpecification request = given().spec(reqSpec).body(addPlace);
		Response response = request.when().post("/maps/api/place/add/json")
		.then().spec(resSpec).extract().response();
		
		String responseString = response.asString();
		System.out.println(responseString);
	}
}
