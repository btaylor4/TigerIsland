package main;

import java.util.ArrayList;

public class TigerIsland {

    public static void main(String [] args) {

        GameBoard game1 = new GameBoard();
        GameBoard game2 = new GameBoard();

        Thread Game1player = new Thread(new Player(game1,1));
        Thread Game1opponent = new Thread(new Player(game1,2));

        Thread Game2player = new Thread(new Player(game2,1));
        Thread Game2opponent = new Thread(new Player(game2,2));

        /*
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
        }*/
    }
}
