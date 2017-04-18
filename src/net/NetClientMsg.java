package net;

import main.Hexagon;
import main.Tile;
import main.enums.TerrainType;
import main.ProjectionPack;

import main.utils.XYZ;
import main.Point;

public class NetClientMsg {

    private static String ErrorMessage = "UNABLE TO BUILD";

    public String FormatAuthenticationForTournament(String tournamentPass){
        return String.format("ENTER THUNDERDOME %1$s", tournamentPass);
    }

    public String FormatAuthenticationPlayer(String username, String password){
        return String.format("I AM %1$s %2$s",username, password );
    }

    private String FormatEnum(String action)
    {
        return action.replace('_', ' ');
    }

    public String FormatGameMove(String gameId, int moveNum, String placeAction, String buildAction){
        return String.format("%1$s %2$s %3$s %4$d %5$s %6$s", "GAME", gameId,
                "MOVE", moveNum, placeAction, FormatEnum(buildAction));
    }

    public String FormatBuildAction(String action, String target, Point point){
        XYZ cubePoint = new XYZ(point);

        return String.format("%1$s %2$s %3$s %4$d %5$d %6$d", action, target, "AT",
                cubePoint.x, cubePoint.y, cubePoint.z);
    }


    public String FormatBuildActionWithTerrain(Point point, TerrainType type) {
        XYZ cubePoint = new XYZ(point);

        return String.format("%1$s %2$s %3$s %4$d %5$d %6$d %7$s", "EXPAND", "SETTLEMENT", "AT",
                cubePoint.x, cubePoint.y, cubePoint.z, (type)  );
    }


    public String FormatPlaceAction(Tile tile) {
        if(tile.serverPoint == null || tile.hexA == null || tile.hexB == null)
            return "";

        XYZ cubePoint = new XYZ(tile.serverPoint);
        Hexagon hexA = tile.hexA;
        Hexagon hexB = tile.hexB;

        return String.format("%1$s %2$s+%3$s %4$s %5$d %6$d %7$d %8$d", "PLACE", hexA.terrain.toString(), hexB.terrain.toString(),
                "AT", cubePoint.x, cubePoint.y, cubePoint.z, tile.rotation);
    }

    public String FormatSendOutcome(String gameID, String PID, int AIscore, String opponentID, int opponentScore){
        return String.format("%1$s %2$s %3$s %4$s %5$s %6$d %7$s %8$s %9$d","GAME",gameID, "OVER","PLAYER",PID, AIscore, "PLAYER",opponentID,opponentScore);
    }

    public String FormatUnableToBuild()
    {
        return ErrorMessage;
    }

}