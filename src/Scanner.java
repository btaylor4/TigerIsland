package net;
import java.util.*;

enum ValueType
{
    Int,
    String,
    Vector,
    Tile,
    StringN,
    Float,
    Message,
    Result
}

public class Scanner {
    HashMap<String, ValueType> hm = new HashMap<String, ValueType>();

    public String Buffer;

    private java.util.Scanner sc;

    public Scanner()
    {
        String[] integers = new String[] { "ROUND", "OF", "PLAY", "MOVE"};
        for(String intVal:  integers) {
            hm.put(intVal, ValueType.Int);
        }

        String[] tiles = new String[] { "PLACE", "PLACED" };
        for(String tileTok:  tiles) {
            hm.put(tileTok, ValueType.Tile);
        }
        String[] hexVectors = new String[] { "AT" };
        for(String vecTok:  hexVectors) {
            hm.put(vecTok, ValueType.Vector);
        }
        String[] stringVectors = new String[] { "GAME", "PLAYER", "CHALLENGE" };
        for(String vecTok:  stringVectors) {
            hm.put(vecTok, ValueType.String);
        }
        String[] stringNVectors = new String[] { "BUILT"};
        for(String vecTok:  stringNVectors) {
            hm.put(vecTok, ValueType.StringN);
        }
        String[] floatVectors = new String[] { "WITHIN"};
        for(String vecTok:  floatVectors) {
            hm.put(vecTok, ValueType.Float);
        }
        String[] msgVectors = new String[] { "FORFEITED", "LOST"};
        for(String vecTok:  msgVectors) {
            hm.put(vecTok, ValueType.Message);
        }
        String[] endVectors = new String[] { "OVER"};
        for(String vecTok:  endVectors) {
            hm.put(vecTok, ValueType.Result);
        }
    }

    public void SetBuffer(String buffer)
    {
        Buffer = buffer.toUpperCase();
        sc = new java.util.Scanner(Buffer);
    }
    public boolean hasNext()
    {
        return sc.hasNext();
    }
    private String CleanString(String input)
    {
        input = input.replace(":", "");
        return input;
    }
    // need to make a special case for game over with Player pid
    public Token Scan()
    {
        String tokenString = CleanString(sc.next());
        Token token = new Token();
        if(tokenString.isEmpty())
        {
            token.Type = TokenType.TOKEN_EOS;
        }
        token.Value = tokenString;
        if(hm.containsKey(tokenString))
        {
            String tokenTypeStr = "TOKEN_" + tokenString;
            token.Type = TokenType.valueOf(tokenTypeStr);
            switch (hm.get(tokenString))
            {
                case Int:
                    ScanInt(sc, token);
                    break;
                case Float:
                    ScanFloat(sc, token);
                    break;
                case Vector:
                    ScanVector(sc, token);
                    break;
                case Tile:
                    ScanTile(sc, token);
                    break;
                case String:
                    ScanString(sc, token);
                    break;
                case StringN:
                    ScanStringN(sc, token);
                    break;
                case Message:
                    ScanMessage(sc, token);
                    break;
                case Result:
                    ScanGameResult(sc, token);
                    break;
            }
        }
        return token;
    }
    private void ScanString(java.util.Scanner sc, Token token)
    {
        if(sc.hasNext())
            token.Data = sc.next();
    }
    public static int NStringTerminator = 2;
    private void ScanStringN(java.util.Scanner sc, Token token)
    {
        int i =0;
        String tokenStr = "";
        while ( i < NStringTerminator)
        {
            tokenStr += sc.next();
            i++;
            if(i != NStringTerminator)
                tokenStr += " ";
        }
        token.Data = tokenStr;
    }
    private void ScanVector(java.util.Scanner sc, Token token)
    {
        TileVector tileVector = new TileVector(sc.nextInt(), sc.nextInt(), sc.nextInt());

        if(sc.hasNextInt())
        {
            tileVector.SetOrientation(sc.nextInt());
        }
        else if(sc.hasNext())
        {
            tileVector.SetTerrain(sc.next());
        }
        token.Data = tileVector;
    }
    private void ScanTile(java.util.Scanner sc, Token token)
    {
        String tileData = sc.next();
        String[] tiles = tileData.split("\\+");
        token.Data = new ArrayList<String>(Arrays.asList(tiles));
    }
    private void ScanInt(java.util.Scanner sc, Token token)
    {
        if(sc.hasNextInt())
            token.Data = sc.nextInt();
    }
    private void ScanMessage(java.util.Scanner sc, Token token)
    {
        if(sc.hasNext()){
            String msg = sc.nextLine();
            if(msg != null)
                token.Data = msg ;
        }
    }
    private void ScanFloat(java.util.Scanner sc, Token token)
    {
        if(sc.hasNextFloat())
            token.Data = sc.nextFloat();
    }
    private void ScanGameResult(java.util.Scanner sc, Token token)
    {
        HashMap<String, Integer> playerResult = new HashMap<String, Integer>();
        String playerStr = "";
        for(int x = 0; x < 2 && sc.hasNext(); x++)
        {
            playerStr = sc.next();
            if(playerStr.equalsIgnoreCase("PLAYER")) {
                String pid = sc.next();
                int score = sc.nextInt();
                playerResult.put(pid, score);
            }
        }
        token.Data = playerResult;
    }
}
