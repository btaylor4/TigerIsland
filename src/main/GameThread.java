package main;

import java.lang.management.OperatingSystemMXBean;

public class GameThread implements Runnable{

    GameBoard game;

    int gameID;

    Player AI;
    Player Opponent;
    Player currentPlayer;

    //TODO: add client to constructor args
    public GameThread(int gameNumber, int designatorOfWhoGoesFirst){
        game = new GameBoard();

        gameID = gameNumber;

        AI = new Player(game,1);
        Opponent = new Player(game,2);

        if(designatorOfWhoGoesFirst == 1){
            currentPlayer = AI;
        }
        else{
            currentPlayer = Opponent;
        }
    }

    @Override
    public void run() {

        //AI.playFirstTile();
        //AI.playBuildPhase();
        //game.printBoard();

        //server will tell us when game is over
        while (true) {
            System.out.println("Game " + gameID +": Player" + currentPlayer.designator + "'s turn");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //currentPlayer.playTilePhase();
            //currentPlayer.playBuildPhase();
            //game.printBoard();

            if(currentPlayer.designator == 1)
                currentPlayer = Opponent;
            else
                currentPlayer = AI;
        }
    }
}
