package Endpoints;

import static io.restassured.RestAssured.given;

import Payload.PayLoads;
import Payload.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

//Creation of CRUD operations
//Created for perform Create, Read, Update and Delete requests to the API
public class UserEndPoints {
	public static Response createUser(User createUserPayload) {
		Response response = given().log().all()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(createUserPayload)
				.when().post(EndPoints.post_url);
		
		return response;
	}
	public static Response readUser(String userName, String password) {
		Response response = given().log().all()
				.queryParam("username", userName).queryParam("password", password)
				.when().get(EndPoints.get_url);
		
		return response;
	}
	public static Response updateUser(String userName, User payload) {
		Response response = given().log().all()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.pathParam("username", userName)
				.body(payload)
				.when().put(EndPoints.put_url);
		
		return response;
	}
	public static Response deleteUser(String userName) {
		Response response = given().log().all()
				.pathParam("username", userName)
				.when().post(EndPoints.delete_url);
		
		return response;
	}
}
