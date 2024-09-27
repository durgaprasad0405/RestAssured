package petstore;

public class Payload {

	public static pojo createPayload() {
		pojo createPayload = new pojo();
		
		createPayload.setId("325");
		createPayload.setPetId("6747");
		createPayload.setQuantity("4");
		createPayload.setShipDate("2024-03-01T09:59:03.858+0000");
		createPayload.setStatus("placed");
		createPayload.setComplete("true");
		
		return createPayload;
	}	
}