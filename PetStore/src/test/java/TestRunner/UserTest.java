package TestRunner;

import org.junit.Assert;
import org.junit.Test;

//import org.testng.Assert;
//import org.testng.annotations.Test;

import Endpoints.UserEndPoints;
import Payload.UserPayLoads;
import io.restassured.response.Response;

//@TestMethodOrder(OrderAnnotation.class)
public class UserTest {
	
	
	@Test//(priority=1)
//	@Order(1)
	
	public void testAPost() {
		
		System.out.println("********************************  *Create User*   ********************************");

		Response responce = UserEndPoints.postUser(UserPayLoads.createUserPayload());
		responce.then().log().all();
		
		System.out.println(responce);
//		Assert.assertEquals(responce.getBody(), PayLoads.createPayload().getID());
		Assert.assertEquals(responce.getStatusCode(), 200);
	}

	@Test//(priority=2)
	public void testBGet() {
		
		System.out.println("********************************  *Get User By Username*   ********************************");

		Response responce = UserEndPoints.getUser(UserPayLoads.createUserPayload().getUserName());
		responce.then().log().all();
		
		Assert.assertEquals(responce.getStatusCode(), 200);
	}

	@Test//(priority=2)
	public void testCPut() {
		
		System.out.println("********************************  *Update User By Username*   ********************************");

		Response responce = UserEndPoints.putUser(UserPayLoads.putUserPayload(), UserPayLoads.createUserPayload().getUserName());
		responce.then().log().all();
		
		Assert.assertEquals(responce.getStatusCode(), 200);
	}
	@Test//(priority=2)
	public void testDGet() {
		
		System.out.println("********************************  *Login User By Username And Password*   ********************************");

		Response responce = UserEndPoints.getUserLogin(UserPayLoads.createUserPayload().getUserName(), UserPayLoads.createUserPayload().getPassword());
		responce.then().log().all();
		
		Assert.assertEquals(responce.getStatusCode(), 200);
	}
	@Test//(priority=2)
	public void testEGet() {
		
		System.out.println("********************************  *Logout User*   ********************************");

		Response responce = UserEndPoints.getUserLogout();
		responce.then().log().all();
		
		Assert.assertEquals(responce.getStatusCode(), 200);
	}
	@Test//(priority=3)
	public void testFDelete() {
		
		System.out.println("********************************  *Delete Pet*   ********************************");

		Response responce = UserEndPoints.deleteUser(UserPayLoads.createUserPayload().getUserName());
		responce.then().log().all();
		
		Assert.assertEquals(responce.getStatusCode(), 404);
	}
}
