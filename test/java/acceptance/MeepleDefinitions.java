package acceptance;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class MeepleDefinitions {
    // placing meeple invalidly

    @Given("^a meeple is being placed$")
    public void a_meeple_is_being_placed() throws Throwable {
    }

    @When("^the meeple’s attempted placement is invalid\\(on volcano or a hexagon that’s already taken\\)$")
    public void the_meeple_s_attempted_placement_is_invalid_on_volcano_or_a_hexagon_that_s_already_taken() throws Throwable {
    }

    @Then("^disallow the invalid meeple placement$")
    public void disallow_the_invalid_meeple_placement() throws Throwable {
    }

    @When("^the meeple is not on a volcano or occupied territory$")
    public void the_meeple_is_not_on_a_volcano_or_occupied_territory() throws Throwable {
    }

    @Then("^place the meeple on the selected tile$")
    public void place_the_meeple_on_the_selected_tile() throws Throwable {
    }
}
