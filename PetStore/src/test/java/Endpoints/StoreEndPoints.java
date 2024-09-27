package Endpoints;

import static io.restassured.RestAssured.given;

import Payload.StorePojo;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class StoreEndPoints {
	
	public static Response postStore(StorePojo createStorePayload) {
		Response response = given().log().all()
				.contentType(ContentType.JSON)
				.body(createStorePayload)
				.when().post(EndPoints.postStore_url);
		return response;
	}
	public static Response getStore(int id) {
		Response response = given().log().all()
				.contentType(ContentType.JSON)
				.pathParam("id", id)
				.when().get(EndPoints.getStore_url);
		return response;
	}
	public static Response deleteStore(int id) {
		Response response = given().log().all()
				.contentType(ContentType.JSON)
				.pathParam("id", id)
				.when().delete(EndPoints.deleteStore_url);
		return response;
	}
}
