import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Bryan on 3/17/17.
 */
public class TestTile
{
    @Test
    public void TestTileCreation()
    {
        Tile tile = new Tile();
        assertNotNull(tile);
        assertTrue("Tile object is a tile", tile instanceof Tile);
    }

    @Test
    public void TestTileIsNotAnotherObject()
    {
        Tile tile = new Tile();
        assertTrue("Tile object is a tile", tile instanceof Tile);
    }
}
