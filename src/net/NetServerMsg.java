package net;
import java.util.ArrayList;
import java.util.HashMap;

import main.enums.TerrainType;
import main.enums.BuildOptions;

public class NetServerMsg {

    NetScanner scanner;
    ArrayList<Token> tokens ;
    public void ParseLine(String line)
    {
        scanner = new NetScanner();
        tokens = new ArrayList<Token>() ;
        scanner.SetBuffer(line);
        Token t;

        while(scanner.hasNext())
        {
            t = scanner.Scan();
            tokens.add(t);

            System.out.println(t.Value);
        }
    }
    public String GetPlayerId()
    {
        Token token = GetTokenByType(TokenType.TOKEN_PLAYER);
        if(token != null)
            return (String)token.Data;
        else {
            token = GetTokenByType(TokenType.TOKEN_BEGIN);
            if (token != null)
                return (String) token.Data;
        }
        return null;
    }
    public int GetMoveId()
    {
        Token token = GetTokenByType(TokenType.TOKEN_MOVE);
        if(token != null)
            return (int) token.Data;
        else
            return -1;
    }
    public String GetGameId()
    {
        Token token = GetTokenByType(TokenType.TOKEN_GAME);
        if(token != null)
            return (String)token.Data;
        else
            return null;
    }
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
        return GetTileStrings();
    }
    public ArrayList<String> GetTileStrings()
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
    public ArrayList<TerrainType> GetTileTerrains()
    {
        Token token = GetTokenByType(TokenType.TOKEN_PLACED);
        if(token != null && ((ArrayList<String>) token.Data) != null) {
            ArrayList<String> terrainList = (ArrayList<String>) token.Data;
            ArrayList<TerrainType> terrains = new ArrayList<TerrainType>();
            for (String terrain : terrainList) {
                TerrainType terrainType = TerrainType.valueOf(terrain);
                terrains.add(terrainType);
            }
            return terrains;
        }
        else {
            token = GetTokenByType(TokenType.TOKEN_PLACE);
            if(token != null && ((ArrayList<String>) token.Data) != null) {
                ArrayList<String> terrainList = (ArrayList<String>) token.Data;
                ArrayList<TerrainType> terrains = new ArrayList<TerrainType>();
                for (String terrain : terrainList) {
                    TerrainType terrainType = TerrainType.valueOf(terrain);
                    terrains.add(terrainType);
                }
                return terrains;
            }
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
    private String CheckBuildString(String input) {
        input = input.replace(" ", "_");
        return input;
    }
    public BuildOptions GetSettlement()
    {
        Token token = GetTokenByType(TokenType.TOKEN_BUILT);
        if(token != null) {
            String option = (String) token.Data;
            return BuildOptions.valueOf(CheckBuildString(option));
        }
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
    public boolean HasProtocolEnded()
    {
        Token token = GetTokenByType(TokenType.TOKEN_END);
        if(token != null)
            return (boolean) token.Data;
        else
            return false;
    }
    public boolean ShouldWaitForNext()
    {
        Token token = GetTokenByType(TokenType.TOKEN_WAIT);
        if(token != null)
            return (boolean) token.Data;
        else
            return false;
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
    public PlayerAction GetAction()
    {
        Token token = GetFunctionToken();
        if(token != null)
            return token.Action;
        else
            return null;
    }
    private Token GetTokenByType(TokenType type)
    {
        for ( Token token : tokens)
        {
            if(token.Type == type && token.Type != TokenType.TOKEN_EOS)
            {
                return token;
            }
        }
        return null;
    }
    private Token GetFunctionToken()
    {
        for ( Token token : tokens)
        {
            if(token.Action != PlayerAction.NONE)
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