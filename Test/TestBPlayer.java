import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by Bryan on 3/20/17.
 */
public class TestBPlayer
{
    GameBoard game;
    B_Player player;

    @Before
    public void initVariables()
    {
        game = new GameBoard();
        player = new B_Player(game);
    }

    @Test
    public void testCreationOfPlayer()
    {
        assertNotNull(player);
    }

    @Test
    public void testPlayerCannotPlaceOnVolcano()
    {
        game.setFirstTile(); //coordinates of volcano are [104,104]
        Point point = new Point(104, 104);
        Hexagon hex = game.getHex(point);
        assertTrue(player.checkIfHexIsVolcano(hex));
    }

    @Test
    public void testPlayerCanPlaceOnNonVolcano()
    {
        game.setFirstTile(); //coordinates of volcano are [104,104]
        Point point = new Point(103, 105);
        Hexagon hex = game.getHex(point);
        assertFalse(player.checkIfHexIsVolcano(hex));
    }

    @Test
    public void testPlayerCannotPlaceOnOccupiedTile()
    {
        game.setFirstTile();
        OccupantType occupant = OccupantType.MEEPLE;
        Point point = new Point(103, 105);
        Hexagon hex = game.getHex(point);
        game.setPieceOnHex(point, occupant);
        assertFalse(player.checkIfHexIsOpen(hex));
    }

    @Test
    public void testPlayerCanPlaceOnOpenTile()
    {
        game.setFirstTile();
        OccupantType occupant = OccupantType.NONE;
        Point point = new Point(103, 105);
        Hexagon hex = game.getHex(point);
        game.setPieceOnHex(point, occupant);
        assertTrue(player.checkIfHexIsOpen(hex));
    }

    @Test
    public void testPlayerCannotPlaceOnLevelNotOne()
    {
        game.setFirstTile();
        Point point = new Point(110, 115);
        Hexagon hex = game.getHex(point);
        assertFalse(player.checkIfHexIsLevelOne(hex));
    }

    @Test
    public void testPlayerCanPlaceOnLevelOne()
    {
        game.setFirstTile();
        Point point = new Point(104, 105);
        Hexagon hex = game.getHex(point);
        assertTrue(player.checkIfHexIsLevelOne(hex));
    }

    @Test
    public void testInitialPlayerMeepleAmount()
    {
        assertEquals(20, player.getMeeples());
    }

    @Test
    public void testInitialTotoroAmount()
    {
        assertEquals(3, player.getTortoro());
    }

    @Test
    public void testInitialScore()
    {
        assertEquals(0, player.getScore());
    }

    @Test
    public void testInvalidInitialPlayerMeepleAmount()
    {
        assertNotEquals(0, player.getMeeples());
    }

    @Test
    public void testInvalidTotoroAmount()
    {
        assertNotEquals(0, player.getTortoro());
    }

    @Test
    public void testInvalidInitialScore()
    {
        assertNotEquals(1, player.getScore());
    }

    @Test
    public void testMeeplesDecreaseAfterPlacing()
    {
        player.placeMeeple();
        assertEquals(19, player.getMeeples());
    }

    @Test
    public void testInvalidMeepleAmountAfterPlacing()
    {
        player.placeMeeple();
        assertNotEquals(20, player.getMeeples());
    }

    @Test
    public void testTortoroDecreaseAfterPlacing()
    {
        player.placeTortoro();
        assertEquals(2, player.getTortoro());
    }

    @Test
    public void testInvalidTortoroAmountAfterPlacing()
    {
        player.placeTortoro();
        assertNotEquals(3, player.getTortoro());
    }
}
