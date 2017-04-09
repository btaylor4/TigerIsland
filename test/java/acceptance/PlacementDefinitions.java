package acceptance;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.en.And;
import main.*;
import main.enums.TerrainType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlacementDefinitions {
    GameBoard game  = new GameBoard();
    Tile tile;
    Player player1;
    Settlement settlement;
    ProjectionPack projections;


    @Given("^a player’s turn$")
    public void a_player_s_turn() throws Throwable {

        game.setFirstTile();
        settlement = new Settlement(game);

        player1 = new Player(game, 1);
        assertNotNull(player1);
    }

    @When("^the player must play a tile$")
    public void the_player_must_play_a_tile() throws Throwable {
        tile = new Tile();
        tile.assignTerrain(TerrainType.VOLCANO, TerrainType.LAKE);
        tile.rotation = 2 ;
        tile.setHexLevels(1);

        projections = player1.projectTilePlacement(tile, new Point(105, 106));
        projections.projectedLevel = game.getProjectedHexLevel(projections);
        assertTrue(game.isValidTilePlacement(projections));
    }

    @Then("^the player is assigned a tile to play$")
    public void the_player_is_assigned_a_tile_to_play() throws Throwable {

        assertNotNull(tile);
        player1.assignTile(tile);
    }

    @And("^the overall amount of tiles is decreased$")
    public void the_overall_amount_of_tiles_is_decreased() throws Throwable {
        int titleCount = game.getTileCount();
        game.decreaseTileCount();
        titleCount--;
        assertTrue(game.getTileCount() == titleCount);
    }
    @And("^the next tile to be played is prepared$")
    public void the_next_tile_to_be_played_is_prepared() throws Throwable {
        Tile nextTile = new Tile();
        nextTile.assignTerrain(TerrainType.VOLCANO, TerrainType.LAKE);
        nextTile.rotation = 2 ;
        nextTile.setHexLevels(1);

        assertNotNull (nextTile != null);
    }

    @And("^the player will enter the build phase$")
    public void the_player_will_enter_the_build_phase() throws Throwable {
        assertFalse(player1.isOutOfPieces());

    }

    @Given("^the selected tile placement is invalid$")
    public void the_selected_tile_placement_is_invalid() throws Throwable {
    }

    @When("^the player is assigned to play a tile$")
    public void the_player_is_assigned_to_play_a_tile() throws Throwable {
    }

    @Then("^the player may not place the tile$")
    public void the_player_may_not_place_the_tile() throws Throwable {
    }

    @And("^the player must place the tile elsewhere$")
    public void the_player_must_place_the_tile_elsewhere() throws Throwable {
    }

}
