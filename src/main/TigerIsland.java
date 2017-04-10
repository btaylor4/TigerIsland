package main;

import java.io.IOException;

import net.*;


public class TigerIsland {


    static String PID;
    static String opponentPID;
    static String challengeID;

    static boolean hasProtocolEnded = false;

    static int totalMatches;
    static int totalRounds;
    static int roundID;

    static NetClient client;
    static NetClientMsg msg = new NetClientMsg();
    static NetServerMsg message = new NetServerMsg();

    public static void main(String[] args) {

        Thread game = new Thread(new GameThread());
        game.start();

    }


    private static void matchProtocolBegin() throws IOException {
        message = client.getNextMessageFromServer();
        opponentPID = message.GetPlayerId();
    }

    /*TODO: add response for following message from server (page 6 of document under match protocol)
        Server: GAME <gid> OVER PLAYER <pid> <score> PLAYER <pid> <score>
        Server: GAME <gid> OVER PLAYER <pid> <score> PLAYER <pid> <score>
     */
    private static void matchProtocolEnd() throws IOException {

    }

    private static void roundProtocolBegin() throws IOException {
        message = client.getNextMessageFromServer();
        totalRounds = message.GetTotalRounds();
        roundID = message.GetRoundId();
    }

    /*TODO: add response for following message from server (page 5 of document under round protocol)
        Server: END OF ROUND <rid> OF <rounds>
        or
        Server: END OF ROUND <rid> OF <rounds> WAIT FOR THE NEXT MATCH
     */
    private static void roundProtocolEnd() throws IOException {

    }

    private static void challengeProtocolBegin() throws IOException {
        message = client.getNextMessageFromServer();
        challengeID = message.GetChallengeId();
        totalMatches = message.GetNumMatchesToPlay();
    }

    /*TODO: add response for following message from server (page 5 of document under challenge protocol)
        Server: END OF CHALLENGES
        or
        Server: WAIT FOR THE NEXT CHALLENGE TO BEGIN
    */
    private static void challengeProtocolEnd() throws IOException {

    }

    private static void TournamentAndAuthenticationProtocol(String[] args) throws IOException {
        client = new NetClient("10.136.31.59", 6969); //IP , port
        client.getNextMessageFromServer();  //receive welcome message
        client.Send(msg.FormatAuthenticationForTournament("heygang"));
        client.getNextMessageFromServer(); //more bs
        client.Send(msg.FormatAuthenticationPlayer("M", "M")); // I Am User Password
        message = client.getNextMessageFromServer(); //get the pid here
        PID = message.GetPlayerId();
    }
}
