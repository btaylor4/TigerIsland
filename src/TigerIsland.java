import java.util.HashMap;

/**
 * Created by jdavi on 3/12/17.
 */
public class TigerIsland {

    public static void main(String [] args){

        GameBoard game = new GameBoard();

        game.setFirstTile();
        game.printBoard();

        game.tileStack[1].rotateCounterClockWise();
        game.tileStack[1].rotateCounterClockWise();
        game.selectTilePlacement(game.tileStack[1], 103, 106);
        game.printBoard();

        game.tileStack[2].rotateClockWise();
        game.tileStack[2].rotateClockWise();

        game.selectTilePlacement(game.tileStack[2], 108, 106);
        game.printBoard();

        System.out.println("Done");
    }
}
