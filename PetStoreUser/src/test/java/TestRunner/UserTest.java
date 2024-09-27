package TestRunner;

import static org.testng.Assert.assertEquals;

import javax.mail.Message;

import org.junit.Assert;
import org.junit.Test;

//import org.testng.Assert;
//import org.testng.annotations.Test;

import Endpoints.UserEndPoints;
import Payload.PayLoads;
import Payload.User;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;


public class UserTest {
	
//	public User userPayload() {
//		
//		User userPayload = new User();
//		userPayload.setID(4422);
//		userPayload.setUserName("Durga0405");
//		userPayload.setFirstName("Durga");
//		userPayload.setLastName("prasad");
//		userPayload.setEmail("prasad1234@gmail.com");
//		userPayload.setPassword("prasad12");
//		userPayload.setPhone("1234567890");
//		return userPayload;
//		
//	}
	
	@Test//(priority=1)
	public void testPostUser() {
		Response responce = UserEndPoints.createUser(PayLoads.createPayload());
		responce.then().log().all();
		
//		Assert.assertEquals(responce.getBody(), PayLoads.createPayload().getID());
		Assert.assertEquals(responce.getStatusCode(), 200);
	}
	@Test//(priority=2)
	public void testGetLoginWithUserAndPassword() {//this.userPayload().getUserName()
		Response responce = UserEndPoints.readUser(PayLoads.createPayload().getUserName(), PayLoads.createPayload().getPassword());
		responce.then().log().all();
		
		Assert.assertEquals(responce.getStatusCode(), 200);
	}
	@Test//(priority=2)
	public void testUpdateUser() {//this.userPayload().getUserName()
		Response responce = UserEndPoints.updateUser(PayLoads.createPayload(), PayLoads.createPayload().getUserName());
		responce.then().log().all();
		
		Assert.assertEquals(responce.getStatusCode(), 200);
	}
	@Test//(priority=2)
	public void testDeleteUser() {//this.userPayload().getUserName()
		Response responce = UserEndPoints.deleteUser(PayLoads.createPayload().getUserName());
		responce.then().log().all();
		
		Assert.assertEquals(responce.getStatusCode(), 200);
	}
}
