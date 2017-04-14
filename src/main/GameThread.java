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

    private BryanAI AI;
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

        AI = new BryanAI(game,Integer.parseInt(MAIN.AIPID));
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

        AI.setTile(tileFromServer);
        Tile tile = AI.determineTilePlacementByAI() ;

        if(AI.hasPlayerLost()){
            msg = new NetClientMsg();
            String clientMsg = msg.FormatGameMove(gameID, moveNumber, msg.FormatPlaceAction(tile), msg.FormatUnableToBuild());

            client.Send(clientMsg);
        }
        else {
            AI.determineBuildByAI();
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

        Opponent.tileHeld = tile;
        Opponent.tileProjection = Opponent.projectTilePlacement(tile, twoDimensionalPoint);
        Opponent.tileProjection.projectedLevel = game.getProjectedHexLevel(Opponent.tileProjection);
        Opponent.placeTile();

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

                switch (buildOption)
                {
                    case TOTORO_SANCTUARY:
                        for (int j=0; j<SIDES_IN_HEX; j++)
                        {
                            int row = twoDimensionalPoint.row + ROW_ADDS[j];
                            int column = twoDimensionalPoint.column + COLUMN_ADDS[j];

                            if(game.board[row][column] != null)
                            {
                                Settlement settlementChoice = game.board[row][column].settlementPointer;

                                if(settlementChoice != null)
                                {
                                    if(settlementChoice.owner == Opponent)
                                    {
                                        if(game.isValidTotoroPosition(twoDimensionalPoint, settlementChoice))
                                        {
                                            Opponent.buildDecision = buildOption ;
                                            Opponent.buildPoint = twoDimensionalPoint ;
                                            Opponent.selectedSettlement = settlementChoice ;

                                            Opponent.playBuildPhase();
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        break;

                    case TIGER_PLAYGROUND:
                        for (int j=0; j<SIDES_IN_HEX; j++)
                        {
                            int row = twoDimensionalPoint.row + ROW_ADDS[j];
                            int column = twoDimensionalPoint.column + COLUMN_ADDS[j];

                            if(game.board[row][column] != null) {
                                Settlement settlementChoice = game.board[row][column].settlementPointer;

                                if (settlementChoice != null)
                                {
                                    if (settlementChoice.owner == Opponent)
                                    {
                                        if (game.isValidTigerPosition(twoDimensionalPoint, settlementChoice))
                                        {
                                            Opponent.buildDecision = buildOption;
                                            Opponent.buildPoint = twoDimensionalPoint;
                                            Opponent.selectedSettlement = settlementChoice;

                                            Opponent.playBuildPhase();
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        break;
                }

//                Opponent.buildDecision = buildOption ;
//                Opponent.buildPoint = twoDimensionalPoint ;
//                Opponent.selectedSettlement = selected ;
//
//                Opponent.playBuildPhase();

                break;
        }
    }

}
