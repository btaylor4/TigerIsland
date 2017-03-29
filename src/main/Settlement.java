package main;

import main.enums.OccupantType;
import main.enums.TerrainType;

import java.util.ArrayList;
import java.util.HashMap;

public class Settlement
{
    private static final int EVEN_ROW_ADDS[] = {-1, -1, -1, 0, 1, 0};
    private static final int EVEN_COLUMN_ADDS[] = {-1, 0, 1, 1, 0, -1};
    private static final int ODD_ROW_ADDS[] = {0,-1,0,1,1,1};
    private static final int ODD_COLUMN_ADDS[] = {-1,0,1,1,0,-1};
    private static final int SIDES_IN_HEX = 6 ;

    public int size ;
    public Player owner ;

    private HashMap<Point, Integer> collectionOfPoints;
    private HashMap<TerrainType, Point> grasslands;
    private HashMap<TerrainType, Point> lakes;
    private HashMap<TerrainType, Point> forests;
    private HashMap<TerrainType, Point> rocky;
    private HashMap<TerrainType, Point> volcanos;
    private ArrayList<Settlement> adjacentSettlementsToMerge;

    public Settlement() {
        size = 0 ;
        owner = null ;

        collectionOfPoints = new HashMap<>();
        grasslands = new HashMap<>();
        lakes = new HashMap<>();
        forests = new HashMap<>();
        rocky = new HashMap<>();
        volcanos = new HashMap<>();
    }

    public void createNewSettlement(Point point)
    {
        collectionOfPoints.put(point, 1);
    }

    public boolean checkExistingSettlement(Point point){
        if(collectionOfPoints.containsKey(point))
            return false;
        else
            return true;
    }

    public void addMeeplesToSettlement(TerrainType terrain, Hexagon[][] board) {
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

            case VOLCANO:
                iterateThroughExpansions(volcanos, board);
                break;
        }
    }

    private void iterateThroughExpansions(HashMap<TerrainType, Point> expansions, Hexagon[][] board) {
        for(Point point : expansions.values()){
            for(int i = 0; i < owner.getHexLevel(point); i++){
                owner.placeMeeple(point, this);
            }

            addAdjacentSettlements(point, board);
        }
    }

    public void addAdjacentSettlements(Point point, Hexagon[][] board) {
        int row, column ;
        int rowAddArray[], columnAddArray[] ;

        if(point.column % 2 == 0){
            rowAddArray = EVEN_ROW_ADDS;
            columnAddArray = EVEN_COLUMN_ADDS;
        }
        else{
            rowAddArray = ODD_ROW_ADDS;
            columnAddArray = ODD_COLUMN_ADDS;
        }

        for (int i = 0; i < SIDES_IN_HEX; i++) {
            row = point.row + rowAddArray[i];
            column = point.column + columnAddArray[i];

            if (board[row][column] != null) {
                if(board[row][column].occupant != OccupantType.NONE && !collectionOfPoints.containsKey(point)) {
                    adjacentSettlementsToMerge.add(board[row][column].settlementPointer);
                }
            }
        }
    }

    public void addAdjacentTerrains(Point point, Hexagon[][] board) {
        int row, column ;
        int rowAddArray[], columnAddArray[] ;

        if(point.column % 2 == 0){
            rowAddArray = EVEN_ROW_ADDS;
            columnAddArray = EVEN_COLUMN_ADDS;
        }
        else{
            rowAddArray = ODD_ROW_ADDS;
            columnAddArray = ODD_COLUMN_ADDS;
        }

        for (int i = 0; i < SIDES_IN_HEX; i++) {
            row = point.row + rowAddArray[i];
            column = point.column + columnAddArray[i];

            if (board[row][column] != null) {
                addTerrainAdjacencies(board[row][column].terrain, new Point(row, column));
            }
        }
    }

    public void addTerrainAdjacencies(TerrainType terrain, Point point){
        switch(terrain) {
            case FOREST:
                forests.put(terrain, point);
                break;

            case GRASS:
                grasslands.put(terrain, point);
                break;

            case VOLCANO:
                volcanos.put(terrain, point);
                break;

            case ROCKY:
                rocky.put(terrain, point);
                break;

            case WATER:
                lakes.put(terrain, point);
                break;

            default:
                System.out.println("Error: Could not resolve adjacent terrain");
                break;
        }
    }

    public void mergeSettlements(ArrayList<Settlement> settlements) {
        for(Settlement settlement : adjacentSettlementsToMerge){
            for(Point point : settlement.collectionOfPoints.keySet()){
                collectionOfPoints.put(point, 1);
            }

            settlements.remove(settlement);
        }
    }
}