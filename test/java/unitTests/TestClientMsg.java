package unitTests;

import main.Point;
import main.Tile;
import main.enums.TerrainType;
import net.* ;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Thierry on 4/8/2017.
 */
public class TestClientMsg {
    NetClientMsg msg;

    @Before
    public void initializeVariables()
    {
        msg = new NetClientMsg();
    }

    @Test
    public void TestFormatTournamentPassword()
    {
        String enter = msg.FormatAuthenticationForTournament("SECRETPASSWORD");
        assertEquals(enter, "ENTER THUNDERDOME SECRETPASSWORD");
    }
    @Test
    public void TestFormatPlayerLogin()
    {
        String enter = msg.FormatAuthenticationPlayer("SECRETUSERNAME","SECRETPASSWORD");
        assertEquals(enter, "I AM SECRETUSERNAME SECRETPASSWORD");
    }
    @Test
    public void TestFormatPlaceAction()
    {
        TileVector tv = new TileVector(90, 91, 92, 4 );

        String enter= msg.FormatPlaceAction("JUNGLE+ROCK", tv);
        assertEquals(enter, "PLACE JUNGLE+ROCK AT 90 91 92 4");
    }
    @Test
    public void TestFormatBuildAction()
    {
        TileVector tv = new TileVector(90, 91, 92, 4 );

        String enter= msg.FormatBuildAction("FOUND", "TIGER PLAYGROUND", tv);
        assertEquals(enter, "FOUND TIGER PLAYGROUND AT 90 91 92");
    }
    @Test
    public void TestFormatBuildActionWithTerrain()
    {
        TileVector tvWithTerrain = new TileVector(90, 91, 92, "GRASS+LAKE" );

        String enter =msg.FormatBuildActionWithTerrain("EXPAND", "SETTLEMENT", tvWithTerrain );
        assertEquals(enter, "EXPAND SETTLEMENT AT 90 91 92 GRASS+LAKE");
    }
    @Test
    public void TestFormatGameMove()
    {

        TileVector tv = new TileVector(90, 91, 92, 4 );
        String placeAction = msg.FormatPlaceAction("JUNGLE+ROCK", tv);
        String buildAction = msg.FormatBuildAction("FOUND", "TIGER PLAYGROUND", tv);

        String gameMove = msg.FormatGameMove("CLOUDS", 4, placeAction, buildAction );

        assertEquals(gameMove, "GAME CLOUDS MOVE 4 PLACE JUNGLE+ROCK AT 90 91 92 4 FOUND TIGER PLAYGROUND AT 90 91 92");
    }
    @Test
    public void TestFormatGameMoveWithTerrain()
    {
        TileVector tv = new TileVector(100, 101, 102, 2 );
        String placeAction = msg.FormatPlaceAction("JUNGLE+ROCK", tv);

        TileVector tvWithTerrain = new TileVector(90, 91, 92, "GRASS+LAKE" );
        String buildAction = msg.FormatBuildActionWithTerrain("EXPAND", "SETTLEMENT", tvWithTerrain );

        String gameMove = msg.FormatGameMove("CLOUDS", 4, placeAction, buildAction );

        assertEquals(gameMove, "GAME CLOUDS MOVE 4 PLACE JUNGLE+ROCK AT " +
                "100 101 102 2 EXPAND SETTLEMENT AT 90 91 92 GRASS+LAKE");
    }
    @Test
    public void TestFormatUnableToBuild()
    {
        String gameMove = msg.FormatUnableToBuild();

        assertEquals(gameMove, "UNABLE TO BUILD");
    }
    @Test
    public void TestFormatBuildActionTile()
    {
        Tile tile = new Tile();
        tile.serverPoint = new Point(101, 105);
        tile.assignTerrain(TerrainType.GRASS, TerrainType.GRASS);
        tile.setRotation(3);
        String buildAct = msg.FormatBuildAction("BUILD", "TOTORO SANCTUARY", tile);

        assertEquals(buildAct, "BUILD TOTORO SANCTUARY AT 0 4 -4");
    }
    @Test
    public void TestFormatPlaceActionTile()
    {
        Tile tile = new Tile();
        tile.serverPoint = new Point(101, 105);
        tile.assignTerrain(TerrainType.GRASS, TerrainType.GRASS);
        tile.setRotation(3);
        String placeTile = msg.FormatPlaceAction(tile);


        assertEquals(placeTile, "PLACE TILE GRASS+GRASS AT 0 4 -4 3");
    }
    @Test
    public void TestFormatBuildActionWithTileAndTerrain()
    {
        Tile tile = new Tile();
        tile.serverPoint = new Point(101, 105);
        tile.assignTerrain(TerrainType.GRASS, TerrainType.GRASS);
        tile.setRotation(3);
        String placeWithTer = msg.FormatBuildActionWithTerrain(tile, TerrainType.ROCK);


        assertEquals(placeWithTer, "EXPAND SETTLEMENT AT 0 4 -4 ROCK");
    }
}
