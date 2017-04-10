package net;
import java.util.*;
import java.util.function.IntConsumer;
enum ValueType
{
    Int,
    String,
    Vector,
    Tile,
    StringN,
    Float,
    Message,
    Result,
    Function,
    Action,
}

public class NetScanner {
    HashMap<String, ValueType> hm = new HashMap<String, ValueType>();
    private String next;

    public String Buffer;

    private java.util.Scanner sc;
    private ArrayList<String> actions;

    public NetScanner() {
        String[] integers = new String[]{"ROUND", "OF", "PLAY", "MOVE"};
        for (String intVal : integers) {
            hm.put(intVal, ValueType.Int);
        }

        String[] tiles = new String[]{"PLACE", "PLACED"};
        for (String tileTok : tiles) {
            hm.put(tileTok, ValueType.Tile);
        }
        String[] hexVectors = new String[]{"AT"};
        for (String vecTok : hexVectors) {
            hm.put(vecTok, ValueType.Vector);
        }
        String[] funcVectors = new String[]{"BEGIN"};
        for (String vecTok : funcVectors) {
            hm.put(vecTok, ValueType.Function);
        }
        String[] stringVectors = new String[]{"GAME", "PLAYER", "CHALLENGE"};
        for (String vecTok : stringVectors) {
            hm.put(vecTok, ValueType.String);
        }
        String[] stringNVectors = new String[]{"BUILT"};
        for (String vecTok : stringNVectors) {
            hm.put(vecTok, ValueType.StringN);
        }
        String[] floatVectors = new String[]{"WITHIN"};
        for (String vecTok : floatVectors) {
            hm.put(vecTok, ValueType.Float);
        }
        String[] msgVectors = new String[]{"FORFEITED", "LOST"};
        for (String vecTok : msgVectors) {
            hm.put(vecTok, ValueType.Message);
        }
        String[] endVectors = new String[]{"OVER"};
        for (String vecTok : endVectors) {
            hm.put(vecTok, ValueType.Result);
        }
        String[] actionTokens = new String[]{"BUILT", "FOUNDED", "EXPANDED"};
        for (String vecTok : actionTokens) {
            hm.put(vecTok, ValueType.Action);
        }
        //ArrayList<String> actions = new ArrayList<String>(
        //      Arrays.asList( "BUILT", "FOUNDED", "EXPANDED", "BUILT"));

    }

    public void SetBuffer(String buffer) {
        Buffer = buffer.toUpperCase();
        sc = new java.util.Scanner(Buffer);
    }

    public boolean hasNext() {
        return (sc.hasNext());
    }

    public String next1() {
        String current = next;
        next = (sc.hasNext() ? sc.next() : null);
        return current;
    }

    public String peek() {
        return next;
    }

    private String CheckString(String input) {
        input = input.replace(":", "");
        return input;
    }

    private void ScanByType(String tokenString, Token token) {
        if (hm.containsKey(tokenString)) {
            String tokenTypeStr = "TOKEN_" + tokenString;
            token.Type = TokenType.valueOf(tokenTypeStr);
            token.Action = PlayerAction.NONE;
            switch (hm.get(tokenString)) {
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
                case Function:
                    ScanFunction(sc, token);
                    break;
                case Action: {
                    token.Action = PlayerAction.valueOf(tokenString);
                    //ScanAction(sc, token);
                    break;
                }

            }
        } else {
            token.Type = TokenType.TOKEN_EOS;
        }
    }

    // need to make a special case for game over with Player pid
    public Token Scan() {
        String tokenString = CheckString(sc.next());
        Token token = new Token();
        if (tokenString.isEmpty()) {
            token.Type = TokenType.TOKEN_EOS;
        }
        token.Value = tokenString;
        ScanByType(tokenString, token);
        return token;
    }

    private void ScanAction(java.util.Scanner sc, Token token)
    {

    }
    private void ScanFunction(java.util.Scanner sc, Token token)
    {
        if(sc.hasNext()) {

            String functionId = sc.next();

            token.Data = functionId;
            if(sc.hasNext())
            {
                ScanByType(functionId, token);
            }
            else
            {

            }
        }
    }
    private void ScanString(java.util.Scanner sc, Token token)
    {
        if(sc.hasNext()) {
            token.Data = sc.next();
        }
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
        {
            token.Data = (int)sc.nextInt();
        }
        else
        {
            token.Type = TokenType.TOKEN_EOS;
        }
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
        else
            token.Type = TokenType.TOKEN_EOS;

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