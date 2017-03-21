import java.util.HashMap;

/**
 * Created by jdavi on 3/12/17.
 */
public class TigerIsland
{
    public static void main(String [] args)
    {
        GameBoard game = new GameBoard();
        B_Player player1 = new B_Player(game);
        B_Player player2 = new B_Player(game);

        player1.placeTileToPlayOn();
        player2.placeTileToPlayOn();
    }
}
