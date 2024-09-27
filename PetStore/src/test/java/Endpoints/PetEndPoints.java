package Endpoints;

import static io.restassured.RestAssured.given;

import java.io.File;

import Payload.PetPojo;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class PetEndPoints {

	public static Response postPet(PetPojo createPetPayload) {
		
		Response response = given().log().all()
				.contentType(ContentType.JSON)
				.body(createPetPayload)
				.when().post(EndPoints.postPet_url);
		return response;
		
	}

	public static Response postPetImage(int id) {
		
		Response response = given().log().all()
				.contentType(ContentType.JSON)
				.pathParam("id", id)
				.contentType("multipart/form-data")
				.multiPart("file", new File("C:\\Users\\draghava\\Downloads\\dog.jpg"))
				.when().post(EndPoints.postPetImage_url);
		return response;
		
	}
	
	public static Response getPetByStatus(String status) {
		
		Response response = given().log().all()
				.contentType(ContentType.JSON)
				.queryParam("status", status)
				.when().get(EndPoints.getPetByStatus_url);
		return response;
		
	}
	
	public static Response getPetById(int id) {
		
		Response response = given().log().all()
				.contentType(ContentType.JSON)
				.pathParam("id", id)
				.when().get(EndPoints.getPetById_url);
		return response;
		
	}
	
	public static Response deletePet(int id) {
		
		Response response = given().log().all()
				.pathParam("id", id)
				.when().delete(EndPoints.deletePet_url);
		return response;
		
	}
}
