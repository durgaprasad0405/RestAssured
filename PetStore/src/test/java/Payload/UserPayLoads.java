package Payload;

public class UserPayLoads {
	
	public static UserPojo createUserPayload() {
		
		UserPojo createUserPayload = new UserPojo();
		createUserPayload.setID(4422);
		createUserPayload.setUserName("Durga0405");
		createUserPayload.setFirstName("Durga");
		createUserPayload.setLastName("prasad");
		createUserPayload.setEmail("prasad1234@gmail.com");
		createUserPayload.setPassword("prasad12");
		createUserPayload.setPhone("1234567890");
		return createUserPayload;
		
	}
	
	public static UserPojo putUserPayload() {
		
		UserPojo putUserPayload = new UserPojo();
		putUserPayload.setFirstName("Raghava");
		putUserPayload.setLastName("Chinna");
		return putUserPayload();
	}
	
}
