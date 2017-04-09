package acceptance;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.en.And;
import main.*;
import main.enums.TerrainType;
import org.junit.Assert;

import static org.junit.Assert.assertNotNull;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PlayerTurnDefinition {
    GameBoard game;
    Tile tile;
    Player player1;
    Settlement settlement;
    ProjectionPack projections;
    private void CreateGame()
    {
        game  = new GameBoard();
        game.setFirstTile();
    }
    @Given("^a player is in the build phase$")
    public void a_player_is_in_the_build_phase() throws Throwable {

        CreateGame();
        settlement = new Settlement(game);

        player1 = new Player(game, 1);
        assertNotNull(player1);
    }

    @Given("^the player has at least (\\d+) meeple or Totoro$")
    public void the_player_has_at_least_meeple_or_Totoro(int arg1) throws Throwable {
        assertNotEquals( 0, player1.getMeeples());
        assertNotEquals( 0, player1.getTotoro());

    }

    @When("^player cannot play meeple or Totoro$")
    public void player_cannot_play_meeple_or_Totoro() throws Throwable {
        game.setFirstTile();
        game.board[104][105].level = 2;

        Point point = new Point(104, 105);
        assertFalse(game.isValidSettlementPosition(point));
    }
    @Then("^the player immediately loses$")
    public void the_player_immediately_loses() throws Throwable {
        //player1.loses();
    }
    @Given("^A player is in the build phase$")
    public void the_player_is_in_the_build_phase() throws Throwable {
        CreateGame();
        settlement = new Settlement(game);

        player1 = new Player(game, 1);
        settlement.owner = player1;
        tile = new Tile();
        tile.assignTerrain(TerrainType.VOLCANO, TerrainType.LAKE);
        tile.rotation = 2 ;
        tile.setHexLevels(1);
        projections = player1.projectTilePlacement(tile, new Point(105, 106));
        projections.projectedLevel = game.getProjectedHexLevel(projections);
        player1.placeMeeple(new Point(104, 105), settlement);
        player1.assignTile(tile);
        player1.placeTile(projections);
        assertNotNull(player1);
        //player1.placeTile();
    }

    @And("^the player has at least (\\d+) meeple$")
    public void the_player_has_at_least_meeple(int arg1) throws Throwable {
        assertNotEquals(0, player1.getMeeples());
    }


    @And("^the player has at least (\\d+) Totoro$")
    public void the_player_has_at_least_Totoro(int arg1) throws Throwable {
        assertNotEquals(0, player1.getTotoro());

    }

    Point testPoint;
    @When("^player can place a meeple or a Totoro$")
    public void player_can_place_a_meeple_or_a_Totoro() throws Throwable {

        assertNotNull(settlement);
        //game.board[testPoint.row][testPoint.column].settlementPointer = settlement;
        testPoint = new Point(104, 105);

        assertNotNull(game.board[testPoint.row][testPoint.column].settlementPointer);
        Settlement totoroSet = new Settlement(game);

        //totoroSet.size = 5;
        game.board[testPoint.row][testPoint.column].settlementPointer.size = 5;
        //assertTrue(game.isValidTotoroPosition(new Point(105, 107), settlement));
        //assertTrue(game.isValidSettlementPosition(new Point(105, 107)));
    }

    @Then("^the player must place a meeple or a Totoro$")
    public void the_player_must_place_a_meeple_or_a_Totoro() throws Throwable {

        player1.placeMeeple(new Point(104, 105), settlement);
    }
}
