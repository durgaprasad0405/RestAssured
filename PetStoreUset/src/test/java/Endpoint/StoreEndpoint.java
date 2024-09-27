package Endpoint;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class StoreEndpoint {
	
	public static Response postStore(Pojo.StorePojo createStorePayload) {
		Response response = given().log().all()
				.contentType(ContentType.JSON)
				.body(createStorePayload)
				.when().post(Endpoints.postStore_url);
		return response;
	}
	public static Response getStore(int id) {
		Response response = given().log().all()
				.contentType(ContentType.JSON)
				.pathParam("id", id)
				.when().get(Endpoints.getStore_url);
		return response;
	}
	public static Response deleteStore(int id) {
		Response response = given().log().all()
				.contentType(ContentType.JSON)
				.pathParam("id", id)
				.when().delete(Endpoints.deleteStore_url);
		return response;
	}
}
