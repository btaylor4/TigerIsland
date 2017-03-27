package main.states;

import main.* ;

/**
 * StatedPlayer has placed Tile and must now select a build action
 * once build action is executed then statedPlayer ends turn
 */

public class BuildPhase implements PlayerState {

    StatedPlayer statedPlayer;

    public BuildPhase (StatedPlayer statedPlayer){
        this.statedPlayer = statedPlayer;
    }

    @Override
    public Tile drawTile() {
        System.err.println("Cannot Draw Tile during build phase");
        return null;
    }

    @Override
    public void placeTile() {
        System.err.println("Tile already placed");
    }

    @Override
    public void foundNewSettlement() {
        statedPlayer.decrementMeeple(1);
        System.out.println("Settlement Founded");
        statedPlayer.currentPlayerState = statedPlayer.endTurn ;
    }

    @Override
    public void expandExistingSettlement() {
        //method may change since < 1 meeples can be placed at once
        statedPlayer.decrementMeeple(1);
        System.out.println("Settlement Expanded");
        statedPlayer.currentPlayerState = statedPlayer.endTurn;
    }

    @Override
    public void buildTotoroSanctuary() {
        statedPlayer.decrementTotoro();
        System.out.println("Totoro Placed");
        statedPlayer.currentPlayerState = statedPlayer.endTurn;
    }
}