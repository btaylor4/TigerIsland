package main.players;

import main.GameBoard;
import main.Player;
import main.Point;
import main.enums.BuildOptions;
import main.players.AIUtils.Objective;
import main.players.AIUtils.SettlementData;
import main.players.AIUtils.TileOptions;
import main.utils.SettlePointPair;

import java.util.ArrayList;


public class JPAI extends Player {

    public int moveNumber ;

    private Objective currentGoal ;
    private TileOptions tilePlayStrat ;
    private BuildOptions buildPlayStrat ;

    private Player other;
    private int opponentMeeples, opponentTotoro, opponentTigers, opponentScore ;

    private ArrayList<SettlePointPair> totoroPossibilities ;
    private ArrayList<SettlePointPair> tigerPossibilities ;

    private ArrayList<SettlementData> opponentSet ;
    private ArrayList<SettlementData> mySettlementData ;

    private ArrayList<Point> totoroPreventionHits;
    private ArrayList<Point> tigerPreventionHits;

    public JPAI(GameBoard gamePointer, int designator, Player opponent){
        super(gamePointer, designator);
        this.other = opponent ;
        moveNumber = 0 ;
        tilePlayStrat = TileOptions.FLATBUILD ;
        buildPlayStrat = BuildOptions.FOUND_SETTLEMENT ;
        currentGoal = Objective.EXPAND ;

        totoroPossibilities = new ArrayList<>();
        tigerPossibilities = new ArrayList<>();

        opponentSet = new ArrayList<>() ;
        mySettlementData = new ArrayList<>() ;

        totoroPreventionHits = new ArrayList<>() ;
        tigerPreventionHits = new ArrayList<>() ;
    }

    private void prepareDecisionData(){
        getOpponentPieces();
        getOpponentSettlements();
        getMySettlements();

        for(SettlementData data : mySettlementData)
            data.compileSettlementData();

        for(SettlementData data : opponentSet)
            data.compileSettlementData();

        gatherAvailableTotoroPlacement();
        gatherAvailableTigerPlacement();

        makeTotoroHitlist();
        makeTigerHitlist();
    }

    private void getOpponentPieces(){
        opponentMeeples = other.meeples ;
        opponentTotoro = other.totoro ;
        opponentTigers = other.tigers ;
        opponentScore = other.score ;
    }

    private void getOpponentSettlements(){
        for(SettlePointPair pair: other.playerSettlements.values()){
            other.uniqueSettlements.put(pair.settlement, pair.point);
        }

        for(Point point : other.uniqueSettlements.values()){
            opponentSet.add(new SettlementData(game.board[point.row][point.column].settlementPointer, game));
        }

        other.uniqueSettlements.clear();
    }

    private void getMySettlements(){
        for(SettlePointPair pair: playerSettlements.values()){
            uniqueSettlements.put(pair.settlement, pair.point);
        }

        for(Point point : uniqueSettlements.values()){
            mySettlementData.add(new SettlementData(game.board[point.row][point.column].settlementPointer, game));
        }

        uniqueSettlements.clear();
    }

    private void gatherAvailableTotoroPlacement(){
        for(SettlementData sd : mySettlementData){
            if(sd.size > 4 && sd.settle.totoroSanctuaries == 0){
                for(Point pt : sd.nonFloodAdjacencies.values()) {
                    totoroPossibilities.add(new SettlePointPair(sd.settle, pt));
                }
            }
        }
    }

    private void gatherAvailableTigerPlacement(){
        for(SettlementData sd : mySettlementData){
            if(sd.settle.tigerPlaygrounds == 0) {
                for (Point pt : sd.level3Adjacencies.values()) {
                    tigerPossibilities.add(new SettlePointPair(sd.settle, pt));
                }
            }
        }
    }

    private void makeTotoroHitlist(){
        for(SettlementData sd : opponentSet){
            if(sd.size > 4 && sd.settle.totoroSanctuaries == 0){
                for(Point pt : sd.nonFloodAdjacencies.values()){
                    totoroPreventionHits.add(pt);
                }
            }
        }
    }

