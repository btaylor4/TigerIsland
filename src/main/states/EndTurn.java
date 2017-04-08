package main.states;

import main.* ;

public class EndTurn implements PlayerState {

    StatedPlayer statedPlayer;

    public EndTurn(StatedPlayer statedPlayer){this.statedPlayer = statedPlayer;}

    @Override
    public Tile drawTile() {
        System.err.println("Not players turn");
        return null;
    }

    @Override
    public void placeTile() {
        System.err.println("Not players turn");
    }

    @Override
    public void foundNewSettlement() {
        System.err.println("Not players turn");
    }

    @Override
    public void expandExistingSettlement() {
        System.err.println("Not players turn");
    }

    @Override
    public void buildTotoroSanctuary() {
        System.err.println("Not players turn");
    }
}