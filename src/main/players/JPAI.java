package main.players;

import main.*;
import main.enums.BuildOptions;
import main.enums.OccupantType;
import main.enums.TerrainType;

import main.players.AIUtils.SettlementData;
import main.players.AIUtils.TileOptions;
import main.utils.SettlePointPair;

import java.util.ArrayList;

import static main.utils.constants.*;


public class JPAI extends Player {

    public int moveNumber ;
    private TileOptions tilePlayStrat ;
    private BuildOptions buildPlayStrat ;

    private int extensionPossibleTilePlacement ;
    private ProjectionPack projectionPossibleTilePlacement ;
    private int projectedLoss ;

    private ArrayList<SettlementData> mySettlementData ;
    private ArrayList<SettlePointPair> totoroPossibilities ;
    private ArrayList<SettlePointPair> tigerPossibilities ;
    private ArrayList<Settlement> settlementsWithTotoro;

    private Player other;
    //private int opponentMeeples, opponentTotoro, opponentTigers, opponentScore ;

    private ArrayList<Settlement> nukeTargets ;
    private ArrayList<Point> totoroPreventionSpots;
    private ArrayList<Point> tigerPreventionSpots;
    private ArrayList<SettlementData> opponentSet ;


    public JPAI(GameBoard gamePointer, int designator){
        super(gamePointer, designator);
        this.other = null ;

        moveNumber = 0 ;
        tilePlayStrat = TileOptions.FLATBUILD ;
        buildPlayStrat = BuildOptions.FOUND_SETTLEMENT ;

        totoroPossibilities = new ArrayList<>();
        tigerPossibilities = new ArrayList<>();
        settlementsWithTotoro = new ArrayList<>();

        opponentSet = new ArrayList<>() ;
        mySettlementData = new ArrayList<>() ;

        nukeTargets = new ArrayList<>() ;
        totoroPreventionSpots = new ArrayList<>() ;
        tigerPreventionSpots = new ArrayList<>() ;
    }

    public void setOpponent(Player opponent){
        this.other = opponent ;
    }

