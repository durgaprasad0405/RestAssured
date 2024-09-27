package stepDefinations;

import java.io.IOException;
import io.cucumber.java.Before;

public class Hooks {
	@Before("@DeletePlace")
	public void beforeScenario() throws IOException {
		//Write a code that will gives you a plice_id
		//execute this code only when the place_id is null
		stepDefination stepDef = new stepDefination();
		if (stepDefination.place_id==null) {
			stepDef.add_place_payload("prasad", "uppal", "Indian");
			stepDef.user_calls_with_http_request("addPlaceAPI", "POST");
			stepDef.verify_place_id_created_maps_to_using("prasad", "getPlaceAPI");
		}
	}
}