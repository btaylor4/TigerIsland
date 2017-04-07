package unitTests;
import main.Hexagon;
import main.GameBoard;
import org.junit.Before;
import org.junit.Test;

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
    public void TestOccupantGetsSet()
    {

    }

    @Test
    public void TestInvalidOccupant()
    {

    }
}
