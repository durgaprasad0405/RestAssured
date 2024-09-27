package petstore;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

public class store{

	public static void main(String[] args) {
		
		given().header("Content-Type","application/json")
		.body(Payload.createPayload())
		.when().post(endpoints.createStore)
		.then().log().all().assertThat().statusCode(200);
		
		String id = Payload.createPayload().getId();
		System.out.println(id);
	}
}
