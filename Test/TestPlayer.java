import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Bryan on 3/17/17.
 */
public class TestPlayer
{
    private GameBoard game;
    private Player player;

    @Before
    public void init()
    {
        game = new GameBoard();
        player = new Player(game);
    }

    @Test
    public void TestCreationOfPlayer()
    {
        assertNotNull(player);
    }

    @Test
    public void TestInitialPlayerMeepleAmount()
    {
        assertEquals(20, player.getMeeples());
    }

    @Test
    public void TestInitialTotoroAmount()
    {
        assertEquals(3, player.getTortoro());
    }

    @Test
    public void TestInitialScore()
    {
        assertEquals(0, player.getScore());
    }

    @Test
    public void TestInvalidInitialPlayerMeepleAmount()
    {
        assertNotEquals(0, player.getMeeples());
    }

    @Test
    public void TestInvalidTotoroAmount()
    {
        assertNotEquals(0, player.getTortoro());
    }

    @Test
    public void TestInvalidInitialScore()
    {
        assertNotEquals(1, player.getScore());
    }
}
