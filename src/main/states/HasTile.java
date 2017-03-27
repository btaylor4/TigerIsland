package main.states;
import main.* ;

/**
 * StatedPlayer has received Tile and must now place Tile
 */

public class HasTile implements PlayerState {

    StatedPlayer statedPlayer;

    public HasTile(StatedPlayer statedPlayer){
        this.statedPlayer = statedPlayer;
    }

    @Override
    public Tile drawTile() {
        System.err.println("Already Drew Tile");
        return statedPlayer.tileInHand ;
    }

    @Override
    public void placeTile() {

    }

    @Override
    public void foundNewSettlement() {
        System.err.println("Must place Tile before founding Settlement");
    }

    @Override
    public void expandExistingSettlement() {
        System.err.println("Must place Tile before expanding Settlement");
    }

    @Override
    public void buildTotoroSanctuary() {
        System.err.println("Must place Tile before building Totoro");
    }
}