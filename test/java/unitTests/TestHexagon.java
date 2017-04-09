package unitTests;
import main.Hexagon;
import main.GameBoard;
import main.Point;
import main.Settlement;
import main.enums.OccupantType;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Bryan on 4/6/17.
 */
public class TestHexagon
{
    GameBoard game;
    Hexagon hex;

    @Before
    public void initializeVariables()
    {
        game = new GameBoard();
        hex = new Hexagon();
    }

    @Test
    public void TestCreationOfHexagon()
    {
        assertNotNull(hex);
    }

    @Test
    public void TestOccupantGetsSettlement()
    {
        Point point = new Point(106,105);
        Settlement settle = new Settlement(game);
        game.setFirstTile();
        game.setPiece(point, OccupantType.MEEPLE, settle);
        assertEquals(game.board[point.row][point.column].settlementPointer, settle);
    }

    @Test
    public void TestInvalidOccupantSettlement()
    {
        Point point = new Point(105, 105);
        Settlement settle = new Settlement(game);
        game.setFirstTile();
        game.setPiece(point, OccupantType.MEEPLE, settle);
        assertNotEquals(game.board[point.row][point.column].settlementPointer, new Settlement(game));
    }

    @Test
    public void TestHexGetsMeeple()
    {
        Point point = new Point(106,105);
        Settlement settle = new Settlement(game);
        game.setFirstTile();
        game.setPiece(point, OccupantType.MEEPLE, settle);
        assertTrue(game.board[point.row][point.column].occupant == OccupantType.MEEPLE);
    }

    @Test
    public void TestHexInvalidHexGetMeeple()
    {
        Point point = new Point(106,105);
        Settlement settle = new Settlement(game);
        game.setFirstTile();
        game.setPiece(point, OccupantType.MEEPLE, settle);
        assertFalse(game.board[point.row][point.column].occupant != OccupantType.MEEPLE);
    }

    @Test
    public void TestHexGetsTotoro()
    {
        Point point = new Point(106, 105);
        Settlement settle = new Settlement(game);
        game.setFirstTile();
        game.setPiece(point, OccupantType.TOTORO, settle);
        assertTrue(game.board[point.row][point.column].occupant == OccupantType.TOTORO);
    }

    @Test
    public void TestInvalidHexGetsTotoro()
    {
        Point point = new Point(106, 105);
        Settlement settle = new Settlement(game);
        game.setFirstTile();
        game.setPiece(point, OccupantType.TOTORO, settle);
        assertFalse(game.board[point.row][point.column].occupant != OccupantType.TOTORO);
    }

    @Test
    public void TestHexGetsTiger()
    {
        Point point = new Point(106, 105);
        Settlement settle = new Settlement(game);
        game.setFirstTile();
        game.setPiece(point, OccupantType.TIGER, settle);
        assertTrue(game.board[point.row][point.column].occupant == OccupantType.TIGER);
    }

    @Test
    public void TestInvalidHexGetsTiger()
    {
        Point point = new Point(106, 105);
        Settlement settle = new Settlement(game);
        game.setFirstTile();
        game.setPiece(point, OccupantType.TIGER, settle);
        assertFalse(game.board[point.row][point.column].occupant != OccupantType.TIGER);
    }
}
