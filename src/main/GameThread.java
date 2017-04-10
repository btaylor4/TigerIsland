package main;

import main.players.BryanAI;
import net.*;

public class GameThread implements Runnable{

    GameBoard game;

    NetClient client;

    String gameID;
    boolean isMyTurn;
    boolean gameOver;

    BryanAI AI;
    Player Opponent;

    //TODO: add client to constructor args
    public GameThread(String gameNumber, boolean weGoFirst, NetClient c){
        game = new GameBoard();

        client = c;

        gameID = gameNumber;
        gameOver = false;

        AI = new BryanAI(game,1);
        Opponent = new Player(game,2);

        isMyTurn = weGoFirst;

    }

    @Override
    public void run() {

        //server will tell us when game is over
        while (!gameOver) {
            System.out.println("Game " + gameID +": " + (isMyTurn ? "AI":"Opponent") + "'s turn");


            if(isMyTurn){
                AI.determineTilePlacementByAI();
                AI.determineBuildByAI();
            }
            else { //its opponents turn

                while (!isMyTurn) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
                //simulate opponents move
                client.GetCurrentMessage();

            }

            //game.printBoard();

            isMyTurn = !isMyTurn;

        }
    }

    //these will change once client is visible
    public void applyOpponentsMoves(){
        Opponent.playTilePhase();
        Opponent.playBuildPhase();
    }

    public void gameIsOver(){
        gameOver = true;
    }

}
