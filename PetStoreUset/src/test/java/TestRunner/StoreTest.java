package TestRunner;

import org.testng.Assert;
import org.testng.annotations.Test;

//import org.junit.Assert;
//import org.junit.Test;

import Endpoint.StoreEndpoint;
import Payload.StorePayload;
import io.restassured.response.Response;

public class StoreTest {
	
	@Test
	public void testAPost() {
		Response response = StoreEndpoint.postStore(StorePayload.createStorePayload());
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
	}
	@Test
	public void testBGet() {
		Response response = StoreEndpoint.getStore(StorePayload.createStorePayload().getId());
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
	}
	@Test
	public void testBdelete() {
		Response response = StoreEndpoint.deleteStore(StorePayload.createStorePayload().getId());
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
	}
}
