
/**
 * Created by jdavi on 3/12/17.
 */
public class TigerIsland
{
    public static void main(String [] args)
    {
        GameBoard game = new GameBoard();
        GraphicsController window = new GraphicsController(game);

        B_Player player1 = new B_Player(game);
        B_Player player2 = new B_Player(game);
        game.setFirstTile();
        while(true)
        {
            System.out.println("Player 1's turn");
            player1.placeTileToPlayOn();
            player1.build();
            window.repaint();
            window.revalidate();
            if(player1.hasWon())
            {
                break;
            }

            System.out.println("Player 2's turn");
            player2.placeTileToPlayOn();
            player2.build();
            window.repaint();
            window.revalidate();
            if(player2.hasWon())
            {
                break;
            }
        }
    }
}
