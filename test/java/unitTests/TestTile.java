package unitTests;

import main.Hexagon;
import main.Tile;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Bryan on 4/7/17.
 */
public class TestTile
{
    Tile tile;

    @Before
    public void initializeVariables()
    {
        tile = new Tile();
    }

    @Test
    public void TestNotNullTile()
    {
        assertNotNull(tile);
    }

    @Test
    public void TestNotNullVolcano()
    {
        assertNotNull(tile.volcano);
    }

    @Test
    public void TestNotNullHexA()
    {
        assertNotNull(tile.hexA);
    }

    @Test
    public void TestNotNullHexB()
    {
        assertNotNull(tile.hexB);
    }

    @Test
    public void TestCreationOfTile()
    {
        assertTrue(tile instanceof Tile);
    }

    @Test
    public void TestCreationOfVolcano()
    {
        assertTrue(tile.volcano instanceof Hexagon);
    }

    @Test
    public void TestCreationOfHexA()
    {
        assertTrue(tile.hexA instanceof Hexagon);
    }

    @Test
    public void TestCreationOfHexB()
    {
        assertTrue(tile.hexB instanceof Hexagon);
    }
}
