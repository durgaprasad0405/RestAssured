package Endpoints;


public class EndPoints {
	
	public static String base_url = "https://petstore.swagger.io/v2/";
	
	//Store Module	
	public static String post_url = base_url+"store/order";
	public static String get_url = base_url+"store/order/{id}";
	public static String delete_url = base_url+"store/order/{id}";
	
}
