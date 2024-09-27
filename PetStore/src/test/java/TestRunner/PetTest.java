package TestRunner;

import org.testng.Assert;
import org.testng.annotations.Test;

//import org.junit.Assert;
//import org.junit.Test;

import Endpoints.PetEndPoints;
import Payload.PetPayload;
import io.restassured.response.Response;

public class PetTest {

	
	
	@Test
	public void testAPost() {
		
		System.out.println("********************************   *Create Pet*   ********************************");
		
		Response response = PetEndPoints.postPet(PetPayload.createPetPayload());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
	}
	
	@Test
	public void testBPost() {
		
		System.out.println("********************************   *Create Image Pet*   ********************************");
		
		Response response = PetEndPoints.postPetImage(PetPayload.createPetPayload().getId());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
	}
	
	@Test
	public void testCGet() {
		
		System.out.println("********************************   *Get Pet By Status*   ********************************");

		Response response = PetEndPoints.getPetByStatus(PetPayload.createPetPayload().getStatus());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
	}
	
	@Test
	public void testDGet() {
		
		System.out.println("********************************   *Get Pet By ID*   ********************************");

		Response response = PetEndPoints.getPetById(PetPayload.createPetPayload().getId());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
	}
	
	@Test
	public void testEDelete() {
		
		System.out.println("********************************   *Delete Pet*   ********************************");

		Response response = PetEndPoints.deletePet(PetPayload.createPetPayload().getId());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
	}
}
