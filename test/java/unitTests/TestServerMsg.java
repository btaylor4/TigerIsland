package unitTests;


import net.NetServerMsg;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

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
    public void TestPlayerScore()
    {
        msg.ParseLine("game 4 over player one 500 player two 400");
        HashMap<String, Integer> results = msg.GetGameResults();
        int p1Score = results.get("ONE");
        int p2Score = results.get("TWO");
        assertTrue(p1Score == 500);
        assertTrue(p2Score == 400);
    }

    @Test
    public void TestNumOfRounds()
    {
        msg.ParseLine("BEGIN ROUND 1 OF 5");
        int roundId = msg.GetRoundId();
        int totalRounds = msg.GetTotalRounds();

        assertTrue(roundId == 1);
        assertTrue(totalRounds == 5);
    }

    @Test
    public void TestChallengeAndMatches()
    {
        msg.ParseLine("NEW CHALLENGE NEW_ONE YOU WILL PLAY 2 MATCHES");
        String cid = msg.GetChallengeId();
        int totalMatches = msg.GetNumMatchesToPlay();

        assertTrue(cid.equals("NEW_ONE"));
        assertTrue(totalMatches == 2);
    }

}
