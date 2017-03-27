package main;

import main.enums.OccupantType;
import main.enums.TerrainType;


public class Hexagon {
    public int level ;
    public int tileNumber ;
    public TerrainType terrain ;
    public OccupantType occupant ;
    public Settlement settlementPointer ;

    public Hexagon(){
        level = 0;
        occupant = OccupantType.NONE ;
    }
}
