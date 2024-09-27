package Endpoints;

import static io.restassured.RestAssured.given;

import Payload.UserPojo;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

//Creation of CRUD operations
//Created for perform Create, Read, Update and Delete requests to the API
public class UserEndPoints {
	public static Response postUser(UserPojo createUserPayload) {
		Response response = given().log().all()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(createUserPayload)
				.when().post(EndPoints.postUser_url);
		
		return response;
	}
	public static Response getUser(String userName) {
		Response response = given().log().all()
				.pathParam("username", userName)
				.when().get(EndPoints.getUser_url);
		
		return response;
	}
	public static Response putUser(UserPojo putUserPayloa, String userName) {
		Response response = given().log().all()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.pathParam("username", userName)
				.body(putUserPayloa)
				.when().put(EndPoints.putUser_url);
		
		return response;
	}
	public static Response getUserLogin(String userName, String password) {
		Response response = given().log().all()
				.queryParam("username", userName).queryParam("password", password)
				.when().get(EndPoints.getUser_url_login);
		
		return response;
	}
	public static Response getUserLogout() {
		Response response = given().log().all()
				.when().get(EndPoints.getUser_url_logout);
		
		return response;
	}
	public static Response deleteUser(String userName) {
		Response response = given().log().all()
				.accept(ContentType.JSON)
				.pathParam("username", userName)
				.when().delete(EndPoints.deleteUser_url);
		
		return response;
	}
}
