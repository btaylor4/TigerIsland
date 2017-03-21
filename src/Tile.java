/**
 * Created by jdavi on 3/11/17.
 */
public class Tile {

    private static final short DEGREES_OF_ROTATION = 60 ;
    private static final short DEGREE_THRESHHOLD = 300 ;

    public int rotation ; // default position is volcano in bottom left corner
    public int tileNumber ;

    Hexagon volcano ;
    Hexagon hexA ;
    Hexagon hexB ;

    public Tile(){
        rotation = 0;

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

    public void setHexTileNumbers(int assignedNumber){
        volcano.tileNumber = assignedNumber ;
        hexA.tileNumber = assignedNumber ;
        hexB.tileNumber = assignedNumber ;
    }

    public void assignTileNumber(int assignedNumber){
        this.tileNumber = assignedNumber ;
        setHexTileNumbers(tileNumber);
    }

    public void rotateClockWise(){
        if(rotation == 0)
            rotation = DEGREE_THRESHHOLD ;
        else
            rotation -= DEGREES_OF_ROTATION ;
    }

    public void rotateCounterClockWise(){
        if(rotation == DEGREE_THRESHHOLD)
            rotation = 0 ;
        else
            rotation += DEGREES_OF_ROTATION ;
    }

    public void setRotation(int degree){
        rotation = degree ;
    }

}
