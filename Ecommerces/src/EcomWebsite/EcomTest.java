package EcomWebsite;

import static io.restassured.RestAssured.*;

import java.io.File;

import PojoClass.LoginRequest;
import PojoClass.LoginResponse;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class EcomTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// Login to the Ecom Website.
		LoginRequest loginReq = new LoginRequest();
		loginReq.setUserEmail("prasad0405@gmail.com");
		loginReq.setUserPassword("RMprasad@1234");
		
		RequestSpecification loginReqSpec = new RequestSpecBuilder()
		.setBaseUri("https://rahulshettyacademy.com")
		.setContentType(ContentType.JSON).build();
		
		ResponseSpecification loginResSpec = new ResponseSpecBuilder()
		.expectStatusCode(200).expectContentType(ContentType.JSON).build();
		
		RequestSpecification reqLogin=given().log().all().spec(loginReqSpec).body(loginReq);
		
		LoginResponse loginRes=reqLogin.when().post("/api/ecom/auth/login")
		.then().log().all().extract().response().as(LoginResponse.class);
		
		System.out.println(loginRes.getToken());
		String token = loginRes.getToken();
		System.out.println(loginRes.getUserId());
		String userId = loginRes.getUserId();
		
		// To create Product in the website.
		// Add Product
		
		RequestSpecification addProductReqSpec = new RequestSpecBuilder()
				.setBaseUri("https://rahulshettyacademy.com")
				.addHeader("authorization", token).build();
		
		RequestSpecification reqAddProduct=given().log().all().spec(addProductReqSpec)
		.param("productName", "Mi").param("productAddedBy", userId)
		.param("productCategory", "Technology").param("productSubCategory", "Mobile")
		.param("productPrice", "44248").param("productDescription", "Nice")
		.param("productFor", "All")
		.multiPart("productImage", new File("\"C:\\Users\\draghava\\Downloads\\MiMobile.jpg\""));
		
		String addProductResponse=reqAddProduct.when().post("/api/ecom/product/add-product")
		.then().log().all().extract().response().asString();
		
		JsonPath jp=new JsonPath(addProductResponse);
		String productId = jp.get("productId");
	}
}
