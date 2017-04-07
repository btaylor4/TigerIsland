package net;
import java.util.ArrayList;
import java.util.HashMap;

public class NetServerMsg {

    Scanner scanner;
    ArrayList<Token> tokens ;
    public void ParseLine(String line)
    {
        scanner = new Scanner();
        tokens = new ArrayList<Token>() ;
        scanner.SetBuffer(line);
        Token t = new Token();

        while(scanner.hasNext())
        {
            t = scanner.Scan();
            tokens.add(t);
        }
        //PostParse();
    }
    // based on the parse tokens, convert some based on preset rules
    private void PostParse()
    {
        if(tokens.size() <= 1)
            return;
        String function = tokens.get(0).Value;
        Token lastToken = tokens.get(tokens.size() - 1);
    }
    public String GetPlayerId()
    {
        Token token = GetTokenByType(TokenType.TOKEN_PLAYER);
        if(token != null)
            return (String)token.Data;
        else
            return null;
    }
    public String GetGameId()
    {
        Token token = GetTokenByType(TokenType.TOKEN_GAME);
        if(token != null)
            return (String)token.Data;
        else
            return null;    }
    public String GetChallengeId()
    {
        Token token = GetTokenByType(TokenType.TOKEN_CHALLENGE);
        if(token != null)
            return (String)token.Data;
        else
            return null;
    }
    public int GetNumMatchesToPlay()
    {
        return (int)GetTokenByType(TokenType.TOKEN_PLAY).Data;
    }
    public int GetRoundId()
    {
        return (int)GetTokenByType(TokenType.TOKEN_ROUND).Data;
    }
    public ArrayList<String> GetTile()
    {
        Token token = GetTokenByType(TokenType.TOKEN_PLACED);
        if(token != null)
            return (ArrayList<String>)token.Data;
        else {
            token = GetTokenByType(TokenType.TOKEN_PLACE);
            if(token != null)
                return (ArrayList<String>)token.Data;
            return null;
        }
    }
    public String GetMessage()
    {
        Token token = GetTokenByType(TokenType.TOKEN_LOST);
        if(token != null)
            return (String)token.Data;
        else {
            token = GetTokenByType(TokenType.TOKEN_FORFEITED);
            if(token != null)
                return (String)token.Data;
            return null;
        }
    }
    public String GetSettlementName()
    {
        Token token = GetTokenByType(TokenType.TOKEN_BUILT);
        if(token != null)
            return (String)token.Data;
        else
            return null;
    }
    public TileVector GetTitlePlacement()
    {
        Token token = GetTokenByTypeAndIndex(TokenType.TOKEN_AT, 1);
        if(token != null)
            return (TileVector)token.Data;
        else
            return null;
    }
    public TileVector GetBuildLocation()
    {
        Token token = GetTokenByTypeAndIndex(TokenType.TOKEN_AT, 2);
        if(token != null)
            return (TileVector)token.Data;
        else
            return null;
    }
    //BEGIN ROUND i of ROUNDS
    public int GetTotalRounds()
    {
        Token token = GetTokenByType(TokenType.TOKEN_OF);
        if(token != null)
            return (int)token.Data;
        else
            return -1;
    }
    public float GetMoveTimeLimit()
    {
        Token token = GetTokenByType(TokenType.TOKEN_WITHIN);
        if(token != null)
            return (float)token.Data;
        else
            return -1;
    }
    // returns <Player_ID, SCORE>
    public HashMap<String, Integer> GetGameResults()
    {
        Token token = GetTokenByType(TokenType.TOKEN_OVER);
        if(token != null)
            return (HashMap<String, Integer>)token.Data;
        else
            return null;
    }
    private Token GetTokenByType(TokenType type)
    {
        for ( Token token : tokens)
        {
            if(token.Type == type )
            {
                return token;
            }
        }
        return null;
    }

    private Token GetTokenByTypeAndIndex(TokenType type, int index)
    {
        int i = 1;
        for ( Token token : tokens)
        {
            if(token.Type == type && (i++ == index))
            {
                return token;
            }
        }
        return null;
    }
}
