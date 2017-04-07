package unitTests;

import main.*;

import main.Settlement;
import main.enums.TerrainType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
        player1.setTigers(0);
        assertTrue(player1.isOutOfPieces());
    }

    @Test
    public void TestWinWhenIUsedUpAllPlaygroundsAndTotoro()
    {
        player1.setTigers(0);
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
        player1.setTigers(0);
        assertFalse(player1.isOutOfPieces());
    }

    @Test
    public void TestNoWinWhenIUsedUpAllPlaygroundsAndTotoro()
    {
        player1.setTigers(2);
        player1.setTotoro(0);
        assertFalse(player1.isOutOfPieces());
    }

    @Test
    public void TestNotLostIfPlayerHasMeeplesLeft()
    {
        assertFalse(player1.hasPlayerLost());
    }

    @Test
    public void TestNotLostIfPlayerCanPlaceTotoro()
    {
        player1.playerSettlements.put(0, new Settlement(game));
        Settlement set = player1.playerSettlements.get(0);
        set.size = 5;

        assertFalse(player1.hasPlayerLost());
    }

    @Test
    public void TestLostIfNoValidPlacements()
    {
        player1.setMeeples(0);
        assertTrue(player1.hasPlayerLost());
    }

    @Test
    public void TestMeepleDecreaseAfterPlace()
    {
        game.setFirstTile();
        Point point = new Point(104, 105);
        player1.placeMeeple(point, new Settlement(game));
        assertEquals(19, player1.getMeeples());
    }

    @Test
    public void TestScoreIncreasesAfterMeeplePlacementOnLevelOne()
    {
        game.setFirstTile();
        Point point = new Point(104, 105);
        player1.placeMeeple(point, new Settlement(game));
        assertEquals(1, player1.getScore());
    }

    @Test
    public void TestMeepleDecreaseAfterPlaceOnLevelHigherThanOne()
    {
        game.setFirstTile();
        Point point = new Point(104, 105);
        game.board[104][105].level = 2;
        player1.placeMeeple(point, new Settlement(game));
        assertEquals(18, player1.getMeeples());
    }

    @Test
    public void TestScoreIncreasesAfterMeeplePlacementOnLevelHigherThanOne()
    {
        game.setFirstTile();
        Point point = new Point(104, 105);
        game.board[104][105].level = 2;
        player1.placeMeeple(point, new Settlement(game));
        assertEquals(4, player1.getScore());
    }

    @Test
    public void TestInvalidMeepleDecreaseAfterPlace()
    {
        game.setFirstTile();
        Point point = new Point(104, 105);
        player1.placeMeeple(point, new Settlement(game));
        assertNotEquals(20, player1.getMeeples());
    }

    @Test
    public void TestInvalidScoreIncreasesAfterMeeplePlacementOnLevelOne()
    {
        game.setFirstTile();
        Point point = new Point(104, 105);
        player1.placeMeeple(point, new Settlement(game));
        assertNotEquals(0, player1.getScore());
    }

    @Test
    public void TestInvalidMeepleDecreaseAfterPlaceOnLevelHigherThanOne()
    {
        game.setFirstTile();
        Point point = new Point(104, 105);
        game.board[104][105].level = 2;
        player1.placeMeeple(point, new Settlement(game));
        assertNotEquals(20, player1.getMeeples());
    }

    @Test
    public void TestInvalidScoreIncreasesAfterMeeplePlacementOnLevelHigherThanOne()
    {
        game.setFirstTile();
        Point point = new Point(104, 105);
        Tile tile = new Tile();
        tile.assignTerrain(TerrainType.GRASSLANDS, TerrainType.GRASSLANDS);

        game.board[104][105].level = 2;
        player1.placeMeeple(point, new Settlement(game));
        assertNotEquals(0, player1.getScore());
    }

    @Test
    public void TestTotoroDecreaseAfterPlacement()
    {
        game.setFirstTile();
        Point point = new Point(104, 105);
        player1.placeTotoro();
        assertEquals(2, player1.getTotoro());
    }

    @Test
    public void TestPlaygroundDecreaseAfterPlacement()
    {
        game.setFirstTile();
        Point point = new Point(104, 105);
        player1.placeTiger();
        assertEquals(1, player1.getTigerPlayground());
    }

    @Test
    public void TestInvalidTotoroDecreaseAfterPlacement()
    {
        game.setFirstTile();
        Point point = new Point(104, 105);
        player1.placeTotoro();
        assertNotEquals(3, player1.getTotoro());
    }

    @Test
    public void TestInvalidPlaygroundDecreaseAfterPlacement()
    {
        game.setFirstTile();
        Point point = new Point(104, 105);
        player1.placeTiger();
        assertNotEquals(2, player1.getTigerPlayground());
    }

    @Test
    public void TestIncreasedScoreAfterTotoroPlacement()
    {
        game.setFirstTile();
        Point point = new Point(104, 105);
        player1.placeTotoro();
        assertEquals(200, player1.getScore());
    }

    @Test
    public void TestIncreasedScoreAfterPlaygroundPlacement()
    {
        game.setFirstTile();
        Point point = new Point(104, 105);
        player1.placeTiger();
        assertEquals(75, player1.getScore());
    }

    @Test
    public void TestInvalidIncreasedScoreAfterTotoroPlacement()
    {
        game.setFirstTile();
        Point point = new Point(104, 105);
        player1.placeTotoro();
        assertNotEquals(0, player1.getScore());
    }

    @Test
    public void TestInvalidIncreasedScoreAfterPlaygroundPlacement()
    {
        game.setFirstTile();
        Point point = new Point(104, 105);
        player1.placeTiger();
        assertNotEquals(0, player1.getScore());
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
