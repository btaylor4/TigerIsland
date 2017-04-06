package unitTests;
import main.GameBoard;
import main.Tile;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Created by Bryan on 4/6/17.
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
        assertTrue("Game is a gameboard", game instanceof GameBoard);
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
    }*/
}
