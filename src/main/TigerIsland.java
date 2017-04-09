package main;

import java.io.IOException;

import main.players.BryanAI;
import net.*;
import java.util.ArrayList;
import java.util.HashMap;


public class TigerIsland {

    public static void main(String [] args) {

        //client will be started here and will send responses to corresponding game
        //GameThread will need access to client in order to send moves out

        String PID;
        String opponentPID;
        String challengeID;

        int totalMatches;

        int totalRounds;
        int roundID;

        NetClient client;
        NetClientMsg msg = new NetClientMsg();
        NetServerMsg message = new NetServerMsg();

        Thread game1 = null;
        Thread game2 = null;

        GameThread g1 = null;
        GameThread g2 = null;

        try {

            //**********Authentication Protocol**********
            client = new NetClient(args[1], Integer.parseInt(args[2])); //IP , port
            client.Start();

            
>>>>>>> dd77ddcd1a9baef19a69475dba0930be065bf480
            client.getNextMessageFromServer();  //receive welcome message
            client.Send(msg.FormatAuthenticationForTournament(args[3]));
            client.getNextMessageFromServer(); //more bs
            client.Send(msg.FormatAuthenticationPlayer("Team M", args[4])); // I Am User Password
            message = client.getNextMessageFromServer(); //get the pid here
            PID = message.GetPlayerId();


            //**********Challenge Protocol**********
            message = client.getNextMessageFromServer();
            challengeID = message.GetChallengeId();
            totalMatches = message.GetNumMatchesToPlay();

            //**********Round Protocol**********
            message = client.getNextMessageFromServer();
            totalRounds = message.GetTotalRounds();
            roundID = message.GetRoundId();

            //**********Match Protocol**********
            message = client.getNextMessageFromServer();
            opponentPID = message.GetPlayerId();

            //**********Move Protocol**********
            message = client.getNextMessageFromServer();
            boolean didGoFirst = false;
            if (message.GetPlayerId() == null) { //we go first
                g1 = new GameThread(message.GetGameId(), true, client);
                game1 = new Thread(g1);
                game1.start();
                System.out.println("starting game1");
            } else { //we go second
                message = client.getNextMessageFromServer();
                g2 = new GameThread(message.GetGameId(), false, client);
                game2 = new Thread(g2);
                game2.start();
                System.out.println("starting game2");
            }
            while (true) {
                message = client.getNextMessageFromServer();

                if (game1 == null && !message.GetGameId().equals(g2.gameID)){
                    g1 = new GameThread(message.GetGameId(), false, client);
                    game1 = new Thread(g1);
                    game1.start();
                    System.out.println("starting game1");
                }
                else if(game2 == null && !message.GetGameId().equals(g1.gameID)){
                    g2 = new GameThread(message.GetGameId(), true, client);
                    game2 = new Thread(g2);
                    game2.start();
                    System.out.println("starting game2");
                }
                else if (message.GetGameId().equals(g1.gameID)){
                    System.out.println("received message for game1");
                }
                else if (message.GetGameId().equals(g2.gameID)){
                    System.out.println("received message for game2");
                }
                else{
                    System.err.printf("unrecognized game id:" + message.GetGameId());
                }

            }




        } catch (IOException e) {
            e.printStackTrace();
        }
















        /*
        try {
            //me=new client(InetAddress.getLocalHost().getHostAddress(), 1025);
            ntc = new NetClient(); // gets and sets default IP and PORT
            if(ntc.IsConnected())
            {
                ntc.Listen();

                NetClientMsg msg = new NetClientMsg();
                msg.FormatAuthenticationForTournament("CRAZY");
                msg.FormatAuthenticationPlayer("USER", "PASS");
            }
        } catch (IOException | NetClientMsg.ClientError e) {
            e.printStackTrace();
        }

        NetServerMsg msg = new NetServerMsg();


        msg.ParseLine("MAKE YOUR MOVE IN GAME 4 within 1.54 seconds: move 1 place grass+water ");
        int mId = msg.GetMoveId();

        msg.ParseLine("game 4 move 1 player hellow lost: unable to build");
        String msgStr = msg.GetMessage();

        msg.ParseLine("game 4 over player one 500 player two 400");
        HashMap<String, Integer> results = msg.GetGameResults();
        int w = results.get("ONE");
        int y = results.get("TWO");
        Float mvTmLmt = msg.GetMoveTimeLimit();
        String settlementName = msg.GetSettlementName();
        TileVector tv = msg.GetTitlePlacement();
        ArrayList<String> tile = msg.GetTile();
        String gid = msg.GetGameId();
        String pid = msg.GetPlayerId();

        TileVector bdl = msg.GetBuildLocation();
        msg.ParseLine("NEW CHALLENGE 20 YOU WILL PLAY 2 MATCHES");
        String cid = msg.GetChallengeId();
        int totalMatches = msg.GetNumMatchesToPlay();
        msg.ParseLine("NEW MATCH BEGINNING NOW YOUR OPPONENT IS PLAYER Player200000000");
        pid = msg.GetPlayerId();

        msg.ParseLine("BEGIN ROUND 1 OF 5");
        int roundId = msg.GetRoundId();
        int totalRounds = msg.GetTotalRounds();
        msg.ParseLine("PLACED JUNGLE+LAKE AT 4 4 6 5");
        tv = msg.GetTitlePlacement();
        msg.ParseLine("NEW CHALLENGE 4 YOU WILL PLAY 6 MATCHES");

        msg.ParseLine("WELCOME TO ANOTHER EDITION OF THUNDERDOME");
          */
    }
}