package main;

import java.io.IOException;

import net.*;


public class TigerIsland {


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

    public static void main(String[] args) {

        GameThread gameA = null;
        GameThread gameB = null;

        try{
            TournamentAndAuthenticationProtocol(args);

            while(true) { //only break loop if we receive END OF CHALLENGES from server
                challengeProtocolBegin();

                for (int i = 0; i < totalMatches; i++) {//new opponent each iteration of for loop
                    roundProtocolBegin();

                    matchProtocolBegin();

                    //**********Move Protocol Begin**********
                    while(true){

                        message = client.getNextMessageFromServer(); //this message will start one of the GameThreads

                        if(gameA == null){
                            gameA = new GameThread(message,client);
                            System.out.println("Starting game" + gameA.gameID);
                            gameA.processMessage(message);
                        }
                        else if (gameB == null && !message.GetGameId().equals(gameA.gameID)){
                            gameB = new GameThread(message,client);
                            System.out.println("Starting game" + gameB.gameID);
                            gameB.processMessage(message);
                        }
                        else if(!message.isGameOverMessage()) {
                            if ((gameA.gameID.equals(message.GetGameId()) && message.isMakeMoveMessage() ||
                                    (!gameA.gameID.equals(message.GetGameId()) && message.isUpdateMessage()))) {
                                System.out.println("Game" + gameA.gameID + ": message received");
                                gameA.processMessage(message);
                            }
                            else if (gameB.gameID.equals(message.GetGameId()) && message.isMakeMoveMessage() ||
                                    (!gameB.gameID.equals(message.GetGameId()) && message.isUpdateMessage())) {
                                System.out.println("Game" + gameB.gameID + ": message received");
                                gameB.processMessage(message);
                            }
                        }
                        else if(message.isGameOverMessage()){
                            if(gameA.gameID.equals(message.GetGameId())){
                                gameA.gameOver = true;
                                System.out.println("Game" + gameA.gameID + ": ending");
                            }
                            else if (gameB.gameID.equals(message.GetGameId())){
                                gameB.gameOver = true;
                                System.out.println("Game" + gameB.gameID + ": ending");
                            }

                            if (gameA.gameOver && gameB.gameOver){
                                System.out.println("Match Over");
                                break;
                            }
                        }
                        else{
                            System.err.println("Unrecognized message: " + message);
                        }
                    }
                    //match has ended
                }

                if(client.getNextMessageFromServer().isEndChallengeMessage()){
                    //challenge has ended
                    break;
                }
            }
        }
        catch (IOException | NullPointerException e) {
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
        client = new NetClient("10.136.18.24"/*args[1]*/, 1000/*Integer.parseInt(args[2])*/); //IP , port
        client.getNextMessageFromServer();  //WELCOME TO ANOTHER EDITION OF THUNDERDOME!
        client.Send(msg.FormatAuthenticationForTournament("heygang"/*args[3]*/));
        client.getNextMessageFromServer(); //TWO SHALL ENTER, ONE SHALL LEAVE
        client.Send(msg.FormatAuthenticationPlayer("M", "M")); // I Am User Password
        message = client.getNextMessageFromServer(); //WAIT FOR THE TOURNAMENT TO BEGIN <pid>
        AIPID = message.GetPlayerId();
    }
}

//128.227.205.151
//10.136.18.24
//10.192.246.253