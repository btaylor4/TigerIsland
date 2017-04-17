package main;

import net.NetClient;
import net.NetClientMsg;
import net.NetServerMsg;

import java.io.IOException;

/**
 * Created by Bryan on 4/13/17.
 */
public class MAIN
{
    static String AIPID;
    static String opponentPID;
    static String challengeID;

    static boolean hasProtocolEnded = false;

    static int totalMatches;
    static int totalRounds;
    static int roundID;

    static NetClient client;
    static NetClientMsg msg = new NetClientMsg();
    static NetServerMsg message = new NetServerMsg();

    public static void main(String[] args)
    {
        try
        {
            TournamentAndAuthenticationProtocol(args);

            while(!client.message.equals("END OF CHALLENGES"))
            {
                challengeProtocolBegin();

                if(client.message.contains("END OF CHALLENGES"))
                {
                    System.exit(0);
                }

                for(int i = 0; i < totalMatches; i++)
                {
                    roundProtocolBegin();

                    matchProtocolBegin();

                    GameThread game1 = null;
                    GameThread game2 = null;

                    while(!client.message.contains("END OF ROUND"))
                    {
                        message = client.getNextMessageFromServer();

                        if(game1 == null)
                        {
                            game1 = new GameThread(message, client);
                            game1.processMessage(message);
                        }

                        else if(game2 == null && !client.message.contains("GAME " + game1.gameID))
                        {
                            game2 = new GameThread(message, client);
                            game2.processMessage(message);
                        }

                        else
                        {
                            if(client.message.contains("END OF ROUND"))
                            {
                                break;
                            }

                            else if(client.message.contains("FORFEITED") || client.message.contains("LOST") || client.message.contains("TIMEOUT") || client.message.contains("ILLEGAL"))
                            {

                            }

                            else if(client.message.contains("GAME OVER"))
                            {
                                if(client.message.contains(game1.gameID))
                                {
                                    game1.gameOver = true;
                                }

                                if(client.message.contains(game2.gameID))
                                {
                                    game2.gameOver = true;
                                }
                            }

                            else if(!game1.gameOver && client.message.contains("GAME " + game1.gameID) && !client.message.contains("PLAYER " + AIPID)
                                    || (client.message.contains("GAME " + game1.gameID) && client.message.contains("MAKE YOUR MOVE")))
                            {
                                if(message.GetMessage() != null && (message.GetMessage().contains("TIMEOUT") || message.GetMessage().contains("ILLEGAL")
                                        || message.GetMessage().contains("UNABLE") || message.GetMessage().contains("MALFROMED")))
                                {
                                    continue;
                                }

                                else
                                    game1.processMessage(message);
                            }

                            else if(game2 != null && !game2.gameOver && client.message.contains("GAME " + game2.gameID) && !client.message.contains("PLAYER " + AIPID)
                                    || (client.message.contains("GAME " + game1.gameID) && client.message.contains("MAKE YOUR MOVE")))
                            {
                                if(message.GetMessage() != null && (message.GetMessage().contains("TIMEOUT") || message.GetMessage().contains("ILLEGAL")
                                        || message.GetMessage().contains("UNABLE") || message.GetMessage().contains("MALFROMED")))
                                {
                                    continue;
                                }
                                else
                                    game2.processMessage(message);
                            }
                        }
                    }
                }
            }
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void matchProtocolBegin() throws IOException {
        message = client.getNextMessageFromServer();
        opponentPID = message.GetPlayerId();
    }

    private static void roundProtocolBegin() throws IOException {
        message = client.getNextMessageFromServer(); //Server: BEGIN ROUND <rid> OF <rounds>
        totalRounds = message.GetTotalRounds();
        roundID = message.GetRoundId();
    }

    private static void challengeProtocolBegin() throws IOException {
        message = client.getNextMessageFromServer(); //NEW CHALLENGE <cid> YOU WILL PLAY <rounds> MATCH
        challengeID = message.GetChallengeId();
        totalMatches = message.GetNumMatchesToPlay();
    }

    private static void TournamentAndAuthenticationProtocol(String[] args) throws IOException {
        client = new NetClient(args[0], Integer.parseInt(args[1])); //IP , port
        client.getNextMessageFromServer();  //WELCOME TO ANOTHER EDITION OF THUNDERDOME!
        client.Send(msg.FormatAuthenticationForTournament(args[2]));
        client.getNextMessageFromServer(); //TWO SHALL ENTER, ONE SHALL LEAVE
        client.Send(msg.FormatAuthenticationPlayer(args[3], args[4])); // I Am User Password
        message = client.getNextMessageFromServer(); //WAIT FOR THE TOURNAMENT TO BEGIN <pid>
        AIPID = message.GetPlayerId();
    }
}