    private void makeTigerHitlist(){
        for(SettlementData sd : opponentSet){
            if(sd.settle.tigerPlaygrounds == 0) {
                for (Point pt : sd.level3Adjacencies.values()) {
                    tigerPreventionHits.add(pt);
                }
            }
        }
    }

    private void clearDataLists(){
        opponentSet.clear();
        mySettlementData.clear();

        totoroPossibilities.clear();
        tigerPossibilities.clear();

        totoroPreventionHits.clear();
        tigerPreventionHits.clear();
    }

    @Override
    protected void determineTilePlacement(){
        // Ai determined by algorithm
        prepareDecisionData();
        decideTileOption();
        determineTilePosition();
    }

    private void decideBuildOption(){
        if(mySettlementData.isEmpty())
            currentGoal = Objective.MAKE_SETTLEMENT;
        else if(!totoroPossibilities.isEmpty() && totoro > 0) {
            currentGoal = Objective.TOTORO_PLACEMENT ;
        }
        else if(!tigerPossibilities.isEmpty() && tigers > 0){
            currentGoal = Objective.TIGER_PLACEMENT ;
        }
        else if(mySettlementData.size() > 0){
            currentGoal = Objective.EXPAND ;
        }
        else{
            currentGoal = Objective.MAKE_SETTLEMENT;
        }
    }

    private void decideTileOption(){
        if(!tigerPreventionHits.isEmpty() || !totoroPreventionHits.isEmpty()){
            tilePlayStrat = TileOptions.OFFENSIVE ;
        }
        else{
            tilePlayStrat = TileOptions.FLATBUILD ;
        }
    }

    private void determineTilePosition(){
        switch(tilePlayStrat){
            case OFFENSIVE:
                //offensiveTilePlacement();
                break;

            case FLATBUILD:
                //flatTilePlacement();
                break;

            default:
                //flatTilePlacement();
                break;
        }
    }

    /*
    private void offensiveTilePlacement(){
        int row, column, rotation ;

        tilePlacement = determineTilePosition();
        tileProjection = projectTilePlacement(tileHeld, tilePlacement);
        tileProjection.projectedLevel = game.getProjectedHexLevel(tileProjection) ;
        game.isValidTilePlacement(tileProjection);
    }

    private void flatTilePlacement(){
        int row, column, rotation ;

        tilePlacement = determineTilePosition();
        tileProjection = projectTilePlacement(tileHeld, tilePlacement);
        tileProjection.projectedLevel = game.getProjectedHexLevel(tileProjection) ;
        game.isValidTilePlacement(tileProjection);
    }

    @Override
    protected void determineBuildOption(){
        // Ai determined by algorithm
        selectBuildOption();

        clearDataLists();
    }

    private void selectBuildOption(){

        switch(buildOption) {
            case 1:
                buildDecision = BuildOptions.FOUND_SETTLEMENT ;
                determineSettlementPosition();
                System.out.println("Successfully Founded");
                break;

            case 2:
                buildDecision = BuildOptions.EXPAND ;
                determineSettlementExpansion();
                break;

            case 3:
                buildDecision = BuildOptions.TOTORO_SANCTUARY ;
                determineTotoroPlacement();
                break;

            case 4:
                buildDecision = BuildOptions.TIGER_PLAYGROUND ;
                determineTigerPlaygroundPlacement();
                break;

            default:
                System.out.println("Invalid choice");
        }
    }

    private void determineSettlementExpansion(){
        buildPoint = chooseSettlementToExpand();
        terrainSelection = chooseTerrainToExpandOn();
    }

    private void determineTotoroPlacement(){
        Point finder ;
        finder = chooseSettlementForTotoro();
        selectedSettlement = game.board[finder.row][finder.column].settlementPointer ;
        buildPoint = chooseTotoroPosition() ;

        !game.isValidTotoroPosition(buildPoint, selectedSettlement));
    }

    private void determineTigerPlaygroundPlacement(){
        Point finder ;

        finder = chooseSettlementForTiger();
        selectedSettlement = game.board[finder.row][finder.column].settlementPointer ;
        buildPoint = chooseTigerPosition() ;
        game.isValidTigerPosition(buildPoint, selectedSettlement);
    }

*/
}
