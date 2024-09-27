Feature: Validating place API's
@AddPlace @Regression
Scenario Outline: Verify if place is being succesfully added using AddPlaceAPI
	Given Add place Payload "<name>" "<language>" "<address>"
	When user calls "addPlaceAPI" with "POST" http Request
	Then The API calls get success with status code 200
	And "status" in response body is "OK"
	And "scope" in response body is "APP"
	And Verify place_id created maps to "<name>" using "getPlaceAPI"
Examples: 
	| name 			| language 		| address					   |
	| Kanna House 	| Telugu-IN 	| Kanna Colony, Kanna Street   |
#	| prasad House	| English-EN 	| Prasad Colony, Prasad Street |

@DeletePlace @Regression
Scenario: Verify if delete place functionality is working
	Given DeletePlace payload
	When user calls "deletePlaceAPI" with "POST" http Request
	Then The API calls get success with status code 200
	And "status" in response body is "OK"