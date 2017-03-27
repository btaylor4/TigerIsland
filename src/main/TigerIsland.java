package main;

public class TigerIsland {

    public static void main(String [] args) {
        boolean game_on = true;

        GameBoard game = new GameBoard();
        Player player1 = new Player(game, 1);
        Player player2 = new Player(game, 2);
        Player currentPlayer ;

        player1.playFirstTile();
        game.printBoard();

        currentPlayer = player2 ;

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
