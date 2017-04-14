package unitTests;

import main.*;
import main.enums.TerrainType;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static main.utils.formulas.coordinatesToKey;
import static org.junit.Assert.assertFalse;

public class TestSettlement {
    GameBoard game ;
    Tile tile ;
    Player p1 ;
    Player p2 ;


    Settlement ts ;

    @Before
    public void makeGame(){
        game = new GameBoard() ;
        p1 = new Player(game, 1);
        p2 = new Player(game, 2);
        createTestArea();
    }

    public void createTestArea(){
        game.setFirstTile();

        // second tile
        tile = new Tile();
        tile.assignTerrain(TerrainType.LAKE, TerrainType.LAKE);
        tile.rotation = 6 ;
        tile.setHexLevels(1);
        game.setTile(tile, p1.projectTilePlacement(tile, new Point(103, 108)));

        // third tile
        tile = new Tile();
        tile.assignTerrain(TerrainType.GRASS, TerrainType.ROCK);
        tile.rotation = 5 ;
        tile.setHexLevels(1);
        game.setTile(tile, p1.projectTilePlacement(tile, new Point(107, 104)));

        // 4th tile
        tile = new Tile();
        tile.assignTerrain(TerrainType.JUNGLE, TerrainType.JUNGLE);
        tile.rotation = 4 ;
        tile.setHexLevels(1);
        game.setTile(tile, p1.projectTilePlacement(tile, new Point(107, 105)));

        // 5th tile
        tile = new Tile();
        tile.assignTerrain(TerrainType.JUNGLE, TerrainType.ROCK);
        tile.rotation = 5 ;
        tile.setHexLevels(1);
        game.setTile(tile, p1.projectTilePlacement(tile, new Point(106, 107)));

        // 6th guy
        tile = new Tile();
        tile.assignTerrain(TerrainType.ROCK, TerrainType.GRASS);
        tile.rotation = 5 ;
        tile.setHexLevels(1);
        game.setTile(tile, p1.projectTilePlacement(tile, new Point(104, 108)));

        /* nuke with this
        tile = new Tile();
        tile.assignTerrain(TerrainType.ROCKY, TerrainType.ROCKY);
        tile.rotation = 5 ;
        tile.setHexLevels(2);
        game.setTile(tile, p1.projectTilePlacement(tile, new Point(103, 108)));
        */
    }

    @Test
    public void creation(){
        ts = new Settlement(game);
        assertNotNull(ts) ;
    }

    @Test
    public void begin_new(){
        ts = new Settlement(game);
        ts.beginNewSettlement(new Point (104, 105));

        assertTrue(ts.occupantPositions.containsKey(coordinatesToKey(104, 105)));
        assertTrue(ts.size == 1);
    }

    @Test
    public void invalid_begin_new(){
        ts = new Settlement(game);
        ts.beginNewSettlement(new Point (104, 105));

        assertFalse(ts.occupantPositions.isEmpty());
        assertFalse(ts.size != 1);
    }

    @Test
    public void adjacent_additions(){
        ts = new Settlement(game);
        Point point = new Point(102,108);
        ts.addAdjacentTerrains(point);

        assertTrue(ts.lakes.size() == 3);
    }

    @Test
    public void invalid_adjacent_additions(){
        ts = new Settlement(game);
        Point point = new Point(102,108);
        ts.addAdjacentTerrains(point);

        assertFalse(ts.lakes.size() == 0);
    }

    @Test
    public void adjacent_additionsVolcanoes(){
        ts = new Settlement(game);
        Point point = new Point(102,108);
        ts.addAdjacentTerrains(point);

        assertTrue(ts.volcanoes.size() == 2);
    }

    @Test
    public void adjacent_additions_volcanoes(){
        ts = new Settlement(game);
        Point point = new Point(102,108);
        ts.addAdjacentTerrains(point);

        assertFalse(ts.volcanoes.size() == 0);
    }

    @Test
    public void hash_terrain_grass(){
        ts = new Settlement(game);
        Point point = new Point(106,105);
        ts.hashAdjacentTerrain(TerrainType.GRASS, point);

        assertTrue(ts.grasslands.size() == 1);
    }

    @Test
    public void hash_terrain_rocky(){
        ts = new Settlement(game);
        Point point = new Point(106,106);
        ts.hashAdjacentTerrain(TerrainType.ROCK, point);

        assertTrue(ts.rocky.size() == 2);
    }

    @Test
    public void hash_terrain_jungle(){
        ts = new Settlement(game);
        Point point = new Point(108,104);
        ts.hashAdjacentTerrain(TerrainType.JUNGLE, point);

        assertTrue(ts.jungles.size() == 3);
    }

    @Test
    public void hash_terrain_lakes(){
        ts = new Settlement(game);
        Point point = new Point(103,107);
        ts.hashAdjacentTerrain(TerrainType.LAKE, point);

        assertTrue(ts.lakes.size() == 3);
    }

    @Test
    public void hash_terrain_grass_typeOverlook(){
        ts = new Settlement(game);
        Point point = new Point(107,104);
        ts.hashAdjacentTerrain(TerrainType.GRASS, point);

        assertTrue(ts.grasslands.size() == 1);
    }

    @Test
    public void hash_terrain_rocky_typeOverlook(){
        ts = new Settlement(game);
        Point point = new Point(107,104);
        ts.hashAdjacentTerrain(TerrainType.ROCK, point);

        assertTrue(ts.rocky.size() == 1);
    }

    @Test
    public void hash_terrain_jungle_typeOverlook(){
        ts = new Settlement(game);
        Point point = new Point(107,104);
        ts.hashAdjacentTerrain(TerrainType.JUNGLE, point);

        assertTrue(ts.jungles.size() == 1);
    }

    @Test
    public void hash_terrain_lakes_typeOverlook(){
        ts = new Settlement(game);
        Point point = new Point(107,104);
        ts.hashAdjacentTerrain(TerrainType.LAKE, point);

        assertTrue(ts.lakes.size() == 1);
    }


}
