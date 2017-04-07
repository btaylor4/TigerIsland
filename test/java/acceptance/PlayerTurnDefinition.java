package acceptance;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PlayerTurnDefinition {
    @Given("^a player is in the build phase$")
    public void a_player_is_in_the_build_phase() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Given("^the player has at least (\\d+) meeple or Totoro$")
    public void the_player_has_at_least_meeple_or_Totoro(int arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @When("^player cannot play meeple or Totoro$")
    public void player_cannot_play_meeple_or_Totoro() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("^the player immediately loses$")
    public void the_player_immediately_loses() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }



    @Given("^the player has at least (\\d+) meeple$")
    public void the_player_has_at_least_meeple(int arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Given("^the player has at least (\\d+) Totoro$")
    public void the_player_has_at_least_Totoro(int arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @When("^player can place a meeple or a Totoro$")
    public void player_can_place_a_meeple_or_a_Totoro() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("^the player must place a meeple or a Totoro$")
    public void the_player_must_place_a_meeple_or_a_Totoro() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }
}
