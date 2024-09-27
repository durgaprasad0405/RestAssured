package files;

import static io.restassured.RestAssured.*;

import org.junit.Assert;

import io.restassured.path.json.JsonPath;

public class GraphiQLScript {
	public static void main(String[] args) {
		
		//Quary
		int characterId = 5228;
		
		String responceQuary = given().log().all()
		.header("Content-Type","application/json; charset=utf-8")
		.body("{\"query\":\"query($characterId:Int!, $eposodeId:Int!)\\n{\\n  character(characterId:$characterId)\\n  {\\n    id\\n    name\\n    type\\n    species\\n  }\\n  episode(episodeId:$eposodeId)\\n  {\\n    id\\n    name\\n    episode\\n  }\\n  characters(filters:{name:\\\"kutti\\\"})\\n  {\\n    info{\\n      count\\n      pages\\n    }\\n    result\\n    {\\n      name\\n      episodes\\n      {\\n        air_date\\n        id\\n      }\\n    }\\n  }\\n}\",\"variables\":{\"characterId\":"+characterId+",\"eposodeId\":4287}}")
		.when().post("https://rahulshettyacademy.com/gq/graphql")
		.then().extract().response().asString();
		
		System.out.println(responceQuary);
		JsonPath js = new JsonPath(responceQuary);
		String characterName = js.get("data.character.name");
		Assert.assertEquals(characterName, "kutti");
		
		
		//Mutations
		String newCharacter = "kutti";
		
		String responceMutation = given().log().all()
		.header("Content-Type","application/json; charset=utf-8")
		.body("{\"query\":\"mutation($locationName:String!, $characterName:String!, $episodeName:String!)\\n{\\n  createLocation(location: {name:$locationName, type:\\\"normal\\\", dimension:\\\"south\\\"})\\n  {\\n    id\\n  }\\n  createEpisode(episode:{name:$episodeName, air_date:\\\"4-jan\\\", episode:\\\"1-12\\\"})\\n  {\\n    id\\n  }\\n  createCharacter(character:{name:$characterName, type:\\\"good looking\\\", status:\\\"alive\\\", species:\\\"advantures\\\", gender:\\\"male\\\", image:\\\"nive\\\", originId:5846, locationId:5846})\\n  {\\n    id\\n  }\\n  deleteEpisodes(episodeIds:[4285])\\n  {\\n    episodesDeleted\\n  }\\n}\",\"variables\":{\"locationName\":\"uppal\",\"characterName\":\""+newCharacter+"\",\"episodeName\":\"Manifest\"}}")
		.when().post("https://rahulshettyacademy.com/gq/graphql")
		.then().extract().response().asString();
		
		System.out.println(responceMutation);
		
	}

}
