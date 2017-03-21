import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        Point point = new Point(103, 105);
        Hexagon hex = game.getHex(point);
        assertTrue(player.checkIfHexIsLevelOne(hex));
    }
}
