package main.players.AIUtils;

import main.GameBoard;
import main.Point;
import main.Settlement;
import main.utils.HexPointPair;

import java.util.HashMap;

public class SettlementData {
    public int size ;
    public int grassCost, lakeCost, jungleCost, rockyCost, paddyCost ;
    public int afterGrass, afterLake, afterJungle, afterRocky, afterPaddy ;

    public Settlement settle;
    public GameBoard game ;

    private HashMap<Integer, Integer> countedTerrain;

    public HashMap<Integer, Point> level2Adjacencies ;
    public HashMap<Integer, Point> level2Occupants ;
    public HashMap<Integer, Point> level3Adjacencies ;
    public HashMap<Integer, Point> greaterLevelAdjacencies ;
    public HashMap<Integer, Point> greaterLevelOccupants ;

    public HashMap<Integer, Point> nonFloodAdjacencies ;

    public SettlementData(Settlement ptr, GameBoard game){
        settle = ptr ;
        this.game = game ;

        countedTerrain = new HashMap<>() ;

        level2Adjacencies = new HashMap<>() ;
        level2Occupants = new HashMap<>() ;
        level3Adjacencies = new HashMap<>() ;
        greaterLevelAdjacencies = new HashMap<>() ;
        greaterLevelOccupants = new HashMap<>() ;
        nonFloodAdjacencies = new HashMap<>() ;
    }

    public void compileSettlementData(){
        level2Adjacencies.clear();
        level2Occupants.clear();
        level3Adjacencies.clear();
        greaterLevelAdjacencies.clear();
        nonFloodAdjacencies.clear();

        gatherLevelDwellers();

        settle.countSettlementMembers();
        size = settle.size ;
        SizeCostPair helper ;

        helper = gatherExpansionCosts(settle.grasslands) ;
        grassCost = helper.cost ;
        afterGrass = helper.size;

        helper = gatherExpansionCosts(settle.jungles) ;
        jungleCost = helper.cost ;
        afterJungle = helper.size;

        helper = gatherExpansionCosts(settle.lakes) ;
        lakeCost = helper.cost ;
        afterLake = helper.size;

        helper = gatherExpansionCosts(settle.rocky) ;
        rockyCost = helper.cost ;
        afterRocky = helper.size;

        helper = gatherExpansionCosts(settle.paddys);
        paddyCost = helper.cost;
        afterPaddy = helper.size;
    }

    private SizeCostPair gatherExpansionCosts(HashMap<Integer,Point> adjacents){
        SizeCostPair value = new SizeCostPair(0, 0);
        SizeCostPair returned ;

        for(Point point : adjacents.values()){
            nonFloodAdjacencies.put(game.board[point.row][point.column].key, point);
            returned = recursiveGather(point) ;
            value.size += returned.size;
            value.cost += returned.cost ;
        }

        countedTerrain.clear();
        return value ;
    }

    private SizeCostPair recursiveGather(Point target) {
        SizeCostPair value = new SizeCostPair(1, game.board[target.row][target.column].level) ;

        countedTerrain.put(game.board[target.row][target.column].key, 1) ;

        processAdjacentLevel(target);

        SizeCostPair returned ;

        for (HexPointPair adjacent : game.board[target.row][target.column].links.values()){
            if (!countedTerrain.containsKey(adjacent.hex.key)) {
                returned = recursiveGather(adjacent.point);
                value.size += returned.size;
                value.cost += returned.cost ;
            }
        }

        return value ;
    }

    private void processAdjacentLevel(Point loc){
        int level = game.board[loc.row][loc.column].level ;

        if(level == 2){
            level2Adjacencies.put(game.board[loc.row][loc.column].key, loc) ;
        }
        else if(level == 3){
            level3Adjacencies.put(game.board[loc.row][loc.column].key, loc) ;
        }
        else if(level > 3){
            greaterLevelAdjacencies.put(game.board[loc.row][loc.column].key, loc) ;
        }
    }

    private void gatherLevelDwellers(){
        for(Point pt : settle.occupantPositions.values()){
            if(game.board[pt.row][pt.column].level == 2){
                level2Occupants.put(game.board[pt.row][pt.column].key, pt) ;
            }
            else if(game.board[pt.row][pt.column].level > 2){
                greaterLevelOccupants.put(game.board[pt.row][pt.column].key, pt) ;
            }
        }
    }

}
