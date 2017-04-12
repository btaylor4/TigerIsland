package net;

import main.Hexagon;
import main.Tile;
import main.enums.TerrainType;
import main.ProjectionPack;

import main.utils.XYZ;
import main.Point;

class ClientBuild
{
    protected Point Point;
    protected Tile Tile;
    protected String Action;
    protected String Target;
}
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

    public String FormatBuildAction(String action, String target, TileVector tileVector)
    {
        return String.format("%1$s %2$s %3$s %4$d %5$d %6$d", action, target, "AT",
                tileVector.GetX(), tileVector.GetY(), tileVector.GetZ());
    }

    public String FormatBuildAction(String action, String target, Tile tile){
        XYZ cubePoint = new XYZ(tile.serverPoint);

        return String.format("%1$s %2$s %3$s %4$d %5$d %6$d", action, target, "AT",
                cubePoint.x, cubePoint.y, cubePoint.z);
    }

    public String FormatBuildAction(String action, String target, Point point){
        XYZ cubePoint = new XYZ(point);

        return String.format("%1$s %2$s %3$s %4$d %5$d %6$d", action, target, "AT",
                cubePoint.x, cubePoint.y, cubePoint.z);
    }

    public String FormatBuildAction(ClientBuild cb) {
        XYZ cubePoint = new XYZ(cb.Point);

        return String.format("%1$s %2$s %3$s %4$d %5$d %6$d", cb.Action, cb.Target, "AT",
                cubePoint.x, cubePoint.y, cubePoint.z);
    }

    public String FormatBuildActionWithTerrain(String action, String target, TileVector tileVector) {
        return String.format("%1$s %2$s %3$s %4$d %5$d %6$d %7$s", action, target, "AT",
                tileVector.GetX(), tileVector.GetY(), tileVector.GetZ(),  tileVector.GetTerrain() );
    }

    public String FormatBuildActionWithTerrain(Point point, TerrainType type) {
        XYZ cubePoint = new XYZ(point);

        return String.format("%1$s %2$s %3$s %4$d %5$d %6$d %7$s", "EXPAND", "SETTLEMENT", "AT",
                cubePoint.x, cubePoint.y, cubePoint.z, (type)  );
    }

    public String FormatBuildActionWithTerrain(Tile tile, TerrainType type) {
        XYZ cubePoint = new XYZ(tile.serverPoint);
        return String.format("%1$s %2$s %3$s %4$d %5$d %6$d %7$s", "EXPAND", "SETTLEMENT", "AT",
                cubePoint.x, cubePoint.y, cubePoint.z, (type)   );
    }

    public String FormatBuildActionWithTerrain(String action, String target, Tile tile, TerrainType type) {
        XYZ cubePoint = new XYZ(tile.serverPoint);

        return String.format("%1$s %2$s %3$s %4$d %5$d %6$d %7$s", action, target, "AT",
                cubePoint.x, cubePoint.y, cubePoint.z, (type)  );
    }


    public String FormatPlaceAction(String tile, TileVector tileVector) {
        return String.format("%1$s %2$s %3$s %4$d %5$d %6$d %7$d", "PLACE", tile, "AT",
                tileVector.GetX(), tileVector.GetY(), tileVector.GetZ(), tileVector.GetOrientation() );
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

    public String FormatUnableToBuild()
    {
        return ErrorMessage;
    }

    private XYZ ConvertToXYZ(Point point)
    {
        return new XYZ(point);
    }

}