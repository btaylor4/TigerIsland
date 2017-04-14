package unitTests;

import main.*;

import main.Settlement;
import main.enums.OccupantType;
import main.enums.TerrainType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class TestPlayer
{
    /* fix all this
    Player player1;
    Player player2;
    GameBoard game;
    Tile tile;

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

    @Test
    public void TestValidTilePlacement()
    {
        game.setFirstTile();
        tile = new Tile();
        tile.assignTerrain(TerrainType.LAKE, TerrainType.LAKE);
        tile.rotation = 2 ;
        tile.setHexLevels(1);
        ProjectionPack projections = player1.projectTilePlacement(tile, new Point(105, 106));
        projections.projectedLevel = game.getProjectedHexLevel(projections);
        assertTrue(game.isValidTilePlacement(projections));
    }

    @Test
    public void TestNotAdjacentTilePlacement()
    {
        game.setFirstTile();
        tile = new Tile();
        tile.assignTerrain(TerrainType.LAKE, TerrainType.LAKE);
        tile.rotation = 1 ;
        tile.setHexLevels(1);
        player1.projectTilePlacement(tile, new Point(104, 108));
        ProjectionPack projections = player1.projectTilePlacement(tile, new Point(103, 108));
        projections.projectedLevel = game.getProjectedHexLevel(projections);
        assertFalse(game.isValidTilePlacement(projections));
    }

    @Test
    public void TestValidTileOverlap()
    {
        game.setFirstTile();
        tile = new Tile();
        tile.assignTerrain(TerrainType.LAKE, TerrainType.LAKE);
        tile.rotation = 2;
        tile.setHexLevels(1);
        ProjectionPack projections = player1.projectTilePlacement(tile, new Point(105, 106));
        projections.projectedLevel = game.getProjectedHexLevel(projections);
        game.setTile(tile, projections);

        tile = new Tile();
        tile.assignTerrain(TerrainType.LAKE, TerrainType.LAKE);
        tile.rotation = 1;
        tile.setHexLevels(1);
        projections = player1.projectTilePlacement(tile, new Point(105, 106));
        projections.projectedLevel = game.getProjectedHexLevel(projections);
        assertTrue(game.isValidTilePlacement(projections));
    }

    @Test
    public void TestInvalidTileOverlap()
    {
        game.setFirstTile();
        tile = new Tile();
        tile.assignTerrain(TerrainType.LAKE, TerrainType.LAKE);
        tile.rotation = 1;
        tile.setHexLevels(1);
        ProjectionPack projections = player1.projectTilePlacement(tile, new Point(105, 105));
        projections.projectedLevel = game.getProjectedHexLevel(projections);
        game.setTile(tile, projections);
        assertFalse(game.isValidTilePlacement(projections));
    }

    @Test
    public void TestInvalidTileOverlapOnSettlementOfOne()
    {
        game.setFirstTile();
        tile = new Tile();
        tile.assignTerrain(TerrainType.LAKE, TerrainType.LAKE);
        tile.rotation = 2;
        tile.setHexLevels(1);
        ProjectionPack projections = player1.projectTilePlacement(tile, new Point(105, 106));
        projections.projectedLevel = game.getProjectedHexLevel(projections);
        game.setPiece(new Point(104, 106), OccupantType.MEEPLE, new Settlement(game));

        tile = new Tile();
        tile.assignTerrain(TerrainType.LAKE, TerrainType.LAKE);
        tile.rotation = 1;
        tile.setHexLevels(1);
        projections = player1.projectTilePlacement(tile, new Point(105, 106));
        projections.projectedLevel = game.getProjectedHexLevel(projections);
        assertFalse(game.isValidTilePlacement(projections));
    }

    /*@Test
    public void TestInvalidTileOverlapNukingEntireSettlement() //not working when it should
    {
        Settlement settlement = new Settlement(game);
        settlement.owner = player1;
        game.setFirstTile();
        tile = new Tile();
        tile.assignTerrain(TerrainType.LAKE, TerrainType.LAKE);
        tile.rotation = 2;
        tile.setHexLevels(1);
        ProjectionPack projections = player1.projectTilePlacement(tile, new Point(105, 106));
        projections.projectedLevel = game.getProjectedHexLevel(projections);
        game.setTile(tile, projections);

        tile = new Tile();
        tile.assignTerrain(TerrainType.LAKE, TerrainType.LAKE);
        tile.rotation = 1;
        tile.setHexLevels(1);
        projections = player1.projectTilePlacement(tile, new Point(105, 106));
        projections.projectedLevel = game.getProjectedHexLevel(projections);

        player1.placeMeeple(new Point(104, 106), settlement);
        player1.placeMeeple(new Point(104, 107), settlement);
        assertFalse(game.isValidTilePlacement(projections));
    }*/
/*
    @Test
    public void TestCannotNukeTotoro()
    {
        game.setFirstTile();
        game.setPiece(new Point(104, 106), OccupantType.TOTORO, new Settlement(game));
        tile = new Tile();
        tile.assignTerrain(TerrainType.LAKE, TerrainType.LAKE);
        tile.rotation = 2;
        tile.setHexLevels(1);
        ProjectionPack projections = player1.projectTilePlacement(tile, new Point(105, 106));
        projections.projectedLevel = game.getProjectedHexLevel(projections);
        game.setTile(tile, projections);

        tile = new Tile();
        tile.assignTerrain(TerrainType.LAKE, TerrainType.LAKE);
        tile.rotation = 1;
        tile.setHexLevels(1);
        projections = player1.projectTilePlacement(tile, new Point(105, 106));
        projections.projectedLevel = game.getProjectedHexLevel(projections);
        assertFalse(game.isValidTilePlacement(projections));
    }

    @Test
    public void TestCannotNukePlayground()
    {
        game.setFirstTile();
        game.setPiece(new Point(104, 106), OccupantType.TIGER, new Settlement(game));
        tile = new Tile();
        tile.assignTerrain(TerrainType.LAKE, TerrainType.LAKE);
        tile.rotation = 2;
        tile.setHexLevels(1);
        ProjectionPack projections = player1.projectTilePlacement(tile, new Point(105, 106));
        projections.projectedLevel = game.getProjectedHexLevel(projections);
        game.setTile(tile, projections);

        tile = new Tile();
        tile.assignTerrain(TerrainType.LAKE, TerrainType.LAKE);
        tile.rotation = 1;
        tile.setHexLevels(1);
        projections = player1.projectTilePlacement(tile, new Point(105, 106));
        projections.projectedLevel = game.getProjectedHexLevel(projections);
        assertFalse(game.isValidTilePlacement(projections));
    }

    /*
    Test not nuking only 1 piece or entire settlement
    Test Settlement construction/deconstruction, merging
     */
    }
