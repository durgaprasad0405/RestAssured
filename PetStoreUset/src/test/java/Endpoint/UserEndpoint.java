package Endpoint;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

//Creation of CRUD operations
//Created for perform Create, Read, Update and Delete requests to the API
public class UserEndpoint {
	public static Response postUser(Pojo.UserPojo createUserPayload) {
		Response response = given().log().all()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(createUserPayload)
				.when().post(Endpoints.postUser_url);
		
		return response;
	}
	public static Response getUser(String userName) {
		Response response = given().log().all()
				.pathParam("username", userName)
				.when().get(Endpoints.getUser_url);
		
		return response;
	}
	public static Response putUser(Pojo.UserPojo putUserPayloa, String username) {
		Response response = given().log().all()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.pathParam("username", username)
				.body(putUserPayloa)
				.when().put(Endpoints.putUser_url);
		
		return response;
	}
	public static Response getUserLogin(String userName, String password) {
		Response response = given().log().all()
				.queryParam("username", userName).queryParam("password", password)
				.when().get(Endpoints.getUser_url_login);
		
		return response;
	}
	public static Response getUserLogout() {
		Response response = given().log().all()
				.when().get(Endpoints.getUser_url_logout);
		
		return response;
	}
	public static Response deleteUser(String userName) {
		Response response = given().log().all()
				.accept(ContentType.JSON)
				.pathParam("username", userName)
				.when().delete(Endpoints.deleteUser_url);
		
		return response;
	}
}
