package TestRunner;

import org.junit.Assert;
import org.junit.Test;

import Endpoints.StoreEndPoints;
import Payload.StorePayload;
import io.restassured.response.Response;

public class StoreTest {
	
	@Test
	public void testAPost() {
		Response response = StoreEndPoints.postStore(StorePayload.createStorePayload());
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
	}
	@Test
	public void testBGet() {
		Response response = StoreEndPoints.getStore(StorePayload.createStorePayload().getId());
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
	}
	@Test
	public void testBdelete() {
		Response response = StoreEndPoints.deleteStore(StorePayload.createStorePayload().getId());
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
	}
}
