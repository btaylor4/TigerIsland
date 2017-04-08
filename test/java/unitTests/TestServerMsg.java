package unitTests;

import net.* ;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestServerMsg
{
    NetServerMsg msg;

    @Before
    public void initializeVariables()
    {
        msg = new NetServerMsg();
    }

    @Test
    public void TestPlayerId()
    {
        msg.ParseLine("WAIT FOR THE TOURNAMENT TO BEGIN PLAYER_ONE");
        String pid = msg.GetPlayerId();
        assertTrue(pid.equals("PLAYER_ONE"));
    }
    @Test
    public void TestOpponentPlayerId()
    {
        msg.ParseLine("NEW MATCH BEGINNING NOW YOUR OPPONENT IS PLAYER PLAYER_TWO");
        String pid = msg.GetPlayerId();
        assertTrue(pid.equals("PLAYER_TWO"));
    }
    @Test
    public void TestPlayer1Score()
    {
        msg.ParseLine("game 4 over player one 500 player two 400");
        HashMap<String, Integer> results = msg.GetGameResults();
        int p1Score = results.get("ONE");
        assertTrue(p1Score == 500);
    }
    @Test
    public void TestPlayer2Score()
    {
        msg.ParseLine("game 4 over player one 500 player two 400");
        HashMap<String, Integer> results = msg.GetGameResults();
        int p2Score = results.get("TWO");
        assertTrue(p2Score == 400);
    }

    @Test
    public void TestNumOfRounds()
    {
        msg.ParseLine("BEGIN ROUND 1 OF 5");
        int totalRounds = msg.GetTotalRounds();

        assertTrue(totalRounds == 5);
    }

    @Test
    public void TestRoundId()
    {
        msg.ParseLine("BEGIN ROUND 1 OF 5");
        int roundId = msg.GetRoundId();

        assertTrue(roundId == 1);
    }

    @Test
    public void TestTotalSingleMatch()
    {
        msg.ParseLine("NEW CHALLENGE NEW_ONE YOU WILL PLAY 4 MATCH");
        int totalMatches = msg.GetNumMatchesToPlay();

        assertTrue(totalMatches == 4);
    }

    @Test
    public void TestTotalMatches()
    {
        msg.ParseLine("NEW CHALLENGE NEW_ONE YOU WILL PLAY 2 MATCHES");
        int totalMatches = msg.GetNumMatchesToPlay();

        assertTrue(totalMatches == 2);
    }

    @Test
    public void TestChallengeId()
    {
        msg.ParseLine("NEW CHALLENGE NEW_ONE YOU WILL PLAY 2 MATCHES");
        String cid = msg.GetChallengeId();

        assertTrue(cid.equals("NEW_ONE"));
    }
    @Test
    public void TestChallengeNumOfRounds()
    {
        msg.ParseLine("NEW CHALLENGE NEW_ONE YOU WILL PLAY 2 MATCHES");
        int numToPlay = msg.GetNumMatchesToPlay();

        assertTrue(numToPlay == 2);
    }

    @Test
    public void TestMoveIdMultiple()
    {
        msg.ParseLine("MAKE YOUR MOVE IN GAME 4 within 1.54 seconds: move 1 place grass+water");
        int movId = msg.GetMoveId();

        assertTrue(movId == 1);
    }
    @Test
    public void TestGameIdMultiple()
    {
        msg.ParseLine("MAKE YOUR MOVE IN GAME ABB within 1.54 seconds: move 1 place grass+water");
        String gid = msg.GetGameId();

        assertTrue(gid.equals("ABB"));
    }
    @Test
    public void TestTilePlacement1Multiple()
    {
        msg.ParseLine("MAKE YOUR MOVE IN GAME ABB within 1.54 seconds: move 1 place grass+water");
        ArrayList<String> tile = msg.GetTile();

        String t1 = tile.get(0);
        assertTrue(t1.equals("GRASS"));
    }
    @Test
    public void TestTilePlacement2Multiple()
    {
        msg.ParseLine("MAKE YOUR MOVE IN GAME ABB within 1.54 seconds: move 1 place grass+water");
        ArrayList<String> tile = msg.GetTile();

        String t2 = tile.get(1);
        assertTrue(t2.equals("WATER"));
    }
    @Test
    public void TestTimeLimit()
    {
        msg.ParseLine("MAKE YOUR MOVE IN GAME ABB within 1.54 seconds: move 1 place grass+water");
        Float mvTmLmt = msg.GetMoveTimeLimit();

        assertTrue(mvTmLmt == 1.54f);
    }


    @Test
    public void TestLoseMessage()
    {
        msg.ParseLine("game 4 move 1 player hellow lost: unable to build");
        String message = msg.GetMessage();

        assertTrue(message.equalsIgnoreCase(" unable to build"));
    }
    @Test
    public void TestForfeitedMessage()
    {
        msg.ParseLine("game 4 move 1 player hellow lost: unable to build");
        String message = msg.GetMessage();

        assertTrue(message.equalsIgnoreCase(" unable to build"));
    }

    @Test
    public void TestForEndOfRound1RoundId()
    {
        msg.ParseLine("END OF ROUND 4 OF 10");
        int rid = msg.GetRoundId();

        assertTrue(rid == 4);
    }
    @Test
    public void TestForEndOfRound1RoundTotal()
    {
        msg.ParseLine("END OF ROUND 4 OF 10");
        int totalRounds = msg.GetTotalRounds();

        assertTrue(totalRounds == 10);
    }

    @Test
    public void TestForEndOfRound2RoundId()
    {
        msg.ParseLine("END OF ROUND 5 OF 6 WAIT FOR NEXT MATCH");
        int rid = msg.GetRoundId();

        assertTrue(rid == 5);
    }
    @Test
    public void TestForEndOfRound2RoundTotal()
    {
        msg.ParseLine("END OF ROUND 40 OF 100 WAIT FOR NEXT MATCH");
        int totalRounds = msg.GetTotalRounds();

        assertTrue(totalRounds == 100);
    }

    @Test
    public void TestForEndOfRound2()
    {
        msg.ParseLine("game 4 move 1 player hellow lost: unable to build");
        String message = msg.GetMessage();

        assertTrue(message.equalsIgnoreCase(" unable to build"));
    }
    @Test
    public void TestMoveStatementForGameId()
    {
        msg.ParseLine("GAME B MOVE 3 PLAYER Player2 PLACED GRASS+WATER AT 1 3 2 765 BUILT TIGER PLAYGROUND AT -9 2 4 JUNGLE");
        String gid = msg.GetGameId();

        assertEquals(gid, "B");
    }
    @Test
    public void TestMoveStatementForMoveId()
    {
        msg.ParseLine("GAME B MOVE 3 PLAYER Player2 PLACED GRASS+WATER AT 1 3 2 765 BUILT TIGER PLAYGROUND AT -9 2 4 JUNGLE");
        int mid = msg.GetMoveId();

        assertEquals(mid, 3);
    }
    @Test
    public void TestMoveStatementForPlayerId()
    {
        msg.ParseLine("GAME B MOVE 3 PLAYER Player2 PLACED GRASS+WATER AT 1 3 2 765 BUILT TIGER PLAYGROUND AT -9 2 4 JUNGLE");
        String pid = msg.GetPlayerId();

        assertEquals(pid, "PLAYER2");
    }
    @Test
    public void TestMoveStatementForTile1()
    {
        msg.ParseLine("GAME B MOVE 3 PLAYER Player2 PLACED LAKE+ROCK AT 1 3 2 765 BUILT TIGER PLAYGROUND AT -9 2 4 JUNGLE");
        ArrayList tile = msg.GetTile();

        assertEquals(tile.get(0), "LAKE");
    }
    @Test
    public void TestMoveStatementForTile2()
    {
        msg.ParseLine("GAME B MOVE 3 PLAYER Player2 PLACED LAKE+ROCK AT 1 3 2 765 BUILT TIGER PLAYGROUND AT -9 2 4 JUNGLE");
        ArrayList tile = msg.GetTile();

        assertEquals(tile.get(1), "ROCK");
    }
    @Test
    public void TestMoveStatementForTilePlacementX()
    {
        msg.ParseLine("GAME B MOVE 3 PLAYER Player2 PLACED LAKE+ROCK AT 1 3 2 765 BUILT TIGER PLAYGROUND AT -9 2 4 JUNGLE");
        TileVector tv = msg.GetTitlePlacement();

        assertEquals(tv.GetX(), 1);
    }
    @Test
    public void TestMoveStatementForTilePlacementY()
    {
        msg.ParseLine("GAME B MOVE 3 PLAYER Player2 PLACED LAKE+ROCK AT 1 3 2 765 BUILT TIGER PLAYGROUND AT -9 2 4 JUNGLE");
        TileVector tv = msg.GetTitlePlacement();

        assertEquals(tv.GetY(), 3);
    }
    @Test
    public void TestMoveStatementForTilePlacementZ()
    {
        msg.ParseLine("GAME B MOVE 3 PLAYER Player2 PLACED LAKE+ROCK AT 1 3 2 765 BUILT TIGER PLAYGROUND AT -9 2 4 JUNGLE");
        TileVector tv = msg.GetTitlePlacement();

        assertEquals(tv.GetZ(), 2);
    }
    @Test
    public void TestMoveStatementForTilePlacementOrientation()
    {
        msg.ParseLine("GAME B MOVE 3 PLAYER Player2 PLACED LAKE+ROCK AT 1 3 2 765 BUILT TIGER PLAYGROUND AT -9 2 4 JUNGLE");
        TileVector tv = msg.GetTitlePlacement();

        assertEquals(tv.GetOrientation(), 765);
    }
    @Test
    public void TestMoveStatementForBuildLocationX()
    {
        msg.ParseLine("GAME B MOVE 3 PLAYER Player2 PLACED LAKE+ROCK AT 1 3 2 765 BUILT TIGER PLAYGROUND AT 9 2 4 JUNGLE");
        TileVector tv = msg.GetBuildLocation();

        assertEquals(tv.GetX(), 9);
    }
    @Test
    public void TestMoveStatementForBuildLocationY()
    {
        msg.ParseLine("GAME B MOVE 3 PLAYER Player2 PLACED LAKE+ROCK AT 1 3 2 765 BUILT TIGER PLAYGROUND AT -9 2 4 JUNGLE");
        TileVector tv = msg.GetBuildLocation();

        assertEquals(tv.GetY(), 2);
    }
    @Test
    public void TestMoveStatementForBuildLocationZ()
    {
        msg.ParseLine("GAME B MOVE 3 PLAYER Player2 PLACED LAKE+ROCK AT 1 3 2 765 BUILT TIGER PLAYGROUND AT -9 2 4 JUNGLE");
        TileVector tv = msg.GetBuildLocation();

        assertEquals(tv.GetZ(), 4);
    }
    @Test
    public void TestMoveStatementForBuildLocationTerrain()
    {
        msg.ParseLine("GAME B MOVE 3 PLAYER Player2 PLACED LAKE+ROCK AT 1 3 2 765 BUILT TIGER PLAYGROUND AT -9 2 4 JUNGLE");
        TileVector tv = msg.GetBuildLocation();

        assertEquals(tv.GetTerrain(), "JUNGLE");
    }
    @Test
    public void TestMoveStatementSettlementTotoro()
    {
        msg.ParseLine("GAME B MOVE 3 PLAYER Player2 PLACED LAKE+ROCK AT 1 3 2 765 BUILT TOTORO SANCTUARY AT -9 2 4 JUNGLE");
        String settlementName = msg.GetSettlementName();

        assertEquals(settlementName, "TOTORO SANCTUARY");
    }
    @Test
    public void TestMoveStatementSettlementTiger()
    {
        msg.ParseLine("GAME B MOVE 3 PLAYER Player2 PLACED LAKE+ROCK AT 1 3 2 765 BUILT TIGER PLAYGROUND AT -9 2 4 JUNGLE");
        String settlementName = msg.GetSettlementName();

        assertEquals(settlementName, "TIGER PLAYGROUND");
    }
}