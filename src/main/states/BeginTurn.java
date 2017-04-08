package main.states;

import main.* ;
/**
 * Created by Adam_Soliman on 3/14/2017.
 *
 * StatedPlayer has just started turn and does not have a Tile yet
 */

public class BeginTurn implements PlayerState{

    StatedPlayer statedPlayer;

    public BeginTurn(StatedPlayer statedPlayer){
        this.statedPlayer = statedPlayer;
    }

    @Override
    public Tile drawTile() {
        System.out.println("Drew Tile");
        statedPlayer.currentPlayerState = statedPlayer.hasTile ;
        return null;
    }

    @Override
    public void placeTile() {
        System.err.println("No Tile to Place");
    }

    @Override
    public void foundNewSettlement() {
        System.err.println("Cannot found Settlement before placing Tile");
    }

    @Override
    public void expandExistingSettlement() {
        System.err.println("Cannot expand Settlement before placing Tile");
    }

    @Override
    public void buildTotoroSanctuary() {
        System.err.println("Cannot place Totoro before placing Tile");
    }
}