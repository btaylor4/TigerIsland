package main;

import main.enums.TerrainType;


public class Tile {

    private static final short ROTATION_POSITIONS = 6 ;

    public int rotation ; // default position is volcano in bottom with A up-left, and B up-right
    public int tileNumber ;

    public Hexagon volcano ;
    public Hexagon hexA ;
    public Hexagon hexB ;

    public Tile(){
        rotation = 1;

        volcano = new Hexagon();
        volcano.terrain = TerrainType.VOLCANO ;

        hexA = new Hexagon();
        hexB = new Hexagon();
    }

    public void assignTerrain(TerrainType terrainA, TerrainType terrainB){
        hexA.terrain = terrainA ;
        hexB.terrain = terrainB ;
    }

    public void setHexLevels(int level){
        volcano.level = level ;
        hexA.level = level ;
        hexB.level = level ;
    }

    private void setHexTileNumbers(int assignedNumber){
        volcano.tileNumber = assignedNumber ;
        hexA.tileNumber = assignedNumber ;
        hexB.tileNumber = assignedNumber ;
    }

    public void assignTileNumber(int assignedNumber){
        this.tileNumber = assignedNumber ;
        setHexTileNumbers(tileNumber);
    }

    public void rotateClockWise(){
        if(rotation == ROTATION_POSITIONS)
            rotation = 1 ;
        else
            rotation += 1 ;
    }

    public void rotateCounterClockWise(){
        if(rotation == 1)
            rotation = ROTATION_POSITIONS ;
        else
            rotation -= 1 ;
    }

    public void setRotation(int rotation){
        this.rotation = rotation ;
    }

}
