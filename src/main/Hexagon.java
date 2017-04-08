package main;

import main.enums.OccupantType;
import main.enums.TerrainType;
import main.utils.HexPointPair;

import java.util.HashMap;

public class Hexagon {
    public int level ;
    public int tileNumber ;
    public int key ;
    public TerrainType terrain ;
    public OccupantType occupant ;
    public Settlement settlementPointer ;

    public HashMap<Integer, HexPointPair> links ;

    public Hexagon(){
        level = 0;
        occupant = OccupantType.NONE ;
        settlementPointer = null ;

        links = new HashMap<>();
    }

    public void link(Hexagon existingHex, Point existingPoint, Point ownPoint){
        links.put(existingHex.key, new HexPointPair(existingHex, existingPoint));
        existingHex.links.put(this.key, new HexPointPair(this, ownPoint)) ;
    }

    public void unlink(){
        for(HexPointPair attache : links.values()){
            attache.hex.links.remove(this.key);
        }
        links.clear();
    }

}
