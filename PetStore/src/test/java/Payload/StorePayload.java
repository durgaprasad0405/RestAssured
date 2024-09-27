package Payload;

public class StorePayload {
	
	public static StorePojo createStorePayload() {
		StorePojo createStorePayload = new StorePojo();
		createStorePayload.setId(484);
		createStorePayload.setPetId(86);
		createStorePayload.setQuantity(8);
		createStorePayload.setShipDate("2024-02-06T10:11:44.120Z");
		createStorePayload.setStatus("Avalabile");
		createStorePayload.setComplete(true);
		return createStorePayload;
	}

}
