package main;

import main.enums.BuildOptions;
import main.enums.TerrainType;
import main.players.BryanAI;
import main.utils.XYZ;
import net.*;

import java.io.IOException;

import static main.utils.constants.COLUMN_ADDS;
import static main.utils.constants.ROW_ADDS;
import static main.utils.constants.SIDES_IN_HEX;

public class GameThread implements Runnable{

    GameBoard game;

    NetClient client;
    NetServerMsg currentMessage = null;

    String gameID;
    String ourPlayerID = TigerIsland.PID;

    int moveNumber;
    boolean isMyTurn;
    boolean gameOver;

    BryanAI AI;
    Player Opponent;

    public GameThread(String gameNumber, boolean weGoFirst, NetClient c){
        game = new GameBoard();

        client = c;

        gameID = gameNumber;
        gameOver = false;

        AI = new BryanAI(game,1);
        Opponent = new Player(game,2);

        isMyTurn = weGoFirst;

        if(weGoFirst){
            moveNumber = 1;
        }
        else{
            moveNumber = 2;
        }

    }

    @Override
    public void run() {

        //server will tell us when game is over
        while (!gameOver) {
            System.out.println("Game " + gameID +": " + (isMyTurn ? "AI":"Opponent") + "'s turn");


            if(isMyTurn){
                try {
                    System.out.println("Game " + gameID +": " +"Its my turn! I'm going to sleep until client gives me a tile");
                    Thread.sleep(10000);
                } catch (InterruptedException e) {

                }

                try {
                    makeAIMove();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else { //its opponents turn

                while (!isMyTurn) {
                    try {
                        System.out.println("Game " + gameID + ": " +"Its NOT my turn! I'm going to sleep until opponent makes move");
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        if(currentMessage != null && !currentMessage.GetPlayerId().equals(ourPlayerID)) {
                            System.out.println("Game " + gameID + ": " +"Simulating Opponents move");
                            break;
                        }
                        else if (currentMessage != null && currentMessage.GetPlayerId().equals(ourPlayerID)){
                            System.out.println("Game " + gameID + ": " + "Ignoring Broadcast of our own move");
                        }
                    }
                }
                //simulate opponents move
                replicateOpponentMove();

            }

            //game.printBoard();

            isMyTurn = !isMyTurn;

        }
    }

    private void makeAIMove() throws IOException {
        Tile tileFromServer = new Tile();
        TerrainType A;
        TerrainType B;

        A = currentMessage.GetTileTerrains().get(0);
        B = currentMessage.GetTileTerrains().get(1);
        tileFromServer.assignTerrain(A,B);


        NetClientMsg msg;
        TerrainType tp;
        BuildOptions buildDecision;
        Point p;

        AI.setTile(tileFromServer);
        Tile tile = AI.determineTilePlacementByAI();

        if(AI.hasPlayerLost()){
            msg = new NetClientMsg();
            String clientMsg = msg.FormatGameMove(gameID, moveNumber, msg.FormatPlaceAction(tile), msg.FormatUnableToBuild());

            client.Send(clientMsg);
        }
        else {
            AI.determineBuildByAI();
            buildDecision = AI.buildDecision;
            p = AI.buildPoint;
            tp = AI.expansionAction;
            msg = new NetClientMsg();

            switch (buildDecision) {
                case TIGER_PLAYGROUND:
                    String clientMsg = msg.FormatGameMove(gameID, moveNumber, msg.FormatPlaceAction(tile), msg.FormatBuildAction("BUILD",
                            buildDecision.toString(), p));

                    client.Send(clientMsg);
                    break;

                case TOTORO_SANCTUARY:
                    clientMsg = msg.FormatGameMove(gameID, moveNumber, msg.FormatPlaceAction(tile), msg.FormatBuildAction("BUILD",
                            buildDecision.toString(), p));
                    client.Send(clientMsg);
                    break;

                case EXPAND:
                    clientMsg = msg.FormatGameMove(gameID, moveNumber, msg.FormatPlaceAction(tile), msg.FormatBuildActionWithTerrain(p,
                            tp));

                    client.Send(clientMsg);
                    break;

                case FOUND_SETTLEMENT:
                    clientMsg = msg.FormatGameMove(gameID, moveNumber, msg.FormatPlaceAction(tile), msg.FormatBuildAction("BUILD",
                            buildDecision.toString(), p));
                    client.Send(clientMsg);
                    break;

                default:
                    System.err.print("Error, invalid AI build option") ;
                    break;
            }
        }
    }

    private void replicateOpponentMove(){
        NetServerMsg opponentsMove = currentMessage;

        BuildOptions buildOption;

        Tile tile;
        TerrainType A;
        TerrainType B;

        TileVector opponentPlacement;

        XYZ xyzTo2DConverter;
        Point twoDimensionalPoint;

        moveNumber = opponentsMove.GetMoveId() + 1;

        //parse tile placement
        tile = new Tile();
        A = opponentsMove.GetTileTerrains().get(0);
        B = opponentsMove.GetTileTerrains().get(1);
        tile.assignTerrain(A,B);

        opponentPlacement = opponentsMove.GetTitlePlacement();
        tile.setRotation(opponentPlacement.GetOrientation());

        xyzTo2DConverter = new XYZ(opponentPlacement.GetX(),opponentPlacement.GetY(),opponentPlacement.GetZ());
        twoDimensionalPoint = xyzTo2DConverter.get2DTranslation();

        //place the tile
        game.setTile(tile,new ProjectionPack(twoDimensionalPoint));

        //parse build action
        opponentPlacement = opponentsMove.GetBuildLocation();
        xyzTo2DConverter = new XYZ(opponentPlacement.GetX(),opponentPlacement.GetY(),opponentPlacement.GetZ());
        twoDimensionalPoint = xyzTo2DConverter.get2DTranslation();

        switch (opponentsMove.GetAction()){

            case NONE:
                break;
            case EXPANDED:
                A = opponentPlacement.GetTerrainType();
                game.board[twoDimensionalPoint.row][twoDimensionalPoint.column].settlementPointer.expand(A);
                break;
            case FOUNDED:
                buildOption = BuildOptions.FOUND_SETTLEMENT;
                Settlement foundMe = new Settlement(game);
                foundMe.owner = Opponent;
                foundMe.beginNewSettlement(twoDimensionalPoint);
                game.setSettlement(twoDimensionalPoint,foundMe);
                Opponent.placeMeeple(twoDimensionalPoint,foundMe);
                break;
            case BUILT:
                buildOption = opponentsMove.GetSettlement();
                Settlement addTotoroOrTigerToMe = null;
                for (int j=0; j<SIDES_IN_HEX; j++){
                    if (game.board[twoDimensionalPoint.row+ROW_ADDS[j]][twoDimensionalPoint.column+COLUMN_ADDS[j]].settlementPointer.owner == Opponent){
                        addTotoroOrTigerToMe = game.board[twoDimensionalPoint.row+ROW_ADDS[j]][twoDimensionalPoint.column+COLUMN_ADDS[j]].settlementPointer;
                    }
                }

                if(buildOption == BuildOptions.TIGER_PLAYGROUND){
                    Opponent.placeTiger(twoDimensionalPoint,addTotoroOrTigerToMe);
                } else if (buildOption == BuildOptions.TOTORO_SANCTUARY) {
                    Opponent.placeTotoro(twoDimensionalPoint,addTotoroOrTigerToMe);
                }

                break;
        }
    }

}
