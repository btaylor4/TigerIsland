package main;

import main.enums.OccupantType;
import main.enums.TerrainType;
import main.utils.constants;

import static main.utils.formulas.coordinatesToKey;

import java.util.ArrayList;
import java.util.HashMap;

public class Settlement {

    public int size ;
    public Player owner ;

    private HashMap<Integer, Point> occupantPositions;

    private HashMap<Integer, Point> grasslands;
    private HashMap<Integer, Point> lakes;
    private HashMap<Integer, Point> forests;
    private HashMap<Integer, Point> rocky;
    private HashMap<Integer, Point> volcanoes;

    private ArrayList<Point> mergingSettlements;

    public Settlement() {
        size = 0 ;
        owner = null ;

        occupantPositions = new HashMap<>();
        grasslands = new HashMap<>();
        lakes = new HashMap<>();
        forests = new HashMap<>();
        rocky = new HashMap<>();
        volcanoes = new HashMap<>();

        mergingSettlements = new ArrayList<>();
    }

    public void beginNewSettlement(Point point) {
        occupantPositions.put(coordinatesToKey(point.row, point.column), point);
    }

    public void addAdjacentTerrains(Point point, Hexagon[][] board) {
        int row, column ;
        int rowAddArray[], columnAddArray[] ;

        if(point.column % 2 == 0){
            rowAddArray = constants.EVEN_ROW_ADDS;
            columnAddArray = constants.EVEN_COLUMN_ADDS;
        }
        else{
            rowAddArray = constants.ODD_ROW_ADDS;
            columnAddArray = constants.ODD_COLUMN_ADDS;
        }

        for (int i = 0; i < constants.SIDES_IN_HEX; i++) {
            row = point.row + rowAddArray[i];
            column = point.column + columnAddArray[i];

            if (board[row][column] != null) {
                hashAdjacentTerrain(board[row][column].terrain, new Point(row, column));
            }
        }
    }

    public void hashAdjacentTerrain(TerrainType terrain, Point point){

        int hashKey = coordinatesToKey(point.row, point.column);

        switch(terrain) {
            case FOREST:
                forests.put(hashKey, point);
                break;

            case GRASS:
                grasslands.put(hashKey, point);
                break;

            case VOLCANO:
                volcanoes.put(hashKey, point);
                break;

            case ROCKY:
                rocky.put(hashKey, point);
                break;

            case WATER:
                lakes.put(hashKey, point);
                break;

            default:
                System.out.println("Error: Could not resolve adjacent terrain");
                break;
        }
    }

    public void addMeeplesForExpansion(TerrainType terrain, Hexagon[][] board){
        switch(terrain){
            case GRASS:
                iterateThroughExpansions(grasslands, board);
                break;

            case ROCKY:
                iterateThroughExpansions(rocky, board);
                break;

            case WATER:
                iterateThroughExpansions(lakes, board);
                break;

            case FOREST:
                iterateThroughExpansions(forests, board);
                break;

            case VOLCANO:  // be warned you cannot expand on volcanoes
                iterateThroughExpansions(volcanoes, board);
                break;
        }
    }

    private void iterateThroughExpansions(HashMap<Integer, Point> expansions, Hexagon[][] board) {
        for(Point point : expansions.values()){
            owner.placeMeeple(point, this);
            addAdjacentSettlementsForMerge(point, board);
        }
        expansions.clear();
    }

    public void addAdjacentSettlementsForMerge(Point point, Hexagon[][] board) {
        int row, column ;
        int rowAddArray[], columnAddArray[] ;

        if(point.column % 2 == 0){
            rowAddArray = constants.EVEN_ROW_ADDS;
            columnAddArray = constants.EVEN_COLUMN_ADDS;
        }
        else{
            rowAddArray = constants.ODD_ROW_ADDS;
            columnAddArray = constants.ODD_COLUMN_ADDS;
        }

        for (int i = 0; i < constants.SIDES_IN_HEX; i++) {
            row = point.row + rowAddArray[i];
            column = point.column + columnAddArray[i];

            if (board[row][column] != null) {
                if(board[row][column].occupant != OccupantType.NONE && board[row][column].settlementPointer != this) {
                    mergingSettlements.add(new Point(row, column));
                }
            }
        }
    }

    public void mergeSettlements(Hexagon[][] board) {
        int row, column ;

        for(Point settlementPoint : mergingSettlements){
            row = settlementPoint.row ;
            column = settlementPoint.column ;

            if(board[row][column].settlementPointer != this) {
                occupantPositions.putAll(board[row][column].settlementPointer.occupantPositions);
                forests.putAll(board[row][column].settlementPointer.forests);
                grasslands.putAll(board[row][column].settlementPointer.grasslands);
                lakes.putAll(board[row][column].settlementPointer.lakes);
                rocky.putAll(board[row][column].settlementPointer.rocky);
                volcanoes.putAll(board[row][column].settlementPointer.volcanoes);

                size += board[row][column].settlementPointer.size ;
                board[row][column].settlementPointer = this ;
            }
        }

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