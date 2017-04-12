package main;

import java.io.IOException;

import net.*;


public class TigerIsland {


    static String AIPID;
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

        try{
            TournamentAndAuthenticationProtocol(args);

            while(true) { //only break loop if we receive END OF CHALLENGES from server
                challengeProtocolBegin();

                for (int i = 0; i < totalMatches; i++) {//new opponent each iteration of for loop
                    roundProtocolBegin();

                    matchProtocolBegin();

                    //**********Move Protocol Begin**********
                    message = client.getNextMessageFromServer(); //this message will start one of the GameThreads

                    if (message.GetPlayerId() == null) { //we go first in Game1

                        g1 = new GameThread(message.GetGameId(), true, client);
                        game1 = new Thread(g1);
                        game1.start();
                        System.out.println("starting gameA"+ message.GetGameId());
                        g1.currentMessage = message;
                        game1.interrupt();

                    }
                    else { //we go second in Game2

                        message = client.getNextMessageFromServer();
                        g2 = new GameThread(message.GetGameId(), false, client);
                        game2 = new Thread(g2);
                        game2.start();
                        System.out.println("starting game" + message.GetGameId());
                        g2.currentMessage = message;
                        game2.interrupt();

                    }

                    while (!hasProtocolEnded) { //TODO: find out when this loop should end. is it on a FORFEIT or GAME <gid> OVER message??
                        message = client.getNextMessageFromServer();
                        if(message.HasProtocolEnded() || message.HasForfeited()) //TODO: check if received a forfeit message
                        {
                            if (g1 != null && message.GetGameId().equals(g1.gameID)){
                                g1.gameOver = true;
                                game1.interrupt();
                                game1.join();
                            }
                            else if (g2 != null && message.GetGameId().equals(g2.gameID)){
                                g2.gameOver = true;
                                game2.interrupt();
                                game2.join();
                            }

                            /*if((!game1.isAlive() && !game2.isAlive())){ //if both threads are dead then its match is over IDK if we need this
                                break;
                            }*/
                        }
                        //if one thread is null and the message gid doesn't match other thread's gid == time to start null thread
                        if (game1 == null && !message.GetGameId().equals(g2.gameID)) {

                            g1 = new GameThread(message.GetGameId(), false, client);
                            game1 = new Thread(g1);
                            game1.start();
                            System.out.println("starting game" + message.GetGameId());
                            g1.currentMessage = message;
                            game1.interrupt();

                        }
                        else if (game2 == null && !message.GetGameId().equals(g1.gameID)) {

                            g2 = new GameThread(message.GetGameId(), true, client);
                            game2 = new Thread(g2);
                            game2.start();
                            System.out.println("starting game"+ message.GetGameId());
                            g2.currentMessage = message;
                            game2.interrupt();

                        }
                        else if (message.GetGameId().equals(g1.gameID) && message.GetTileTerrains() != null) {
                            System.out.println("Received message for game"+ message.GetGameId());
                            g1.currentMessage = message;
                            game1.interrupt();
                        }
                        else if (message.GetGameId().equals(g2.gameID)&& message.GetTileTerrains() != null) {
                            System.out.println("Received message for game"+ message.GetGameId());
                            g2.currentMessage = message;
                            game2.interrupt();
                        }
                        else if(message.GetGameResults() != null) {
                            if (message.GetGameId().equals(g1.gameID)) {
                                System.out.println("Ending Thread 1");
                                g1.gameOver = true;
                                game1.interrupt();
                                game1.join();
                            }
                            else if (message.GetGameId().equals(g2.gameID)) {
                                System.out.println("Ending Thread 2");
                                g2.gameOver = true;
                                game2.interrupt();
                                game2.join();
                            }
                        }
                        else {//unrecognized gid
                            //System.err.printf("IDK what to do with this packet:" + message.GetGameId() + ":" + message);
                        }
                    }

                    //At this point both threads should be done

                    ////**********Move Protocol End**********
                    message = client.getNextMessageFromServer();
                    if (message.HasProtocolEnded()){
                        System.out.println("protocol has ended 2");
                        break;
                    }
                }
            }
        }
        catch (IOException | NullPointerException | InterruptedException e) {
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
        message = client.getNextMessageFromServer(); //Server: BEGIN ROUND <rid> OF <rounds>
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
        message = client.getNextMessageFromServer(); //NEW CHALLENGE <cid> YOU WILL PLAY <rounds> MATCH
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
        client = new NetClient("10.136.18.24"/*args[1]*/, 6969/*Integer.parseInt(args[2])*/); //IP , port
        client.getNextMessageFromServer();  //WELCOME TO ANOTHER EDITION OF THUNDERDOME!
        client.Send(msg.FormatAuthenticationForTournament("heygang"/*args[3]*/));
        client.getNextMessageFromServer(); //TWO SHALL ENTER, ONE SHALL LEAVE
        client.Send(msg.FormatAuthenticationPlayer("A", "A")); // I Am User Password
        message = client.getNextMessageFromServer(); //WAIT FOR THE TOURNAMENT TO BEGIN <pid>
        AIPID = message.GetPlayerId();
    }
}

//128.227.205.151
//10.136.18.24