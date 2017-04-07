package main;

import main.enums.TerrainType;

public class StartingTile{

    public Hexagon volcano ;
    public Hexagon hexUpLeft ;
    public Hexagon hexUpRight ;
    public Hexagon hexDownLeft ;
    public Hexagon hexDownRight ;

    public SelfProjection projects ;

    public class SelfProjection{
        Point volcano ;
        Point upLeft ;
        Point upRight ;
        Point downLeft ;
        Point downRight ;

        SelfProjection(){
        }
    }

    public StartingTile(){

        volcano = new Hexagon();
        volcano.terrain = TerrainType.VOLCANO ;

        hexUpLeft = new Hexagon() ;
        hexUpRight = new Hexagon() ;
        hexDownLeft = new Hexagon() ;
        hexDownRight = new Hexagon() ;

        projects = new SelfProjection();
    }

    public void assignTerrain(){
        hexUpLeft.terrain = TerrainType.JUNGLE ;
        hexUpRight.terrain = TerrainType.LAKE ;
        hexDownLeft.terrain = TerrainType.ROCKY ;
        hexDownRight.terrain = TerrainType.GRASSLANDS ;
    }

    public void setHexLevels(int level){
        volcano.level = level ;

        hexUpLeft.level = level ;
        hexUpRight.level = level ;
        hexDownLeft.level = level ;
        hexDownRight.level = level ;
    }

    public void setHexTileNumbers(int assignedNumber){
        volcano.tileNumber = assignedNumber ;
        hexUpLeft.tileNumber = assignedNumber ;
        hexUpRight.tileNumber = assignedNumber ;
        hexDownLeft.tileNumber = assignedNumber ;
        hexDownRight.tileNumber = assignedNumber ;
    }
}
