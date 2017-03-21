import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

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
}
