import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Created by jdavi on 3/17/17.
 */
public class StepDefinition {

    GameBoard testGame ;

    @Given("^There is no game")
    public void creationGiven() throws Throwable{
        assert (testGame == null);
    }

    @When("^A game is made")
    public void creationWhen() throws Throwable{
        testGame = new GameBoard();
    }

    @Then("^Create the game")
    public void creationThen() throws Throwable{
        assert (testGame != null);
        System.out.println("  Game board created");
    }


    Tile testTile ;

    @Given("^A tile is being made")
    public void tileCreationGiven() throws Throwable{
        assert (testTile == null);
    }

    @When("^a tile is made")
    public void tileCreationWhen() throws Throwable{
        testTile = new Tile();
    }

    @Then("^create the tile")
    public void tileCreationThen() throws Throwable{
        assert (testTile != null);
        System.out.println("  New tile created");
    }



}
