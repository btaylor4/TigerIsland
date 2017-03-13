import java.util.HashMap;

/**
 * Created by jdavi on 3/12/17.
 */
public class TigerIsland {

    static HashMap testHash = new HashMap() ;

    public static void main(String [] args){
        System.out.println("Well hello muthafucka");

        GameBoard game = new GameBoard();

        game.setFirstTile();
        game.printBoard();

        game.selectTilePlacement(game.tileStack[1], 105, 107);
        game.printBoard();

        game.selectTilePlacement(game.tileStack[2], 107, 107);
        game.printBoard();

        System.out.println("Done");
    }
}
