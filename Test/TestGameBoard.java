import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Bryan on 3/17/17.
 */
public class TestGameBoard
{
    GameBoard game;

    @Before
    public void initVariables()
    {
        game = new GameBoard();
    }

    @Test
    public void TestCreateBoard()
    {
        assertNotNull(game);
        assertTrue("Tile object is a tile", game instanceof GameBoard);
    }

    @Test
    public void TestCreationOfAllTiles()
    {
        game.createTiles();

        Tile[] tileCreations = game.getTileStack();

        for(Tile tile : tileCreations)
        {
            assertNotNull(tile);
            assertTrue("Tile object is a tile", game instanceof GameBoard);
        }
    }

    /*@Test
    public void settingTheFirstTile()
    {
        game.setFirstTile();

        Hexagon[][] board = game.getBoard();

        for(int i = 0; i < 209; i++)
        {
            for(int j = 0; j < 209; j++)
            {
                if((i == 104 && j == 104) || (i == 104 && j == 105) || (i == 103 && j == 105))
                    assertNotNull(board[i][j]);
                else
                    assertNull(board[i][j]);
            }
        }
    }*/

    @Test
    public void testDifferentGeneratedTileAfterFirstTile()
    {
        Tile[] deck = game.getTileStack();
        game.setFirstTile();

        Tile tile = game.generateTile();

        assertNotEquals(tile, deck[0]);
    }

    @Test
    public void testValidTilePlacement()
    {
        game.setFirstTile();
        Tile tile = game.generateTile();
        int row = 104;
        int col = 106;
        int rot = 0;

        tile.setRotation(rot);
        assertTrue(game.selectTilePlacement(tile, row, col));
    }

    @Test
    public void testInvalidTilePlacement()
    {
        game.setFirstTile();
        Tile tile = game.generateTile();
        int row = 104;
        int col = 110;
        int rot = 0;

        tile.setRotation(rot);
        assertFalse(game.selectTilePlacement(tile, row, col));
    }
}