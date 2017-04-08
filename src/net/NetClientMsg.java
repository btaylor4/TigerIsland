package net;

public class NetClientMsg {
    public class ClientError extends Exception
    {
        public ClientError(String message)
        {
            super(message);
        }
    }
    public String FormatAuthenticationForTournament(String tournamentPass) throws ClientError
    {
        if(tournamentPass.isEmpty())
        {
            throw new ClientError("invalid tournament password");
        }

        return String.format("ENTER THUNDERDOME %1$s", tournamentPass);
    }
    public String FormatAuthenticationPlayer(String username, String password) throws ClientError
    {
        if(username.isEmpty() || password.isEmpty())
        {
            throw new ClientError("invalid username or password");
        }
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
