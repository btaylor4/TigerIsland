import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Bryan on 3/17/17.
 */
public class TestGameBoard
{
    @Test
    public void TestCreateBoard()
    {
        GameBoard game = new GameBoard();
        assertNotNull(game);
        assertTrue("Tile object is a tile", game instanceof GameBoard);
    }

    @Test
    public void TestCreationOfAllTiles()
    {
        GameBoard game = new GameBoard();
        game.createTiles();

        Tile[] tileCreations = game.getTileStack();

        for(Tile tile : tileCreations)
        {
            assertNotNull(tile);
            assertTrue("Tile object is a tile", game instanceof GameBoard);
        }
    }

    @Test
    public void TestEachTileHasThreeTerrains()
    {

    }
}
