package Payload;

import java.util.ArrayList;
import java.util.List;

public class PetPayload {

	public static PetPojo createPetPayload() {
		
		PetPojo createPetPayload = new PetPojo();
		createPetPayload.setId(464);
		
		category c = new category();
		c.setId(464);
		c.setName("Hachi");
		createPetPayload.setCategory(c);
		
		createPetPayload.setName("Chichu");
		
		List<String> myList = new ArrayList<String>();
		myList.add("Hachi1");
		createPetPayload.setPhotoUrls(myList);
		
		tag t= new tag();
		t.setId(464);
		t.setName("Chichu");
		List<tag> t1 = new ArrayList<tag>();
		t1.add(t);
		createPetPayload.setTags(t1);
		
		createPetPayload.setStatus("Avalible");
		
		
		return createPetPayload;
		
		
	}
}
