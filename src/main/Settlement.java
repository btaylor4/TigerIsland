package main;

import main.enums.OccupantType;
import main.enums.TerrainType;

import static main.utils.formulas.coordinatesToKey;
import static main.utils.constants.* ;

import java.util.ArrayList;
import java.util.HashMap;

public class Settlement {

    public int size ;
    public int ownerNumber ;
    public int hasTotoro;
    public int hasTiger;
    public Player owner ;
    private GameBoard game ;


    public HashMap<Integer, Point> occupantPositions;

    public HashMap<Integer, Point> grasslands;
    public  HashMap<Integer, Point> lakes;
    public HashMap<Integer, Point> forests;
    public HashMap<Integer, Point> rocky;
    public HashMap<Integer, Point> volcanoes;

    private ArrayList<Point> mergingSettlements;
    public ArrayList<Point> markedForRemoval ;

    public Settlement(GameBoard gamePointer) {
        size = 0 ;
        ownerNumber = 0 ;
        owner = null ;
        game = gamePointer ;
        hasTotoro = 0;
        hasTiger = 0;

        occupantPositions = new HashMap<>();
        grasslands = new HashMap<>();
        lakes = new HashMap<>();
        forests = new HashMap<>();
        rocky = new HashMap<>();
        volcanoes = new HashMap<>();

        mergingSettlements = new ArrayList<>();
        markedForRemoval = new ArrayList<>();
    }

    public void beginNewSettlement(Point point) {
        occupantPositions.put(coordinatesToKey(point.row, point.column), point);
        size = 1;
    }

    public void addAdjacentTerrains(Point point) {
        int row, column ;
        boolean present ;

        for (int i = 0; i < SIDES_IN_HEX; i++) {
            row = point.row + ROW_ADDS[i];
            column = point.column + COLUMN_ADDS[i];

            if (game.board[row][column] != null){
                if(game.board[row][column].occupant == OccupantType.NONE) {
                    present = isFreeTerrainPresent(game.board[row][column].terrain, new Point(row, column));
                    if(!present) {
                        hashAdjacentTerrain(game.board[row][column].terrain, new Point(row, column));
                    }
                }
            }
        }
    }

    private void recursivelyAddMatchingTerrain(Point origin){
        int row, column ;
        boolean matching, present ;

        for (int i = 0; i < SIDES_IN_HEX; i++) {
            row = origin.row + ROW_ADDS[i];
            column = origin.column + COLUMN_ADDS[i];

            if (game.board[row][column] != null) {
                matching = (game.board[row][column].terrain == game.board[origin.row][origin.column].terrain) ;
                present = isFreeTerrainPresent(game.board[row][column].terrain, new Point(row, column)) ;

                if((matching && (!present)) && (game.board[row][column].occupant == OccupantType.NONE)){
                    hashAdjacentTerrain(game.board[row][column].terrain, new Point(row, column));
                }
            }
        }
    }

    public void hashAdjacentTerrain(TerrainType terrain, Point point){

        int hashKey = coordinatesToKey(point.row, point.column);

        switch(terrain) {
            case JUNGLE:
                forests.put(hashKey, point);
                break;

            case GRASSLANDS:
                grasslands.put(hashKey, point);
                break;

            case VOLCANO:
                volcanoes.put(hashKey, point);
                break;

            case ROCKY:
                rocky.put(hashKey, point);
                break;

            case LAKE:
                lakes.put(hashKey, point);
                break;

            default:
                System.out.println("Error: Could not resolve adjacent terrain");
                break;
        }

        recursivelyAddMatchingTerrain(point);
    }

    private boolean isFreeTerrainPresent(TerrainType terrain, Point point){
        int hashKey = coordinatesToKey(point.row, point.column);

        switch(terrain) {
            case JUNGLE:
                return forests.containsKey(hashKey);

            case GRASSLANDS:
                return grasslands.containsKey(hashKey);

            case VOLCANO:
                return volcanoes.containsKey(hashKey);

            case ROCKY:
                return rocky.containsKey(hashKey);

            case LAKE:
                return lakes.containsKey(hashKey);

            default:
                System.out.println("Error: Could not resolve adjacent terrain");
                break;
        }
        return false;
    }

    public void expand(TerrainType terrain){
        switch(terrain){
            case GRASSLANDS:
                expandThroughTerrain(grasslands);
                break;

            case ROCKY:
                expandThroughTerrain(rocky);
                break;

            case LAKE:
                expandThroughTerrain(lakes);
                break;

            case JUNGLE:
                expandThroughTerrain(forests);
                break;

            case VOLCANO:  // be warned you cannot expand on volcanoes
                expandThroughTerrain(volcanoes);
                break;
        }
    }

    private void expandThroughTerrain(HashMap<Integer,Point> expansions ){
        for(Point point : expansions.values()){
            System.out.println("espanding " + point.row + " " + point.column);
            size++ ;
            owner.placeMeeple(point, this);
            occupantPositions.put(coordinatesToKey(point.row, point.column), point) ;
            addAdjacentSettlementsForMerge(point);
        }
        expansions.clear();
    }

    public void addAdjacentSettlementsForMerge(Point point) {
        int row, column ;
        boolean occupied, differentSettlement, sameOwner = false ;

        for (int i = 0; i < SIDES_IN_HEX; i++) {
            row = point.row + ROW_ADDS[i];
            column = point.column + COLUMN_ADDS[i];

            if(game.board[row][column] != null){
                occupied = game.board[row][column].occupant != OccupantType.NONE ;
                differentSettlement = game.board[row][column].settlementPointer != this ;
                if(occupied){
                    sameOwner = (game.board[row][column].settlementPointer.owner == this.owner) ;
                }

                if(occupied && differentSettlement && sameOwner) {
                    mergingSettlements.add(new Point(row, column));
                }
            }
        }
    }

    public void mergeSettlements() {
        int row, column ;

        for(Point settlementPoint : mergingSettlements){
            row = settlementPoint.row ;
            column = settlementPoint.column ;

            if(game.board[row][column].settlementPointer != this) {
                System.out.println("merging:" + game.board[row][column].settlementPointer + " with : " + this);

                occupantPositions.putAll(game.board[row][column].settlementPointer.occupantPositions);
                forests.putAll(game.board[row][column].settlementPointer.forests);
                grasslands.putAll(game.board[row][column].settlementPointer.grasslands);
                lakes.putAll(game.board[row][column].settlementPointer.lakes);
                rocky.putAll(game.board[row][column].settlementPointer.rocky);
                volcanoes.putAll(game.board[row][column].settlementPointer.volcanoes);

                size += game.board[row][column].settlementPointer.size ;

                for(Point pt : occupantPositions.values()) {
                    game.board[pt.row][pt.column].settlementPointer = this;
                    owner.playerSettlements.put(coordinatesToKey(pt.row, pt.column), this);
                }
            }
        }

        mergingSettlements.clear();
        cleanTerrainLists();    /* IMPORTANT: remove occupant positions from all adjacent terrain hash maps
                                there will be at least one conflict to resolve */
    }

    private void cleanTerrainLists(){
        int hashKey ;
        for(Point point : occupantPositions.values()){
            hashKey = coordinatesToKey(point.row, point.column);
            forests.remove(hashKey);
            grasslands.remove(hashKey);
            lakes.remove(hashKey);
            rocky.remove(hashKey);
            volcanoes.remove(hashKey);
        }
    }
}