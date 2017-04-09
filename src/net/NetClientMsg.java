package net;

public class NetClientMsg {

    public String FormatAuthenticationForTournament(String tournamentPass)
    {

        return String.format("ENTER THUNDERDOME %1$s", tournamentPass);
    }
    public String FormatAuthenticationPlayer(String username, String password)
    {

        return String.format("I AM %1$s %2$s",username, password );
    }

    public String FormatGameMove(String gameId, int moveNum, String placeAction, String buildAction)
    {
        return String.format("%1$s %2$s %3$s %4$d %5$s %6$s", "GAME", gameId,
                "MOVE", moveNum, placeAction, buildAction);
    }
    public String FormatBuildAction(String action, String target, TileVector tileVector)
    {
        return String.format("%1$s %2$s %3$s %4$d %5$d %6$d", action, target, "AT",
                tileVector.GetX(), tileVector.GetY(), tileVector.GetZ());
    }
    public String FormatBuildActionWithTerrain(String action, String target, TileVector tileVector)
    {
        return String.format("%1$s %2$s %3$s %4$d %5$d %6$d %7$s", action, target, "AT",
                tileVector.GetX(), tileVector.GetY(), tileVector.GetZ(),  tileVector.GetTerrain() );
    }
    public String FormatPlaceAction(String tile, TileVector tileVector)
    {
        return String.format("%1$s %2$s %3$s %4$d %5$d %6$d %7$d", "PLACE", tile, "AT",
                tileVector.GetX(), tileVector.GetY(), tileVector.GetZ(), tileVector.GetOrientation() );
    }
}
