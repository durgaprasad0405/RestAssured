package resources;

import java.util.ArrayList;
import java.util.List;

import pojo.AddPlace;
import pojo.Location;

public class TestDataBuild {
	
	public AddPlace addPlacePayload(String name, String language, String address) {
		
		AddPlace addPlace = new AddPlace();
		
		Location l = new Location();
		l.setLat(-38.383494);
		l.setLat(33.427362);
		addPlace.setLocation(l);
		
		addPlace.setAccuracy(40);
		addPlace.setName(name);
		addPlace.setPhone_number("(+91) 983 893 3937");
		addPlace.setAddress(address);
		
		List<String> myList = new ArrayList<String>();
		myList.add("Kanna park");
		myList.add("Kanna shop");
		addPlace.setTypes(myList);
		
		addPlace.setWebsite("http://Kanna.com");
		addPlace.setLanguage(language);
		
		return addPlace;	
	}
	public String deletePlacePayload(String placeId) {
		return "{\r\n    \"place_id\":\""+placeId+"\"\r\n}";
	}
}
