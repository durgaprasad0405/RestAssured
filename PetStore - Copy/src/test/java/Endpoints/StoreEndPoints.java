package Endpoints;

import static io.restassured.RestAssured.*;

import Payload.Store;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class StoreEndPoints {
	public static Response createUser(Store createStorePayload) {
		Response response = given().log().all()
				.contentType(ContentType.JSON)
				.body(createStorePayload)
				.when().post(EndPoints.post_url);
		
		return response;
	}
	public static Response getUser(int id) {
		Response response = given().log().all()
				.pathParam("id", id)
				.when().get(EndPoints.get_url);
		
		return response;
	}
	public static Response deleteUser(int id) {
		Response response = given().log().all()
				.pathParam("id", id)
				.when().delete(EndPoints.delete_url);
		
		return response;
	}
}
