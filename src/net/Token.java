package net;

enum TokenType
{
    TOKEN_EOS,
    TOKEN_STRING,
    TOKEN_BEGIN,
    TOKEN_PLAY,
    TOKEN_CHALLENGE,
    TOKEN_ROUNDID,
    TOKEN_ROUNDS,
    TOKEN_TIME,
    TOKEN_PID,
    TOKEN_SCORE,
    TOKEN_TERRAIN,
    TOKEN_TILE,

    TOKEN_AT,
    TOKEN_ROUND,
    TOKEN_GAME,
    TOKEN_PLAYER,
    TOKEN_PLACE,
    TOKEN_MOVE,
    TOKEN_PLACED,
    TOKEN_OF,
    TOKEN_BUILT,
    TOKEN_WITHIN,
    TOKEN_LOST,
    TOKEN_FORFEITED,
    TOKEN_OVER
}

public class Token {
    public Object Data;
    public TokenType Type;
    public String Value;
}
