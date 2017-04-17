package main;

import main.enums.OccupantType;
import main.enums.TerrainType;
import main.utils.HexPointPair;
import main.utils.SettlePointPair;

import static main.utils.formulas.coordinatesToKey;
import static main.utils.constants.* ;

import java.util.ArrayList;
import java.util.HashMap;

public class Settlement {

    public int size ;
    public int ownerNumber ;
    public int totoroSanctuaries ;
    public int tigerPlaygrounds ;

    public HashMap<Integer, Point> occupantPositions;

    public Player owner ;
    protected GameBoard game ;

    public HashMap<Integer, Point> grasslands;
    public HashMap<Integer, Point> lakes;
    public HashMap<Integer, Point> jungles;
    public HashMap<Integer, Point> rocky;
    public HashMap<Integer, Point> volcanoes;

    public int grassExpansions, lakeExpansions, jungleExpansions, rockyExpansions;

    private ArrayList<Point> mergingSettlements;
    public ArrayList<Point> markedForExpansion;
    public ArrayList<Point> markedForRemoval;
    public HashMap<Integer, Integer> checkTerrains;

    private HashMap<Integer, Integer> countedTerrain;

    public Point endPointToNuke;
    public Point endPoint;

    public Settlement(GameBoard gamePointer) {
        size = 0 ;
        ownerNumber = 0 ;
        totoroSanctuaries = 0;
        tigerPlaygrounds = 0;
        owner = null ;
        game = gamePointer ;

        occupantPositions = new HashMap<>();
        grasslands = new HashMap<>();
        lakes = new HashMap<>();
        jungles = new HashMap<>();
        rocky = new HashMap<>();
        volcanoes = new HashMap<>();

        grassExpansions = lakeExpansions = jungleExpansions = rockyExpansions = 0 ;

        mergingSettlements = new ArrayList<>();
        markedForExpansion = new ArrayList<>();
        markedForRemoval = new ArrayList<>();

        countedTerrain = new HashMap<>();
        endPoint = null;
        endPointToNuke = null;
        checkTerrains = new HashMap<>();
    }

    public void beginNewSettlement(Point point) {
        occupantPositions.put(coordinatesToKey(point.row, point.column), point);
        endPoint = point;
    }

    public void addAdjacentTerrains(Point point) {
        int row, column ;
        boolean present ;

        for (int i = 0; i < SIDES_IN_HEX; i++) {
            row = point.row + ROW_ADDS[i];
            column = point.column + COLUMN_ADDS[i];

            if (game.board[row][column] != null){
                if(game.board[row][column].occupant == OccupantType.NONE) {
                    present = isFreeTerrainPresent(game.board[row][column].terrain, game.board[row][column].key);
                    if(!present) {
                        hashAdjacentTerrain(game.board[row][column].terrain, new Point(row, column));
                    }
                }
            }
        }
    }

    public void hashAdjacentTerrain(TerrainType terrain, Point point){

        int hashKey = coordinatesToKey(point.row, point.column);

        switch(terrain) {
            case JUNGLE:
                jungles.put(hashKey, point);
                break;

            case GRASS:
                grasslands.put(hashKey, point);
                break;

            case VOLCANO:
                volcanoes.put(hashKey, point);
                break;

            case ROCK:
                rocky.put(hashKey, point);
                break;

            case LAKE:
                lakes.put(hashKey, point);
                break;

            default:
                System.out.println("Error: Could not resolve adjacent terrain");
                break;
        }
    }

    private boolean isFreeTerrainPresent(TerrainType terrain, int key){
        switch(terrain) {
            case JUNGLE:
                return jungles.containsKey(key);

            case GRASS:
                return grasslands.containsKey(key);

            case VOLCANO:
                return volcanoes.containsKey(key);

            case ROCK:
                return rocky.containsKey(key);

            case LAKE:
                return lakes.containsKey(key);

            default:
                System.out.println("Error: Could not resolve adjacent terrain");
                break;
        }
        return false;
    }

    public void updateExpansionCounts(){
        grassExpansions = countAdjacentTerrain(grasslands);
        lakeExpansions = countAdjacentTerrain(lakes);
        jungleExpansions = countAdjacentTerrain(jungles);
        rockyExpansions = countAdjacentTerrain(rocky);
    }

    private int countAdjacentTerrain(HashMap<Integer,Point> adjacents){
        int count = 0 ;

        for(Point point : adjacents.values()){
            count++ ;
            countedTerrain.put(game.board[point.row][point.column].key, 1) ;
            count += countPrep(point);
        }

        countedTerrain.clear();

        return count ;
    }

    private int countPrep(Point target) {
        int count = 1 ;
        countedTerrain.put(game.board[target.row][target.column].key, 1) ;

        for (HexPointPair adjacent : game.board[target.row][target.column].links.values()){
            if (!countedTerrain.containsKey(adjacent.hex.key)) {
                count += countPrep(adjacent.point);
            }
        }

        return count ;
    }

    public void expand(TerrainType terrain){
        switch(terrain){
            case GRASS:
                expandThroughTerrain(grasslands);
                break;

            case ROCK:
                expandThroughTerrain(rocky);
                break;

            case LAKE:
                expandThroughTerrain(lakes);
                break;

            case JUNGLE:
                expandThroughTerrain(jungles);
                break;

            case VOLCANO:  // be warned you cannot expand on volcanoes
                expandThroughTerrain(volcanoes);
                break;
        }
    }

    private void expandPrep(Point target) {
        occupantPositions.put(coordinatesToKey(target.row, target.column), target);
        markedForExpansion.add(target);

        for (HexPointPair adjacent : game.board[target.row][target.column].links.values()){
            if (!occupantPositions.containsKey(adjacent.hex.key)) {
                expandPrep(adjacent.point);
            }
        }
    }

