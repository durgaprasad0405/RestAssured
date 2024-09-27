import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;

import org.testng.Assert;

public class JiraTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RestAssured.baseURI="http://localhost:8080";
		// Login to Jira to create session using Login API
		
		// We are using sessionFilter instead of JsonPath
		SessionFilter session = new SessionFilter();
		
		String response = given().relaxedHTTPSValidation().log().all()
		.header("Content-Type", "application/json")
		.body("{\r\n"
				+ "    \"username\": \"durgaprasad0405\",\r\n"
				+ "    \"password\": \"RMprasad@0405\"\r\n"
				+ "}").filter(session)
		.when().post("/rest/auth/1/session")
		.then().log().all().assertThat().statusCode(200)
		.extract().response().asString();
		
		String expectedMassage = "I am using this comment for Get Issues details";
		// Add a comment to existing issue using Add comment API 
		String addCommentRes = given().pathParam("key", "10200").log().all()
		.header("Content-Type", "application/json")
		.body("{\r\n"
				+ "    \"body\": \""+expectedMassage+"\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}")
		.filter(session).when().post("/rest/api/2/issue/{key}/comment")
		.then().log().all().assertThat().statusCode(201)
		.extract().response().asString();
		
		JsonPath jsAddComment = new JsonPath(addCommentRes);
		String commentId = jsAddComment.getString("id");
		
		// Add Attachments 
		given().log().all().header("X-Atlassian-Token", "no-check")
		.filter(session).pathParam("key", "10200")
		.header("Content-Type", "multipart/form-data")
		.multiPart("file", new File("jira.txt"))
		.when().post("/rest/api/2/issue/{key}/attachments")
		.then().log().all().assertThat().statusCode(200)
		.extract().response().asString();
		
		// Get Issues
		String issueDatiles = given().log().all().filter(session).pathParam("key", "10200")
		.when().get("/rest/api/2/issue/{key}")
		.then().log().all().extract().response().asString();
				
		JsonPath jsIssueDailes = new JsonPath(issueDatiles);
		int commentsCount = jsIssueDailes.getInt("fields.comment.comments.size()");
		for (int i = 0; i < commentsCount; i++) {
			String commentIdIssue = jsIssueDailes.get("fields.comment.comments["+i+"].id").toString();
			if (commentIdIssue.equalsIgnoreCase(commentId)) {
				String message = jsIssueDailes.get("fields.comment.comments["+i+"].body").toString();
				System.out.println(message);
				Assert.assertEquals(message, expectedMassage);
			}
		}
		

	}

}
