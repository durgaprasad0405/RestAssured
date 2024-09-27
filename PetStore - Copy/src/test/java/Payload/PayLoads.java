package Payload;

public class PayLoads {
	
	public static Store createStorePayload() {
		
		Store createStorePayload = new Store();
		createStorePayload.setId(424);
		createStorePayload.setPetId(24);
		createStorePayload.setQuantity(4);
		createStorePayload.setShipDate("2024-01-30T09:31:40.223Z");
		createStorePayload.setStatus("Placed");
		createStorePayload.setComplete(true);
		return createStorePayload;
		
	}
	
	
}
