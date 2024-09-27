package TestRunner;

import org.junit.Assert;
import org.junit.Test;

//import org.testng.Assert;
//import org.testng.annotations.Test;

import Endpoints.StoreEndPoints;
import Payload.PayLoads;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class StoreTest {
		
	@Test
	public void testAPost() {
		Response response = StoreEndPoints.createUser(PayLoads.createStorePayload());
		response.then().log().all().extract().response();
		System.out.println(response);
		String res = response.asString();
		JsonPath js = new JsonPath(res);
		int expectedId = js.getInt("id");
		System.out.println(expectedId);
		int actualId = PayLoads.createStorePayload().getId();
		Assert.assertEquals(actualId, "55");
		Assert.assertEquals(response.getStatusCode(), 200);
		
	}
	@Test
	public void testBGet() {
		Response response = StoreEndPoints.getUser(PayLoads.createStorePayload().getId());
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 201);
	}
	@Test
	public void testCDelete() {
		Response response = StoreEndPoints.deleteUser(PayLoads.createStorePayload().getId());
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
	}
}