import main.Point;
import main.enums.BuildOptions;
import main.enums.TerrainType;
import main.Tile;

import net.* ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.io.*;
import java.net.*;
import main.utils.*;

/**
 * Created by Thierry on 4/3/2017.
 */

public class Main {
    public static void main(String args[]) {
        NetClientMsg msg = new NetClientMsg();

/*
        client me = null;

        Tile tile = new Tile();
        tile.serverPoint = new Point(101, 105);
        tile.assignTerrain(TerrainType.GRASS, TerrainType.GRASS);
        tile.setRotation(3);
        String buildAct = msg.FormatBuildAction("BUILD", "TOTORO SANCTUARY", tile);
        String errorMsg = msg.FormatUnableToBuild();

        String placeTile = msg.FormatPlaceAction(tile);
        String placeWithTer = msg.FormatBuildActionWithTerrain(tile, TerrainType.ROCK);

        String tournEnter = msg.FormatAuthenticationForTournament("CRAZY");
        String authUser = msg.FormatAuthenticationPlayer("USER", "PASS");

        TileVector tv = new TileVector(90, 91, 92, 4 );
        TileVector tvWithTerrain = new TileVector(90, 91, 92, "GRASS+LAKE" );
        String placeAction = msg.FormatPlaceAction("JUNGLE+ROCK", tv);
        String buildAction = msg.FormatBuildAction("FOUND", "TIGER PLAYGROUND", tv);
        String bAWithTerrain = msg.FormatBuildActionWithTerrain("EXPAND", "SETTLEMENT", tvWithTerrain );

        String gameMove = msg.FormatGameMove("CLOUDS", 4, placeAction, buildAction );

        System.out.println(gameMove);

*/


        //XYZ
        String PID;
        String opponentPID;
        String challengeID;

        int totalMatches;

        int totalRounds;
        int roundID;

        NetClient client;
        NetServerMsg message = new NetServerMsg();

        Thread game1 = null;
        Thread game2 = null;
/*
        public enum TerrainType {
            JUNGLE, LAKE, GRASSLANDS, ROCKY, VOLCANO
        }
        // Good: JUNGLE, LAKE, (VOLCANO) not needed for parser
        // Bad:
        // GRASSLANDS - > GRASS
        // ROCKY -> ROCK
*/
        try {
            //message.ParseLine("GAME B MOVE 3 PLAYER Player2 PLACED GRASS+ROCK AT 1 3 2 -4 BUILT TOTORO SANCTUARY AT 9 2 4 GRASS");

            message.ParseLine("WAIT FOR THE TOURNAMENT TO BEGIN Player1");
            boolean shouldWaitForNext = message.ShouldWaitForNext();


            message.ParseLine("WAIT FOR THE NEXT CHALLENGE TO BEGIN");
            shouldWaitForNext = message.ShouldWaitForNext();
            boolean ended = message.HasProtocolEnded();

            message.ParseLine("END OF CHALLENGES");
            ended = message.HasProtocolEnded();
            shouldWaitForNext = message.ShouldWaitForNext();
            message.ParseLine("END OF ROUND 1 OF 30");
            ended = message.HasProtocolEnded();
            message.ParseLine("END OF ROUND 1 OF 30 WAIT FOR THE NEXT MATCH");
            ended = message.HasProtocolEnded();

            //message.ParseLine("GAME B MOVE 3 PLAYER Player2 PLACED GRASS+ROCK AT 1 3 2 -4 EXPANDED SETTLEMENT AT 9 2 4 GRASS");
            message.ParseLine("GAME B MOVE 3 PLAYER Player2 PLACED GRASS+ROCK AT 1 3 2 -4 BUILT TOTORO SANCTUARY AT 9 2 4 GRASS");
            BuildOptions opt = message.GetSettlement();

            message.ParseLine("GAME B MOVE 3 PLAYER Player2 PLACED JUNGLE+LAKE AT 1 3 2 -4 BUILT TIGER PLAYGROUND AT 9 2 4 Lake");

             opt = message.GetSettlement();

            message.GetTitlePlacement();
            message.GetBuildLocation();


            PlayerAction action = message.GetAction();
            TileVector vec = message.GetTitlePlacement();
            ended = message.HasProtocolEnded();

            XYZ cs = new XYZ(vec.GetX(), vec.GetY(), vec.GetZ());

            Point point = cs.get2DTranslation();
            TileVector bd = message.GetBuildLocation();

            message.ParseLine("GAME B MOVE 3 PLAYER Player2 PLACED GRASS+ROCK AT 1 3 2 -4 FOUNDED SETTLEMENT AT 9 2 4");
            action = message.GetAction();

            message.ParseLine("GAME B MOVE 3 PLAYER Player2 PLACED GRASS+WATER AT 1 3 2 765 BUILT TIGER PLAYGROUND AT -9 2 4");
            action = message.GetAction();




            //**********Authentication Protocol**********
            client = new NetClient(); //IP , port
            client.Start();

            message = client.getNextMessageFromServer();  //receive welcome message


            ArrayList<TerrainType> terrainTypes = message.GetTileTerrains();

            //client.Send(msg.FormatAuthenticationForTournament("TEST"));
            client.getNextMessageFromServer(); //more bs
            //client.Send(msg.FormatAuthenticationPlayer("Team M", "TETS")); // I Am User Password
            message = client.getNextMessageFromServer(); //get the pid here
            PID = message.GetPlayerId();

            //client.Send("test");

            //**********Challenge Protocol**********
            message = client.getNextMessageFromServer();
            challengeID = message.GetChallengeId();
            totalMatches = message.GetNumMatchesToPlay();
            //client.Send("test");

            //**********Round Protocol**********
            message = client.getNextMessageFromServer();
            totalRounds = message.GetTotalRounds();
            roundID = message.GetRoundId();
            //client.Send("test");

            //**********Match Protocol**********
            message = client.getNextMessageFromServer();
            opponentPID = message.GetPlayerId();

            //**********Move Protocol**********
            message = client.getNextMessageFromServer();
            boolean didGoFirst = false;
            if (message.GetPlayerId() == null) { //we go first


            } else { //we go second

            }
            while (true) {
                message = client.getNextMessageFromServer();
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
        try {
            //me=new client(InetAddress.getLocalHost().getHostAddress(), 1025);
            NetClient ntc = new NetClient(); // gets and sets default IP and PORT
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

        /*
        if(me.socket_is_open())
        {
            try {
                me.listening_to_server();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        */

/*
        //NetServerMsg msg = new NetServerMsg();
        //msg.ParseLine("GAME A MOVE 4 PLAYER Player2 PLACED GRASS+WATER AT 1 6 2 backwards EXPANDED SETTLEMENT AT 0 5 4 GRASS");
        //msg.ParseLine("GAME B MOVE 3 PLAYER Player2 PLACED GRASS+WATER AT 1 -3 2 12 BUILT TOTORO SANCTUARY AT 9 2 4 GRASS");
        //msg.ParseLine("GAME B MOVE 3 PLAYER Player2 PLACED GRASS+WATER AT 1 3 2 765 BUILT TIGER PLAYGROUND AT -9 2 4 JUNGLE");
        //TileVector tv1 = msg.GetTitlePlacement();

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
         totalMatches = msg.GetNumMatchesToPlay();
        msg.ParseLine("NEW MATCH BEGINNING NOW YOUR OPPONENT IS PLAYER Player200000000");
        pid = msg.GetPlayerId();

        msg.ParseLine("BEGIN ROUND 1 OF 5");
        int roundId = msg.GetRoundId();
         totalRounds = msg.GetTotalRounds();
        msg.ParseLine("PLACED JUNGLE+LAKE AT 4 4 6 5");
        tv = msg.GetTitlePlacement();
        msg.ParseLine("NEW CHALLENGE 4 YOU WILL PLAY 6 MATCHES");

        msg.ParseLine("WELCOME TO ANOTHER EDITION OF THUNDERDOME");
        */

    }
}