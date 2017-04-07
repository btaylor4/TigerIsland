package net;

public class NetClientMsg {
    class ClientError extends Exception
    {
        public ClientError(String message)
        {
            super(message);
        }
    }
    public String FormatAuthenticationTournament(String tournamentPass) throws ClientError
    {
        if(tournamentPass.isEmpty())
        {
            throw new ClientError("invalid tournament password");
        }

        return String.format("ENTER THUNDERDOME %1$",tournamentPass );
    }
    public String FormatAuthPlayer(String username, String password) throws ClientError
    {
        if(username.isEmpty() || password.isEmpty())
        {
            throw new ClientError("invalid username or password");
        }
        return String.format("I AM %1$ %2$",username, password );
    }
    public String FormatGameMove()
    {
        return "";
    }
    public String FormatBuildAction(String action, String target, int x, int y, int z)
    {
        return String.format("%1$ %2$ AT %3$d %4$d %5$d %6$d", action, target,
                x, y, z );
    }
    public String FormatBuildAction(String action, int x, int y, int z, String terrain)
    {
        return "";
    }
    public String FormatPlaceAction(String title, int x, int y, int z, int orientation)
    {
        return String.format("PLACE %1$ AT %2$ %3$d %4$d %5$d %6$d",
                x, y, z, orientation );
    }
}