    private void checkExpansion(Point target)
    {
        checkTerrains.put(game.board[target.row][target.column].key, 1);
        markedForExpansion.add(target);

        for (HexPointPair adjacent : game.board[target.row][target.column].links.values()){
            if (!checkTerrains.containsKey(adjacent.hex.key)) {
                expandPrep(adjacent.point);
            }
        }
    }

    public int checkNewSettlementSize(HashMap<Integer,Point> expansions)
    {
        int numberOfMeeplesAdded = 0;

        for(Point point : expansions.values()){
            numberOfMeeplesAdded += 1;
        }

        markedForExpansion.clear();

        return numberOfMeeplesAdded;
    }

    public int checkExpansionCost(HashMap<Integer,Point> expansions)
    {
        int numberOfMeeplesLost = 0;

        for(Point point : expansions.values()){
            checkExpansion(point);
        }

        for(Point target : markedForExpansion)
        {
            numberOfMeeplesLost += game.board[target.row][target.column].level;
        }

        markedForExpansion.clear();

        return numberOfMeeplesLost;
    }

    private void expandThroughTerrain(HashMap<Integer,Point> expansions) {
        for(Point point : expansions.values()){
            expandPrep(point);
        }

        for(Point target : markedForExpansion) {
            owner.placeMeeple(target, this);
        }
        expansions.clear();

        for(Point target : markedForExpansion) {
            addAdjacentTerrains(target);
            addAdjacentSettlementsForMerge(target);
        }

        markedForExpansion.clear();
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
                occupantPositions.putAll(game.board[row][column].settlementPointer.occupantPositions);
                jungles.putAll(game.board[row][column].settlementPointer.jungles);
                grasslands.putAll(game.board[row][column].settlementPointer.grasslands);
                lakes.putAll(game.board[row][column].settlementPointer.lakes);
                rocky.putAll(game.board[row][column].settlementPointer.rocky);
                volcanoes.putAll(game.board[row][column].settlementPointer.volcanoes);

                for(Point pt : occupantPositions.values()) {
                    game.board[pt.row][pt.column].settlementPointer = this;
                    owner.playerSettlements.put(coordinatesToKey(pt.row, pt.column), new SettlePointPair(this, pt));
                }
            }
        }


        mergingSettlements.clear();
        cleanTerrainLists();    /* IMPORTANT: remove occupant positions from all adjacent terrain hash maps
                                there will be at least one conflict to resolve */
        endPoint = findEndPoints();
    }

    public void countSettlementMembers(){ // call after merging
        size = 0 ;
        totoroSanctuaries = 0 ;
        tigerPlaygrounds = 0 ;

        for(Point pt : occupantPositions.values()){
            size++ ;

            switch(game.board[pt.row][pt.column].occupant){
                case TOTORO:
                    totoroSanctuaries++ ;
                    break;

                case TIGER:
                    tigerPlaygrounds++ ;
                    break;

                default:
                    break;
            }
        }
    }

    public Point findEndPoints(){
        int min = Integer.MAX_VALUE;
        Point endPoint = null;
        Point testPoint = null;

        for(Point point : occupantPositions.values())
        {
            testPoint = point;
            int row = point.row;
            int column = point .column;
            int numberOfAdjacencies = 0;

            for(int i = 0; i < SIDES_IN_HEX; i++)
            {
                row = point.row + ROW_ADDS[i];
                column = point.column + COLUMN_ADDS[i];

                if(game.board[row][column] != null && game.board[row][column].occupant != OccupantType.MEEPLE)
                {
                    if(game.board[row][column].settlementPointer == this)
                    {
                        numberOfAdjacencies++;
                    }
                }
            }

            if(numberOfAdjacencies < min && numberOfAdjacencies != 0)
            {
                endPoint = point;
                min = numberOfAdjacencies;
                endPointToNuke = point;
            }
        }

        if(endPoint == null) {
            endPointToNuke = testPoint;
            return testPoint;
        }

        else
            return endPoint;
    }

    public int checkPieceAdjacencies(Point point){
        int row = point.row;
        int column = point.column;
        int numberOfAdjacencies = 0;

        for(int i = 0; i < SIDES_IN_HEX; i++)
        {
            row = point.row + ROW_ADDS[i];
            column = point.column + COLUMN_ADDS[i];

            if(game.board[row][column] != null && game.board[row][column].occupant != OccupantType.MEEPLE)
            {
                if(game.board[row][column].settlementPointer == this)
                {
                    numberOfAdjacencies++;
                }
            }
        }

        return numberOfAdjacencies;
    }

    private void cleanTerrainLists(){
        int hashKey ;
        for(Point point : occupantPositions.values()){
            hashKey = coordinatesToKey(point.row, point.column);
            jungles.remove(hashKey);
            grasslands.remove(hashKey);
            lakes.remove(hashKey);
            rocky.remove(hashKey);
            volcanoes.remove(hashKey);
        }
    }

    public void removeFromExpansions(int key){
        jungles.remove(key);
        grasslands.remove(key);
        volcanoes.remove(key);
        rocky.remove(key);
        lakes.remove(key);
    }

    public boolean doesSettlementcontainTotoro()
    {
        for(Point point : occupantPositions.values())
        {
            if(game.board[point.row][point.column].occupant == OccupantType.TOTORO)
                return true;
        }

        return false;
    }

    public boolean doesSettlementcontainTiger()
    {
        for(Point point : occupantPositions.values())
        {
            if(game.board[point.row][point.column].occupant == OccupantType.TIGER)
                return true;
        }

        return false;
    }
}