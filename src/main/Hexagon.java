package main;

import main.enums.OccupantType;
import main.enums.TerrainType;

import java.util.ArrayList;


public class Hexagon {
    public int level ;
    public int tileNumber ;
    public TerrainType terrain ;
    public OccupantType occupant ;

    public Settlement settlementPointer ;
    public ArrayList<Hexagon> adjacencyList ;

    public Hexagon(){
        level = 0;
        occupant = OccupantType.NONE ;
        settlementPointer = null ;
        adjacencyList = null ;
    }
}
