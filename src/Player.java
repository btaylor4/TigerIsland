/**
 * Created by Bryan on 3/8/17.
 */
public class Player
{
    private int score;
    private int meeples;
    private int tortoro;

    //possible states of player
    PlayerState beginTurn;
    PlayerState hasTile;
    PlayerState buildPhase;
    //PlayerState endTurn;

    //current state of player
    PlayerState currentPlayerState;

    public Player()
    {
        score = 0;
        meeples = 20;
        tortoro = 3;

        beginTurn = new BeginTurn(this);
        hasTile = new HasTile(this);
        buildPhase = new BuildPhase(this);

        //initialize state
        currentPlayerState = beginTurn;

    }

    public int getScore(){return score;}
    public int getMeeples(){return meeples;}
    public int getTortoro(){return tortoro;}

    void setCurrentPlayerState(PlayerState newPlayerState){
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

    public void placeMeeple()
    {
        //should decrease Meeple if player does it validly
        currentPlayerState.placeMeeple();
    }

    public void placeTortoro()
    {
        //should decrease Tortoro if player does it validly
        currentPlayerState.placeTortoro();
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
}
