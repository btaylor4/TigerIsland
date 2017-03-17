/**
 * Created by Bryan on 3/8/17.
 */
public class Player
{
    private int score;
    private int meeples;
    private int tortoro;
    private Tile tileInHand;

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
        tileInHand = null;

        this.game = game;

        beginTurn = new BeginTurn(this);
        hasTile = new HasTile(this);
        buildPhase = new BuildPhase(this);
        endTurn = new EndTurn(this);


        //initialize state
        currentPlayerState = endTurn;

    }

    //local variable accessors
    public int getScore(){return score;}
    public int getMeeples(){return meeples;}
    public int getTortoro(){return tortoro;}
    public Tile getTileInHand(){return tileInHand;}
    public GameBoard getGame(){return game;}

    //local PlayerState accessors
    public PlayerState getBeginTurnState() {return beginTurn;}
    public PlayerState getHasTileState(){return hasTile;}
    public PlayerState getBuildPhaseState(){return buildPhase;}
    public PlayerState getEndTurnState(){return endTurn;}
    public PlayerState getCurrentPlayerState(){return currentPlayerState;}

    public void decrementMeeple(int meeplesPlayed){meeples -= meeplesPlayed;}
    public void decrementTotoro(){--tortoro;}

    //NOTE: decide whether to create new method for notifying player to begin their turn OR use method below and pass in beginTurn
    public void setCurrentPlayerState(PlayerState newPlayerState){
        currentPlayerState = newPlayerState;
    }

    //In Game Moves
    //TODO: grab Tile from GameBoard (does not have method that returns Tile)
    public void drawTile(){
        tileInHand = currentPlayerState.drawTile();
    }

    public void placeTile(Tile tileBeingPlaced, int row, int column)
    {
        //should decrease number of tiles if player places validly
        currentPlayerState.placeTile(tileBeingPlaced, row, column);

        //if tile has been played then it is no longer in hand
        //NOTE: might want to move this condition to HasTile
        if(currentPlayerState == buildPhase){
            tileInHand = null;
        }
    }

//TODO: specify way to identify hexagon for build actions i.e. coordinates or tileID
//TODO: check meeple requirement in order to accomplish build action
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
