import net.* ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by Thierry on 4/3/2017.
 */

public class Main {
    public static void main(String args[]) {
        NetServerMsg msg = new NetServerMsg();
        //msg.ParseLine("GAME A MOVE 4 PLAYER Player2 PLACED GRASS+WATER AT 1 6 2 backwards EXPANDED SETTLEMENT AT 0 5 4 GRASS");
        //msg.ParseLine("GAME B MOVE 3 PLAYER Player2 PLACED GRASS+WATER AT 1 -3 2 12 BUILT TOTORO SANCTUARY AT 9 2 4 GRASS");
        //msg.ParseLine("GAME B MOVE 3 PLAYER Player2 PLACED GRASS+WATER AT 1 3 2 765 BUILT TIGER PLAYGROUND AT -9 2 4 JUNGLE");
        //msg.ParseLine("MAKE YOUR MOVE IN GAME 4 within 1.54 seconds: move 4 place grass+water ");
        //msg.ParseLine("game 4 move 1 player hellow lost: unable to build");
        msg.ParseLine("game 4 over player one 500 player two 400");
        HashMap<String, Integer> results = msg.GetGameResults();
        String msgStr = msg.GetMessage();

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

    }
}
