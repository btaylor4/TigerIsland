package main;

import java.io.IOException;

import net.*;


public class TigerIsland {


    static String PID;
    static String opponentPID; //should not really be needed
    static String challengeID;

    static boolean hasProtocolEnded = false;

    static int totalMatches;
    static int totalRounds;
    static int roundID;

    static NetClient client;
    static NetClientMsg msg = new NetClientMsg();
    static NetServerMsg message = new NetServerMsg();

    public static void main(String[] args) {

        Thread game1 = null;
        Thread game2 = null;
        GameThread g1 = null;
        GameThread g2 = null;

        try {
            TournamentAndAuthenticationProtocol(args);

            while(true) {
                challengeProtocolBegin();

                for (int i = 0; i < totalMatches; i++) {
                    roundProtocolBegin();

                    matchProtocolBegin();

                    //**********Move Protocol Begin**********
                    message = client.getNextMessageFromServer();

                    if (message.GetPlayerId() == null) { //we go first
                        g1 = new GameThread(message.GetGameId(), true, client);
                        game1 = new Thread(g1);
                        game1.start();
                        System.out.println("starting game1");
                        g1.currentMessage = message;
                        game1.interrupt();
                    } else { //we go second
                        message = client.getNextMessageFromServer();
                        g2 = new GameThread(message.GetGameId(), false, client);
                        game2 = new Thread(g2);
                        game2.start();
                        System.out.println("starting game2");
                        g2.currentMessage = message;
                        game2.interrupt();
                    }

                    while (!hasProtocolEnded) {
                        message = client.getNextMessageFromServer();
                        if(message.HasProtocolEnded() || message.HasForfeited()) //TODO: check if received a forfeit message
                        {
                            if (message.GetGameId().equals(g1.gameID)){
                                game1.join();
                            } else if (message.GetGameId().equals(g2.gameID)){
                                game2.join();
                            }
                        }
                        //in case other thread has not been started
                        if (game1 == null && !message.GetGameId().equals(g2.gameID)) {
                            g1 = new GameThread(message.GetGameId(), false, client);
                            game1 = new Thread(g1);
                            game1.start();
                            System.out.println("starting gameA");
                            g1.currentMessage = message;
                        }
                        else if (game2 == null && !message.GetGameId().equals(g1.gameID)) {
                            g2 = new GameThread(message.GetGameId(), true, client);
                            game2 = new Thread(g2);
                            game2.start();
                            System.out.println("starting gameB");
                            g2.currentMessage = message;
                        }
                        else if (message.GetGameId().equals(g1.gameID)) {
                            System.out.println("received message for gameA");
                            g1.currentMessage = message;
                            game1.interrupt();
                        }
                        else if (message.GetGameId().equals(g2.gameID)) {
                            System.out.println("received message for gameB");
                            g2.currentMessage = message;
                            game2.interrupt();
                        }
                        else if(message.GetGameResults() != null) {
                            if (message.GetGameId().equals(g1.gameID)) {
                                System.out.println("Ending Thread 1");
                                game1.join();
                            } else if (message.GetGameId().equals(g2.gameID)) {
                                System.out.println("Ending Thread 2");
                                game2.join();
                            }
                        }
                        //unrecognized gid
                        else {
                            System.err.printf("unrecognized game id:" + message.GetGameId());
                        }
                    }
                    ////**********Move Protocol End**********

                    message = client.getNextMessageFromServer();
                    if (message.HasProtocolEnded()){
                        System.out.println("protocol has ended 2");
                        break;
                    }
                }
            }


        } catch (IOException | NullPointerException | InterruptedException e) {
            e.printStackTrace();
        }

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
        client = new NetClient("10.136.15.159", 6969); //IP , port
        client.getNextMessageFromServer();  //receive welcome message
        client.Send(msg.FormatAuthenticationForTournament("heygang"));
        client.getNextMessageFromServer(); //more bs
        client.Send(msg.FormatAuthenticationPlayer("Y", "Y")); // I Am User Password
        message = client.getNextMessageFromServer(); //get the pid here
        PID = message.GetPlayerId();
    }
}
