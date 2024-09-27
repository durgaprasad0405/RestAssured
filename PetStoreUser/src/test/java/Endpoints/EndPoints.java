package Endpoints;

//Swagger Base URL	--> https://petstore.swagger.io/v2/
//Create user(POST)	--> https://petstore.swagger.io/v2/user
//Get User(GET)		--> https://petstore.swagger.io/v2/user/{username}
//put User(PUT)		--> https://petstore.swagger.io/v2/user/{username}
//Delete User(DELETE)-> https://petstore.swagger.io/v2/user/{username}

public class EndPoints {
	
	public static String base_uel = "https://petstore.swagger.io/v2/";
	
	//user module
	
	public static String post_url = base_uel+"user";
	public static String get_url = base_uel+"user/login";
	public static String put_url = base_uel+"user/{username}";
	public static String delete_url = base_uel+"user/{username}";
	
	//Store Model
	
	//Pet Model
	
}
