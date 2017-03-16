/**
 * Created by Bryan on 3/8/17.
 */
public class Player
{
    private int score;
    private int meeples;
    private int tortoro;

    private GameBoard game;

    //possible states of player
    PlayerState beginTurn;
    PlayerState hasTile;
    PlayerState buildPhase;
    PlayerState endTurn;

    //current state of player
    PlayerState currentPlayerState;

    public Player(GameBoard game)
    {
        score = 0;
        meeples = 20;
        tortoro = 3;

        this.game = game;

        beginTurn = new BeginTurn(this);
        hasTile = new HasTile(this);
        buildPhase = new BuildPhase(this);
        endTurn = new EndTurn(this);


        //initialize state
        currentPlayerState = beginTurn;

    }

    public int getScore(){return score;}
    public int getMeeples(){return meeples;}
    public int getTortoro(){return tortoro;}

    public PlayerState getBeginTurnState() {return beginTurn;}
    public PlayerState getHasTileState(){return hasTile;}
    public PlayerState getBuildPhaseState(){return buildPhase;}
    public PlayerState getEndTurnState(){return endTurn;}
    public PlayerState getCurrentPlayerState(){return currentPlayerState;}

    public void decrementMeeple(int meeplesPlayed){meeples -= meeplesPlayed;}
    public void decrementTotoro(){--tortoro;}


    public void placeMeeple()
    {
        //should decrease Meeple if player does it validly
    }

    public void placeTortoro() {
        //should decrease Tortoro if player does it validly
    }

    public void setCurrentPlayerState(PlayerState newPlayerState){
        currentPlayerState = newPlayerState;
    }

    public void drawTile(){
        currentPlayerState.drawTile();
    }

    public void placeTile()
    {
        //should decrease number of tiles if player places validly
        currentPlayerState.placeTile();
    }

    public void foundNewSettlement()
    {
        //should use place meeple method
        currentPlayerState.foundNewSettlement();
    }

    public void expandExistingSettlement()
    {
        //should use place meeple method
        currentPlayerState.expandExistingSettlement();
    }

    public void buildTotoroSanctuary(){
        currentPlayerState.buildTotoroSanctuary();
    }
}
