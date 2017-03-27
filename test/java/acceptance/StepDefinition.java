package acceptance;

import main.* ;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import main.enums.TerrainType;

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
    public static Tile testTileStack[] = new Tile[48];

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


    @Given("^the game is created")
    public void tileGenerationGiven() throws Throwable{
        GameBoard testGame = new GameBoard();
        assert(testGame != null) ;
    }

    @When("^the tiles are generated")
    public void tileGenerationWhen() throws Throwable{
        int tileCounter = 0;

        for (TerrainType a : TerrainType.values() ) {
            if(a == TerrainType.VOLCANO) continue ;

            for (TerrainType b : TerrainType.values() ) {
                if(b == TerrainType.VOLCANO) continue ;

                for (int i = 0; i < 3; i++) {
                    testTileStack[tileCounter] = new Tile();
                    testTileStack[tileCounter].assignTileNumber(tileCounter + 1) ;
                    testTileStack[tileCounter].assignTerrain(a, b) ;
                    tileCounter++;
                }
            }
        }
    }

    @Then("^the tiles should be permuted in sequence")
    public void tileGenerationThen() throws Throwable{
        for (int i = 0 ; i < 48 ; i++) {
             assert(testTileStack[i] != null);
             assert(testTileStack[i].tileNumber == i+1);
             assert(testTileStack[i].hexA != null);
             assert(testTileStack[i].hexB != null);
             assert(testTileStack[i].volcano.terrain == TerrainType.VOLCANO);
        }
    }
}