package Endpoint;

//Swagger Base URL	--> https://petstore.swagger.io/v2/

//User
//Create user(POST)	--> https://petstore.swagger.io/v2/user/createWithList
//Get User(GET)		--> https://petstore.swagger.io/v2/user/{username}
//PUT User(PUT)		--> https://petstore.swagger.io/v2/user/{username}
//GET User(GET)		--> https://petstore.swagger.io/v2/user/login
//GET User(GET)		--> https://petstore.swagger.io/v2/user/logout	
//Delete User(DELETE)-> https://petstore.swagger.io/v2/user/{username}

//Store
//POST Store	--> https://petstore.swagger.io/v2/store/order
//GET Store		--> https://petstore.swagger.io/v2/store/order/{id}
//DELETE Store	--> https://petstore.swagger.io/v2/store/order/{id}

//Pet
//POST Pet	--> https://petstore.swagger.io/v2/pet
//POST Pet	--> https://petstore.swagger.io/v2/pet/{id}/uploadImage
//GET Pet	--> https://petstore.swagger.io/v2/pet/findByStatus?status=pending
//GET pet	--> https://petstore.swagger.io/v2/pet/{id}
//POST Pet	--> https://petstore.swagger.io/v2/pet/{id}
//DELETE Pet--> https://petstore.swagger.io/v2/pet/{id}

public class Endpoints {
	
	public static String base_uel = "https://petstore.swagger.io/v2/";
	
	//user module
	
	public static String postUser_url = base_uel+"user";
	public static String getUser_url = base_uel+"user/{username}";
	public static String putUser_url = base_uel+"user/{username}";
	public static String getUser_url_login = base_uel+"user/login";
	public static String getUser_url_logout = base_uel+"user/logout";
	public static String deleteUser_url = base_uel+"user/{username}";
	
	//Store Model
	
	public static String postStore_url = base_uel+"store/order";
	public static String getStore_url = base_uel+"user/store/order/{id}";
	public static String deleteStore_url = base_uel+"user/store/order/{id}";
	
	
	//Pet Model
	
	public static String postPet_url = base_uel+"store/order";
	public static String postPetImage_url = base_uel+"store/order";
	public static String getPetByStatus_url = base_uel+"store/order";
	public static String getPetById_url = base_uel+"store/order";
	public static String postPetById_url = base_uel+"store/order";
	public static String deletePet_url = base_uel+"store/order";
}
