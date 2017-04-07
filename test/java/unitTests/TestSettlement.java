package unitTests;

import main.GameBoard;
import main.Settlement;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class TestSettlement {
    GameBoard testGame ;
    Settlement ts ;

    @Before
    public void makeGame(){
        testGame = new GameBoard() ;
    }

    @Test
    public void creation(){
        ts = new Settlement(testGame);
        assertNotNull(ts) ;
    }




}
