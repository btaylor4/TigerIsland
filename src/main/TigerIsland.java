package main;

import java.util.ArrayList;

public class TigerIsland {

    public static void main(String [] args) {
        boolean game_on = false;

        GameBoard game = new GameBoard();
        Player player1 = new Player(game, 1);
        Player player2 = new Player(game, 2);
        Player currentPlayer ;

        player1.playFirstTile();
        game.printBoard();

        currentPlayer = player2 ;

        ArrayList<Integer> myList = new ArrayList<>();

        System.out.println("Im so big" + myList.size());

        myList.add(22);

        System.out.println("Im so big" + myList.size());

        myList.add(33);

        myList.remove(0);

        System.out.println(myList.get(0));


        while (game_on) {
            System.out.println("Player" + currentPlayer.designator + "'s turn");
            currentPlayer.playTilePhase();
            currentPlayer.build();
            game_on = !currentPlayer.isOutOfPieces();

            if(currentPlayer.designator == 1)
                currentPlayer = player2 ;
            else
                currentPlayer = player1 ;
        }
    }
}
