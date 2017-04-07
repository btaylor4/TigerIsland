package unitTests;
import main.GameBoard;
import main.Point;
import main.Settlement;
import main.Tile;

import main.enums.OccupantType;
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

    @Test
    public void testPlayerCannotPlaceOnVolcano()
    {
        game.setFirstTile(); //coordinates of volcano are [104,104]
        Point point = new Point(105, 105);
        assertFalse(game.isValidSettlementPosition(point));
    }

    @Test
    public void testPlayerCanPlaceOnNonVolcano()
    {
        game.setFirstTile(); //coordinates of volcano are [104,104]
        Point point = new Point(104, 105);
        assertTrue(game.isValidSettlementPosition(point));
    }

    @Test
    public void testPlayerCannotPlaceOnOccupiedTile()
    {
        game.setFirstTile();
        OccupantType occupant = OccupantType.MEEPLE;
        Point point = new Point(104, 105);
        game.setPiece(point, occupant, new Settlement(game));
        assertFalse(game.isValidSettlementPosition(point));
    }

    @Test
    public void testPlayerCanPlaceOnOpenTile()
    {
        game.setFirstTile();
        OccupantType occupant = OccupantType.NONE;
        Point point = new Point(104, 105);
        assertTrue(game.isValidSettlementPosition(point));
    }

    @Test
    public void testPlayerCannotPlaceOnLevelNotOne()
    {
        game.setFirstTile();
        game.board[104][105].level = 2;

        Point point = new Point(104, 105);
        assertFalse(game.isValidSettlementPosition(point));
    }

    @Test
    public void testPlayerCanPlaceOnLevelOne()
    {
        game.setFirstTile();
        Point point = new Point(104, 105);
        assertTrue(game.isValidSettlementPosition(point));
    }

    /*
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
