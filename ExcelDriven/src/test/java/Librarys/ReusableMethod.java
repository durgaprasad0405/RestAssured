package Librarys;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ReusableMethod {

	public static JsonPath rawToJson(Response r)
	{ 
		String respon=r.asString();
		JsonPath x=new JsonPath(respon);
		System.out.println(respon);
		return x;
	}
}
