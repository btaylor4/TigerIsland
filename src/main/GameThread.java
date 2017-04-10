package main;

import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import main.players.BryanAI;
import main.players.HumanPlayer;

public class GameThread implements Runnable{

    GameBoard game;
    BryanAI player1;
    BryanAI player2;
    BryanAI currentPlayer;

    public GameThread(){
        game = new GameBoard();
        player1 = new BryanAI(game,1);
        player2 = new BryanAI(game,2);
        currentPlayer = player2;
    }

    @Override
    public void run() {


        System.out.println(player1.determineTilePlacementByAI().rotation);
        player1.determineBuildByAI();
        game.printBoard();

        currentPlayer = player2 ;

        //server will tell us when game is over
        while (true) {
            System.out.println("Player" + currentPlayer.designator + "'s turn");

            Tile tile = currentPlayer.determineTilePlacementByAI();
            if(tile == null)
            {
                if(player1.getScore() > player2.getScore())
                {
                    System.out.println("Game over! Player: 2 has Lost");
                }

                else
                {
                    System.out.println("Game over! Player: 1 has Lost");
                }

                System.out.println("Player 1 Score: " + player1.getScore());
                System.out.println("Player 2 Score: " + player2.getScore());

                break;
            }

            if(currentPlayer.hasPlayerLost())
            {
                System.out.println("Game over! Player: " + currentPlayer.designator + " has Lost");
                System.out.println("Player 1 Score: " + player1.getScore());
                System.out.println("Player 2 Score: " + player2.getScore());
                break;
            }
            currentPlayer.determineBuildByAI();

            game.printBoard();

            if(currentPlayer.designator == 1)
                currentPlayer = player2 ;
            else
                currentPlayer = player1 ;
        }
    }
}
