package main;

import java.util.ArrayList;

/**
 * Created by Adam_Soliman on 4/6/2017.
 * Will allow multiple games to run concurrently
 */
public class GameThread implements Runnable{

    GameBoard game;
    Player player1;
    Player player2;
    Player currentPlayer;

    public GameThread(){
        game = new GameBoard();
        player1 = new Player(game,1);
        player2 = new Player(game,2);
    }

    @Override
    public void run() {

        player1.playFirstTile();
        player1.playBuildPhase();
        game.printBoard();

        currentPlayer = player2 ;

        //server will tell us when game is over
        while (true) {
            System.out.println("Player" + currentPlayer.designator + "'s turn");
            currentPlayer.playTilePhase();
            currentPlayer.playBuildPhase();
            game.printBoard();

            if(currentPlayer.designator == 1)
                currentPlayer = player2 ;
            else
                currentPlayer = player1 ;
        }
    }
}
