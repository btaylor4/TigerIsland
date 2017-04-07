package unitTests;

import main.GameBoard;
import main.Player;
import main.Settlement;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Bryan on 4/6/17.
 */
public class TestPlayer
{
    Player player1;
    Player player2;
    GameBoard game;

    @Before
    public void initializeVariables()
    {
        game = new GameBoard();
        player1 = new Player(game, 1);
        player2 = new Player(game, 2);
    }

    @Test
    public void TestCreationOfPlayer()
    {
        assertNotNull(player1);
        assertNotNull(player2);
    }

    @Test
    public void TestWinWhenIUsedUpAllMeeplesAndTotoro()
    {
        player1.setMeeples(0);
        player1.setTotoro(0);
        assertTrue(player1.isOutOfPieces());
    }

    @Test
    public void TestWinWhenIUsedUpAllMeeplesAndPlaygrounds()
    {
        player1.setMeeples(0);
        player1.setTigerPlayground(0);
        assertTrue(player1.isOutOfPieces());
    }

    @Test
    public void TestWinWhenIUsedUpAllPlaygroundsAndTotoro()
    {
        player1.setTigerPlayground(0);
        player1.setTotoro(0);
        assertTrue(player1.isOutOfPieces());
    }

    @Test
    public void TestNoWinWhenIUsedUpAllMeeplesAndTotoro()
    {
        player1.setMeeples(1);
        player1.setTotoro(0);
        assertFalse(player1.isOutOfPieces());
    }

    @Test
    public void TestNoWinWhenIUsedUpAllMeeplesAndPlaygrounds()
    {
        player1.setMeeples(1);
        player1.setTigerPlayground(0);
        assertFalse(player1.isOutOfPieces());
    }

    @Test
    public void TestNoWinWhenIUsedUpAllPlaygroundsAndTotoro()
    {
        player1.setTigerPlayground(2);
        player1.setTotoro(0);
        assertFalse(player1.isOutOfPieces());
    }

    @Test
    public void TestNotLostIfPlayerHasMeeplesLeft()
    {
        assertFalse(player1.hasPlayerLost());
    }

    /*@Test
    public void TestNotLostIfPlayerCanPlaceTotoro()
    {
        player1.playerSettlements.put(0, new Settlement());
        Settlement set = player1.playerSettlements.get(0);
        set.size = 5;

        assertFalse(player1.hasPlayerLost());
    }*/

    @Test
    public void TestLostIfNoValidPlacements()
    {
        player1.setMeeples(0);
        assertTrue(player1.hasPlayerLost());
    }

    /*
    Test overlapping tiles correctly
    Test not placing on volcano
    Test not nuking only 1 piece or entire settlement
    Test Valid tile placement
    Test Settlement construction/deconstruction, merging
    Test Scoring

     */
}