    private void prepareDecisionData(){
        extensionPossibleTilePlacement = BOARD_CENTER-2 ;
        projectionPossibleTilePlacement = null ;
        projectedLoss = 0 ;

        getOpponentSettlements();
        getMySettlements();

        for(SettlementData data : mySettlementData)
            data.compileSettlementData();

        for(SettlementData data : opponentSet)
            data.compileSettlementData();

        gatherAvailableTotoroPlacement();
        gatherAvailableTigerPlacement();
        gatherTotoroToDetach();

        makeTotoroHitlist();
        makeTigerHitlist();
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

    private void gatherTotoroToDetach(){
        for(SettlementData sd : mySettlementData){
            if(sd.settle.totoroSanctuaries > 0 && sd.size > 4){
                settlementsWithTotoro.add(sd.settle) ;
            }
        }
    }

    private void makeTotoroHitlist(){
        for(SettlementData sd : opponentSet){
            if(sd.size > 4 && sd.settle.totoroSanctuaries == 0){
                nukeTargets.add(sd.settle) ;
                for(Point pt : sd.nonFloodAdjacencies.values()){
                    totoroPreventionSpots.add(pt);
                }
            }
        }
    }

    private void makeTigerHitlist(){
        for(SettlementData sd : opponentSet){
            if(sd.settle.tigerPlaygrounds == 0) {
                for (Point pt : sd.level3Adjacencies.values()) {
                    tigerPreventionSpots.add(pt);
                }
            }
        }
    }

    private void clearDataLists(){
        opponentSet.clear();
        mySettlementData.clear();

        totoroPossibilities.clear();
        tigerPossibilities.clear();
        settlementsWithTotoro.clear();

        nukeTargets.clear();
        totoroPreventionSpots.clear();
        tigerPreventionSpots.clear();

        uniqueSettlements.clear();
    }

    @Override
    protected void determineTilePlacement(){
        prepareDecisionData();
        decideTileOption();
        determineTilePosition();
        clearDataLists();
    }

    private void decideTileOption(){
        if(!nukeTargets.isEmpty()){
            tilePlayStrat = TileOptions.OFFENSIVE ;
        }
        else if(!settlementsWithTotoro.isEmpty()){
            tilePlayStrat = TileOptions.SELFNUKE ;
        }
        else{
            tilePlayStrat = TileOptions.FLATBUILD ;
        }
    }

    private void determineTilePosition(){
        switch(tilePlayStrat){
            case OFFENSIVE:
                offensiveTilePlacement();
                break;

            case SELFNUKE:
                selfNukeTilePlacement();
                break;

            case FLATBUILD:
                flatTilePlacement();
                break;

            default:
                flatTilePlacement();
                break;
        }
    }

    private void flatTilePlacement(){
        if(mySettlementData.isEmpty())
            setFlatTilePlacement(new Point(BOARD_CENTER, BOARD_CENTER));
        else {
            for (SettlementData data : mySettlementData) {
                for (Point pt : data.settle.occupantPositions.values())
                    setFlatTilePlacement(pt);
            }
        }
    }

    private void setFlatTilePlacement(Point origin){
        // origin is where a settlement member is then we go around it
        Point seeker = new Point(0, 0);
        boolean found = false ;

        for(int displacement = 2; (displacement < BOARD_CENTER-2) && !found; displacement++){
            if(displacement > extensionPossibleTilePlacement) break ;

            seeker.row = origin.row - displacement;
            seeker.column = origin.column ;

            if(checkAllRotations_Flat(seeker)){
                found = true ;
                if(displacement < extensionPossibleTilePlacement){
                    tileProjection = projectionPossibleTilePlacement;
                    tilePlacement = seeker;
                }
            }

            for(int side = 0 ; (side < SIDES_IN_HEX) && !found; side++) {
                for (int moves = 0; (moves < displacement) && !found; moves++) {
                    seeker.row += TRAVERSE_ROW_ADDS[side] ;
                    seeker.column += TRAVERSE_COLUMN_ADDS[side] ;

                    if(checkAllRotations_Flat(seeker)){
                        found = true ;
                        if(displacement < extensionPossibleTilePlacement){
                            tileProjection = projectionPossibleTilePlacement;
                            tilePlacement = seeker;
                        }
                    }
                }
            }
        }
    }

    private boolean checkAllRotations_Flat(Point point){
        for(int i = 0 ; i < SIDES_IN_HEX; i++){
            tileHeld.setRotation(i + 1) ;
            projectionPossibleTilePlacement = projectTilePlacement(tileHeld, point) ;
            projectionPossibleTilePlacement.projectedLevel = game.getProjectedHexLevel(projectionPossibleTilePlacement);
            if(game.isValidTilePlacement(projectionPossibleTilePlacement) && projectionPossibleTilePlacement.projectedLevel == 1)
                return true;
        }
        return false ;
    }

    private void offensiveTilePlacement(){
        // try to prevent tigers before preventing totoro
        totoroPreventionNuke();

        if(projectedLoss == 0) { // cant nuke
            flatTilePlacement();
        }
    }

    private void totoroPreventionNuke(){
        int victims = 0 ;

        for(Settlement tgt : nukeTargets){
            for(Point volcano : tgt.volcanoes.values()){
                // try to nuke two from this volcano
                if(victims != 2) victims = checkAllRotations_Nuke(volcano, tgt) ;
            }
        }
    }

    private int checkAllRotations_Nuke(Point point, Settlement settle){
        int victims = 0 ;

        for(int i = 0 ; i < SIDES_IN_HEX; i++){
            victims = 0;
            tileHeld.setRotation(i + 1) ;
            projectionPossibleTilePlacement = projectTilePlacement(tileHeld, point) ;
            projectionPossibleTilePlacement.projectedLevel = game.getProjectedHexLevel(projectionPossibleTilePlacement);

            if((projectionPossibleTilePlacement.projectedLevel > 1) && game.isValidOverlap(projectionPossibleTilePlacement)){
                if(game.board[projectionPossibleTilePlacement.hex_a.row][projectionPossibleTilePlacement.hex_a.column].settlementPointer == settle)
                    victims++ ;

                if(game.board[projectionPossibleTilePlacement.hex_b.row][projectionPossibleTilePlacement.hex_b.column].settlementPointer == settle)
                    victims++ ;

                if(victims > projectedLoss){
                    projectedLoss = victims ;
                    tileProjection = projectionPossibleTilePlacement ;
                    tilePlacement = point ;
                }

                if(victims == 2) return victims;
            }
        }

        return victims ;
    }

    private void selfNukeTilePlacement(){
        Point totoroPosition ;
        boolean nukeSucceeded = false ;

        for(Settlement set : settlementsWithTotoro) {
            totoroPosition = findTheTotoro(set);
            if (totoroPosition.row != 0 && totoroPosition.column != 0) {
                nukeSucceeded = nukeAroundTotoro(totoroPosition);
            }
        }

        if(!nukeSucceeded) flatTilePlacement();
    }

    private Point findTheTotoro(Settlement settlement){
        for(Point pt : settlement.occupantPositions.values()){
            if(game.board[pt.row][pt.column].occupant == OccupantType.TOTORO)
                return pt ;
        }
        return new Point(0,0);
    }

    private boolean nukeAroundTotoro(Point pt){
        Point seeker = new Point(pt.row-1, pt.column);
        if(attemptValidNuke(seeker)) return true ;

        for(int i = 0 ; i < SIDES_IN_HEX; i++){
            seeker.row += TRAVERSE_ROW_ADDS[i] ;
            seeker.column += TRAVERSE_COLUMN_ADDS[i] ;

            if(attemptValidNuke(seeker)) return true ;
        }

        return false ;
    }

    private boolean attemptValidNuke(Point pt){
        for(int j = 0; j < SIDES_IN_HEX; j++){
            tileHeld.setRotation(j + 1);
            projectionPossibleTilePlacement = projectTilePlacement(tileHeld, pt);
            projectionPossibleTilePlacement.projectedLevel = game.getProjectedHexLevel(projectionPossibleTilePlacement);

            if((projectionPossibleTilePlacement.projectedLevel > 1) && game.isValidOverlap(projectionPossibleTilePlacement)){
                tileProjection = projectionPossibleTilePlacement;
                tilePlacement = pt;
                return true;
            }
        }

        return false ;
    }


    @Override
    protected void determineBuildOption(){
        prepareDecisionData();
        decideBuildOption();
        selectBuildOption();
        clearDataLists();
    }

    private void decideBuildOption(){
        if(mySettlementData.isEmpty())
            buildPlayStrat = BuildOptions.FOUND_SETTLEMENT ;
        else if(!totoroPossibilities.isEmpty() && totoro > 0) {
            buildPlayStrat = BuildOptions.TOTORO_SANCTUARY ;
        }
        else if(!tigerPossibilities.isEmpty() && tigers > 0){
            buildPlayStrat = BuildOptions.TIGER_PLAYGROUND ;
        }
        else if(mySettlementData.size() > 0){
            buildPlayStrat = BuildOptions.EXPAND ;
        }
        else{
            buildPlayStrat = BuildOptions.FOUND_SETTLEMENT;
        }
    }

    private void selectBuildOption(){
        switch(buildPlayStrat) {
            case FOUND_SETTLEMENT:
                buildDecision = BuildOptions.FOUND_SETTLEMENT ;
                buildPoint = determineSettlementPosition();
                break;

            case EXPAND:
                buildDecision = BuildOptions.EXPAND ;
                determineSettlementExpansion();

                break;

            case TOTORO_SANCTUARY:
                buildDecision = BuildOptions.TOTORO_SANCTUARY ;
                determineTotoroPlacement();
                break;

            case TIGER_PLAYGROUND:
                buildDecision = BuildOptions.TIGER_PLAYGROUND ;
                determineTigerPlaygroundPlacement();
                break;

            default:
                System.out.println("Invalid choice");
        }
    }

    private Point determineSettlementPosition(){
        Point seeker = new Point(0, 0);

        for(int displacement = 1; (displacement < BOARD_CENTER-2); displacement++){
            seeker.row = BOARD_CENTER - displacement;
            seeker.column = BOARD_CENTER ;

            for(int side = 0 ; (side < SIDES_IN_HEX) ; side++) {
                for (int moves = 0; (moves < displacement) ; moves++) {
                    seeker.row += TRAVERSE_ROW_ADDS[side] ;
                    seeker.column += TRAVERSE_COLUMN_ADDS[side] ;

                    if(game.isValidSettlementPosition(seeker)) {
                        return seeker;
                    }
                }
            }
        }

        System.out.println("Settlement position not found!");
        return seeker ;
    }

    private void determineSettlementExpansion(){
        buildPoint = findSettlementToExpand();
        if(buildPoint == null){
            buildDecision = BuildOptions.FOUND_SETTLEMENT ;
            buildPoint = determineSettlementPosition();
        }
    }

    private Point findSettlementToExpand(){
        int sizeAfter = 0, meepleCost = 20;
        boolean goForTotoro = false ;
        Point selectedPoint = null ;

        for(SettlementData sd : mySettlementData){
            if((sd.afterGrass > 4) && (sd.grassCost < meepleCost) && (sd.grassCost < meeples)){
                meepleCost = sd.grassCost ;
                terrainSelection = TerrainType.GRASS ;
                selectedPoint = uniqueSettlements.get(sd.settle) ;
                goForTotoro = true ;
            }

            if((sd.afterLake > 4) && (sd.lakeCost < meepleCost) && (sd.lakeCost < meeples)){
                meepleCost = sd.lakeCost ;
                terrainSelection = TerrainType.LAKE ;
                selectedPoint = uniqueSettlements.get(sd.settle) ;
                goForTotoro = true ;
            }

            if((sd.afterRocky > 4) && (sd.rockyCost < meepleCost) && (sd.rockyCost < meeples)){
                meepleCost = sd.rockyCost ;
                terrainSelection = TerrainType.ROCK ;
                selectedPoint = uniqueSettlements.get(sd.settle) ;
                goForTotoro = true ;
            }

            if((sd.afterJungle > 4) && (sd.jungleCost < meepleCost) && (sd.jungleCost < meeples)){
                meepleCost = sd.jungleCost ;
                terrainSelection = TerrainType.JUNGLE ;
                selectedPoint = uniqueSettlements.get(sd.settle) ;
                goForTotoro = true ;
            }
        }

        if(goForTotoro) return selectedPoint ;

        for(SettlementData sd : mySettlementData){
            if((sd.afterGrass >= sizeAfter) && (sd.grassCost < meepleCost) && (sd.grassCost < meeples)){
                meepleCost = sd.grassCost ;
                sizeAfter = sd.afterGrass ;
                terrainSelection = TerrainType.GRASS ;
                selectedPoint = uniqueSettlements.get(sd.settle) ;
            }

            if((sd.afterLake >= sizeAfter) && (sd.lakeCost < meepleCost) && (sd.lakeCost < meeples)){
                meepleCost = sd.lakeCost ;
                sizeAfter = sd.afterLake ;
                terrainSelection = TerrainType.LAKE ;
                selectedPoint = uniqueSettlements.get(sd.settle) ;
            }

            if((sd.afterRocky >= sizeAfter) && (sd.rockyCost < meepleCost) && (sd.rockyCost < meeples)){
                meepleCost = sd.rockyCost ;
                sizeAfter = sd.afterRocky ;
                terrainSelection = TerrainType.ROCK ;
                selectedPoint = uniqueSettlements.get(sd.settle) ;
            }

            if((sd.afterJungle >= sizeAfter) && (sd.jungleCost < meepleCost) && (sd.jungleCost < meeples)){
                meepleCost = sd.jungleCost ;
                sizeAfter = sd.afterJungle ;
                terrainSelection = TerrainType.JUNGLE ;
                selectedPoint = uniqueSettlements.get(sd.settle) ;
            }
        }

        if(sizeAfter == 0) return null ;

        return selectedPoint ;
    }

    private void determineTotoroPlacement(){
        boolean spotFound = false ;

        for(SettlePointPair sp : totoroPossibilities){
            if(game.isValidTotoroPosition(sp.point, sp.settlement)){
                buildPoint = sp.point ;
                selectedSettlement = sp.settlement ;
                spotFound = true ;
                break;
            }
        }

        if(!spotFound){
            buildDecision = BuildOptions.FOUND_SETTLEMENT ;
            buildPoint = determineSettlementPosition();
        }
    }

    private void determineTigerPlaygroundPlacement(){
        boolean spotFound = false ;

        for(SettlePointPair sp : tigerPossibilities){
            if(game.isValidTigerPosition(sp.point, sp.settlement)){
                buildPoint = sp.point ;
                selectedSettlement = sp.settlement ;
                spotFound = true ;
                break;
            }
        }

        if(!spotFound){
            buildDecision = BuildOptions.FOUND_SETTLEMENT ;
            buildPoint = determineSettlementPosition();
        }
    }
}
