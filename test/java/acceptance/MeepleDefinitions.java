package acceptance;
import main.Hexagon;
import main.GameBoard;
import main.Point;
import main.Player;
import main.Settlement;
import main.Tile;
import main.enums.OccupantType;
import org.junit.Before;
import org.junit.Test;
import main.enums.*;
import main.ProjectionPack;


import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class MeepleDefinitions {
    // placing meeple invalidly
    GameBoard game;
    Tile tile;
    Player player1;
    Settlement settlement;
    ProjectionPack projections;
    @Before
    public void initVariables()
    {
        game = new GameBoard();
    }

    @Given("^a meeple is being placed$")
    public void a_meeple_is_being_placed() throws Throwable {
        settlement = new Settlement(game);
        game.setFirstTile();
        player1 = new Player(game, 1);
        settlement.owner = player1;
        tile = new Tile();
        tile.assignTerrain(TerrainType.VOLCANO, TerrainType.LAKE);
        tile.rotation = 2 ;
        tile.setHexLevels(1);
        projections = player1.projectTilePlacement(tile, new Point(105, 106));
        projections.projectedLevel = game.getProjectedHexLevel(projections);

    }

    @When("^the meeple’s attempted placement is invalid\\(on volcano or a hexagon that’s already taken\\)$")
    public void the_meeple_s_attempted_placement_is_invalid_on_volcano_or_a_hexagon_that_s_already_taken() throws Throwable {
        player1.placeMeeple(new Point(104, 105), settlement);
    }

    @Then("^disallow the invalid meeple placement$")
    public void disallow_the_invalid_meeple_placement() throws Throwable {
        assertTrue(game.isValidTilePlacement(projections));
    }

    @When("^the meeple is not on a volcano or occupied territory$")
    public void the_meeple_is_not_on_a_volcano_or_occupied_territory() throws Throwable {

    }


    @Then("^place the meeple on the selected tile$")
    public void place_the_meeple_on_the_selected_tile() throws Throwable {

    }
}
