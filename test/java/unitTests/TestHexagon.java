package unitTests;
import main.Hexagon;
import main.GameBoard;
import main.Point;
import main.Settlement;
import main.enums.OccupantType;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
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
    public void TestOccupantGetsSettlement()
    {
        Point point = new Point(106,105);
        Settlement settle = new Settlement(game);
        game.setFirstTile();
        game.setPiece(point, OccupantType.MEEPLE, settle);
        assertTrue(game.isValidSettlementPosition(point));
    }

    @Test
    public void TestInvalidOccupantSettlement() {
        Point point = new Point(105, 105);
        Settlement settle = new Settlement(game);
        game.setFirstTile();
        game.setPiece(point, OccupantType.MEEPLE, settle);
        assertFalse(game.isValidSettlementPosition(point));
    }
}
