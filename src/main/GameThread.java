package main;

import main.enums.BuildOptions;
import main.enums.TerrainType;
import main.players.BryanAI;
import main.players.JPAI;
import main.utils.XYZ;
import net.*;

import java.io.IOException;

import static main.utils.constants.COLUMN_ADDS;
import static main.utils.constants.ROW_ADDS;
import static main.utils.constants.SIDES_IN_HEX;

public class GameThread {

    GameBoard game;

    NetClient client;
    NetServerMsg currentMessage = null;

    String gameID;
    String ourPlayerID = MAIN.AIPID;

    int moveNumber;
    private boolean isMyTurn;
    boolean gameOver;

    private JPAI AI;
    private Player Opponent;

    public GameThread(NetServerMsg message, NetClient c){
        game = new GameBoard();

        gameID = message.GetGameId();
        gameOver = false;

        client = c;

        currentMessage = message;

        if(currentMessage.isMakeMoveMessage()){
            isMyTurn = true;
        } else if (currentMessage.isUpdateMessage()){
            isMyTurn = false;
        }

        AI = new JPAI(game,Integer.parseInt(MAIN.AIPID));
        Opponent = new Player(game,Integer.parseInt(MAIN.opponentPID));

        AI.setOpponent(Opponent);

    }

    public void processMessage(NetServerMsg protocol){
        if(protocol.isMakeMoveMessage())
        {
            try {
                moveNumber = protocol.GetMoveId();
                AIMainMethod();
            }

            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        else{
            replicateOpponentMove();
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

        AI.playTilePhase(tileFromServer);
        Tile tile = AI.tileHeld ;

        if(AI.hasPlayerLost()){
            msg = new NetClientMsg();
            String clientMsg = msg.FormatGameMove(gameID, moveNumber, msg.FormatPlaceAction(tile), msg.FormatUnableToBuild());

            client.Send(clientMsg);
        }
        else {
            AI.playBuildPhase();
            buildDecision = AI.buildDecision;
            p = AI.buildPoint;
            tp = AI.terrainSelection;
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
                    clientMsg = msg.FormatGameMove(gameID, moveNumber, msg.FormatPlaceAction(tile), msg.FormatBuildAction("",
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

        //parse tile placement
        tile = new Tile();
        A = opponentsMove.GetTileTerrains().get(0);
        B = opponentsMove.GetTileTerrains().get(1);
        tile.assignTerrain(A,B);

        opponentPlacement = opponentsMove.GetTitlePlacement();
        tile.setRotation(opponentPlacement.GetOrientation());

        xyzTo2DConverter = new XYZ(opponentPlacement.GetX(),opponentPlacement.GetY(),opponentPlacement.GetZ());
        twoDimensionalPoint = xyzTo2DConverter.get2DTranslation();

        Opponent.tilePlacement = twoDimensionalPoint ;
        Opponent.tileHeld = tile;
        Opponent.tileProjection = Opponent.projectTilePlacement(Opponent.tileHeld, Opponent.tilePlacement);
        Opponent.tileProjection.projectedLevel = game.getProjectedHexLevel(Opponent.tileProjection);

        game.setTile(Opponent.tileHeld, Opponent.tileProjection);

        System.out.println("Opponent placing: " + Opponent.tilePlacement.row + " " + Opponent.tilePlacement.column + " "
                + Opponent.tileProjection.volcano.row + " " + Opponent.tileProjection.volcano.column + " " + Opponent.tileHeld.rotation);

        //parse build action
        opponentPlacement = opponentsMove.GetBuildLocation();
        xyzTo2DConverter = new XYZ(opponentPlacement.GetX(),opponentPlacement.GetY(),opponentPlacement.GetZ());
        twoDimensionalPoint = xyzTo2DConverter.get2DTranslation();

        switch (opponentsMove.GetAction()){

            case NONE:
                break;
            case EXPANDED:
                Opponent.buildDecision = BuildOptions.EXPAND ;
                Opponent.selectedSettlement = game.board[twoDimensionalPoint.row][twoDimensionalPoint.column].settlementPointer ;
                A = opponentPlacement.GetTerrainType();
                Opponent.terrainSelection = A ;
                Opponent.buildPoint = twoDimensionalPoint ;
                Opponent.playBuildPhase();
                break;

            case FOUNDED:
                Opponent.buildPoint = twoDimensionalPoint ;
                Opponent.buildDecision = BuildOptions.FOUND_SETTLEMENT;
                Opponent.playBuildPhase();
                break;

            case BUILT:

                buildOption = opponentsMove.GetSettlement();
                Settlement selected = null ;

                for (int j=0; j<SIDES_IN_HEX; j++){
                    if (game.board[twoDimensionalPoint.row+ROW_ADDS[j]][twoDimensionalPoint.column+COLUMN_ADDS[j]].settlementPointer.owner == Opponent){
                        selected = game.board[twoDimensionalPoint.row+ROW_ADDS[j]][twoDimensionalPoint.column+COLUMN_ADDS[j]].settlementPointer;
                        break;
                    }
                }

                Opponent.buildDecision = buildOption ;
                Opponent.buildPoint = twoDimensionalPoint ;
                Opponent.selectedSettlement = selected ;

                Opponent.playBuildPhase();

                break;
        }
    }

}
