package main;

import main.states.* ;

public class StatedPlayer {
    public int score;
    public int meeplesHeld;
    public int totoroHeld;
    public Tile tileInHand;

    public GameBoard game;

    public PlayerState beginTurn;
    public PlayerState hasTile;
    public PlayerState buildPhase;
    public PlayerState endTurn;
    public PlayerState currentPlayerState;

    public StatedPlayer(GameBoard game)
    {
        score = 0;
        meeplesHeld = 20;
        totoroHeld = 3;
        tileInHand = null;

        this.game = game;

        beginTurn = new BeginTurn(this);
        hasTile = new HasTile(this);
        buildPhase = new BuildPhase(this);
        endTurn = new EndTurn(this);

        currentPlayerState = endTurn;
    }

    public void decrementMeeple(int meeplesPlayed){
        meeplesHeld -= meeplesPlayed;
    }

    public void decrementTotoro(){
        --totoroHeld;
    }

    //NOTE: decide whether to create new method for notifying player to begin their turn OR use method below and pass in beginTurn
    public void setCurrentPlayerState(PlayerState newPlayerState){
        currentPlayerState = newPlayerState;
    }

    //In Game Moves
    //TODO: grab Tile from GameBoard (does not have method that returns Tile)
    public void drawTile(){
        tileInHand = currentPlayerState.drawTile();
    }

    public void placeTile(Tile tileBeingPlaced, Point selectedPoint){
        //should decrease number of tiles if player places validly
        //if tile has been played then it is no longer in hand
        //NOTE: might want to move this condition to HasTile
        if(currentPlayerState == buildPhase){
            tileInHand = null;
        }
    }

    //TODO: specify way to identify hexagon for build actions i.e. coordinates or tileID
//TODO: check meeple requirement in order to accomplish build action
    public void foundNewSettlement(){
        //should use place meeple method
        currentPlayerState.foundNewSettlement();
    }

    public void expandExistingSettlement(){
        //should use place meeple method
        currentPlayerState.expandExistingSettlement();
    }

    public void buildTotoroSanctuary(){
        currentPlayerState.buildTotoroSanctuary();
    }
}