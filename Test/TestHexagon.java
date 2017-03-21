import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Bryan on 3/21/17.
 */
public class TestHexagon
{
    private Hexagon hex;

    @Before
    public void initVariables()
    {
        hex = new Hexagon();
    }

    @Test
    public void testCreateHexagon()
    {
        assertNotNull(hex);
    }

    @Test
    public void testOccupantWhenHexIsInitialized()
    {
        assertTrue(OccupantType.NONE == hex.getOccupant());
    }

    @Test
    public void testOccupantWhenSetOnHex()
    {
        for(OccupantType o: OccupantType.values())
        {
            hex.setOccupantOnHex(o);
            assertTrue(o == hex.getOccupant());
        }
    }

}
