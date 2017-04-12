package main;

import main.enums.BuildOptions;
import main.enums.TerrainType;
import main.players.BryanAI;
import main.utils.SettlePointPair;
import main.utils.XYZ;
import net.*;

import java.io.IOException;

import static main.utils.constants.COLUMN_ADDS;
import static main.utils.constants.ROW_ADDS;
import static main.utils.constants.SIDES_IN_HEX;
import static main.utils.formulas.coordinatesToKey;

public class GameThread implements Runnable{

    GameBoard game;

    NetClient client;
    NetServerMsg currentMessage = null;

    String gameID;
    String ourPlayerID = TigerIsland.AIPID;

    int moveNumber;
    private boolean isMyTurn;
    boolean gameOver;

    private BryanAI AI;
    private Player Opponent;

    public GameThread(NetServerMsg message){
        game = new GameBoard();

        gameID = message.GetGameId();
        gameOver = false;

        currentMessage = message;

        if(currentMessage.isMakeMoveMessage()){
            isMyTurn = true;
        } else if (currentMessage.isUpdateMessage()){
            isMyTurn = false;
        }

        AI = new BryanAI(game,Integer.parseInt(TigerIsland.AIPID));
        Opponent = new Player(game,Integer.parseInt(TigerIsland.opponentPID));

    }

    public void processMessage(NetServerMsg protocol){
        if(protocol.isMakeMoveMessage())
        {
            try {
                AIMainMethod();
            }

            catch(Exception e)
            {}
        }

        else{
            replicateOpponentMove();
        }
    }

    @Override
    public void run() {

        //server will tell us when game is over
        while (!gameOver) {
            System.out.println("Game " + gameID +": " + (isMyTurn ? "AI":"Opponent") + "'s turn");

//TODO: maybe add while loop that only breaks when we have a tile
            if(isMyTurn){

                while(true) {
                    try {
                        System.out.println("Game " + gameID + ": " + "Its my turn! I'm going to sleep until client gives me a tile");
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        Thread.interrupted();
                        if(currentMessage.GetMoveTimeLimit() != -1) {
                            break;
                        }
                    }
                }
                System.out.println("Game " + gameID + ": " + "Received message time to make a move");
                try {
                    moveNumber = currentMessage.GetMoveId();
                    AIMainMethod();
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
                        Thread.interrupted();
                        if(currentMessage != null && currentMessage.GetPlayerId() != null &&!currentMessage.GetPlayerId().equals(ourPlayerID) && currentMessage.GetTileTerrains() != null) { //null pointers
                            System.out.println("Game " + gameID + ": " +"Simulating Opponents move");
                            break;
                        }
                        else if (currentMessage != null && currentMessage.GetPlayerId() != null && currentMessage.GetPlayerId().equals(ourPlayerID)){
                            System.out.println("Game " + gameID + ": " + "Ignoring Broadcast of our own move");
                        }
                    }
                }
                //simulate opponents move
                replicateOpponentMove();

            }

            //discard old message
            currentMessage = null;

            //alternate turn
            isMyTurn = !isMyTurn;

        }
    }

    private void AIMainMethod() throws IOException {
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
                foundMe.ownerNumber = Integer.parseInt(TigerIsland.opponentPID);
                foundMe.beginNewSettlement(twoDimensionalPoint);
                Opponent.placeMeeple(twoDimensionalPoint,foundMe);
                game.setSettlement(twoDimensionalPoint,foundMe);
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
