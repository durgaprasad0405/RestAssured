package stepDefinations;

import static io.restassured.RestAssured.given;

import resources.APIResources;
import resources.TestDataBuild;
import resources.Utils;
import io.cucumber.java.en.*;

import static org.junit.Assert.*;

import java.io.IOException;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class stepDefination extends Utils {
	
	ResponseSpecification resSpec;
	RequestSpecification reqSpec;
	Response response;
	TestDataBuild data = new TestDataBuild();
	static String place_id;
	
	
	@Given("Add place Payload {string} {string} {string}")
	public void add_place_payload(String name, String language, String address) throws IOException {
	    // Write code here that turns the phrase above into concrete actions
		reqSpec = given().spec(requestSpecification())
				.body(data.addPlacePayload(name, language, address));	
	}
	
	@When("user calls {string} with {string} http Request")
	public void user_calls_with_http_request(String resource ,String method) {
	    // Write code here that turns the phrase above into concrete actions
		// COnstructor will be called with value of resource which you pass
		APIResources resourceAPI = APIResources.valueOf(resource);
		System.out.println(resourceAPI.getResource());
		
		resSpec = new ResponseSpecBuilder().expectStatusCode(200)
				.expectContentType(ContentType.JSON).build();
		if(method.equalsIgnoreCase("POST"))
			response = reqSpec.when().post(resourceAPI.getResource());
		else if (method.equalsIgnoreCase("GET"))
			response = reqSpec.when().get(resourceAPI.getResource());
	}
	
	@Then("The API calls get success with status code {int}")
	public void the_api_calls_get_success_with_status_code(Integer int1) {
	    // Write code here that turns the phrase above into concrete actions
		assertEquals(response.getStatusCode(), 200);
	}
	
	@Then("{string} in response body is {string}")
	public void in_response_body_is(String key, String value) {
	    // Write code here that turns the phrase above into concrete actions
		
		assertEquals(getJsonPath(response, key),value);
	}
	
	@Then("Verify place_id created maps to {string} using {string}")
	public void verify_place_id_created_maps_to_using(String expectedName, String resource) throws IOException {
		place_id = getJsonPath(response, "place_id");
		reqSpec = given().spec(requestSpecification()).queryParam("place_id", place_id);
		user_calls_with_http_request(resource,"GET");
		String actualName = getJsonPath(response, "name");
		assertEquals(actualName, expectedName);
		
	}
	@Given("DeletePlace payload")
	public void delete_place_payload() throws IOException {
	    // Write code here that turns the phrase above into concrete actions
		reqSpec = given().spec(requestSpecification()).body(data.deletePlacePayload(place_id));
	}
}