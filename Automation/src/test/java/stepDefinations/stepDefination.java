package stepDefinations;

import org.junit.runner.RunWith;

//import org.junit.runner.RunWith;
//import cucumber.api.PendingException;
import io.cucumber.java.en.*;
import io.cucumber.junit.Cucumber;
//import cucumber.api.java.en.Given;
//import cucumber.api.java.en.Then;
//import cucumber.api.java.en.When;
//import io.cucumber.junit.Cucumber;

@RunWith(Cucumber.class)
public class stepDefination {

	@Given("^User is on NetBanking landing page$")
	public void user_is_on_NetBanking_landing_page() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		System.out.println("Login");
	}

	@When("^User login into application with username \"([^\"]*)\" and password \"([^\"]*)\"$")
	public void user_login_into_application_with_and_password(String arg1, String arg2) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		System.out.println(arg1);
		System.out.println(arg2);
	}

	@Then("^Home page is populated$")
	public void home_page_is_populated() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		System.out.println("Home pahe");
	}

	@And("^Cards displayed are \"([^\"]*)\"$")
	public void cards_displayed_are(String arg1) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		System.out.println("Validated the cards");
		System.out.println(arg1);
	}
}
