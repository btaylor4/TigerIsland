package main;

import main.enums.OccupantType;
import main.enums.TerrainType;

import java.util.HashMap;
import java.util.Random;

import static main.utils.formulas.coordinatesToKey;
import static main.utils.constants.* ;

public class GameBoard {
    public static final int BOARD_CENTER = ARRAY_DIMENSION/2 ;

    private int upperLimit, lowerLimit, leftLimit, rightLimit ;

    public static Hexagon[][] board;
    private HashMap<Integer, Point> playableHexes;
    public static Tile tileStack[] = new Tile[NUM_TILES];

    private int tilePlayIndex;

    public GameBoard(){
        board = new Hexagon[ARRAY_DIMENSION][ARRAY_DIMENSION];
        playableHexes = new HashMap<>() ;
        upperLimit = lowerLimit = leftLimit = rightLimit = BOARD_CENTER ;
        createTiles();
        shuffleTiles();
        tilePlayIndex = 0 ;
    }

    public void createTiles(){
        // the tiles, are three copies of a full permutation of the terrains
        int tileCounter = 0;

        for (TerrainType a : TerrainType.values() ) {
            if(a == TerrainType.VOLCANO) continue ;

            for (TerrainType b : TerrainType.values() ) {
                if(b == TerrainType.VOLCANO) continue ;

                for (int i = 0; i < NUM_TILE_COPIES; i++) {
                    tileStack[tileCounter] = new Tile();
                    tileStack[tileCounter].assignTileNumber(tileCounter + 1) ;
                    tileStack[tileCounter].assignTerrain(a, b) ;
                    tileCounter++;
                }
            }
        }
    }

    public void shuffleTiles(){
        Random r = new Random();
        int random_index ;
        Tile tileBeingSwapped ;

        for(int i = 0; i < NUM_TILES; i++){
            tileBeingSwapped = tileStack[i] ;
            random_index = r.nextInt(NUM_TILES) ;
            tileStack[i] = tileStack[random_index] ;
            tileStack[random_index] = tileBeingSwapped ;
        }
    }

    public boolean checkAdjacency(ProjectionPack projections){
        boolean volcanoAdjacent, hex_aAdjacent, hex_bAdjacent ;

        // if one of the hexes fall in the freePlay list and nothing is underneath the tiles return true
        volcanoAdjacent = playableHexes.containsKey(coordinatesToKey(projections.volcano.row, projections.volcano.column));
        hex_aAdjacent = playableHexes.containsKey(coordinatesToKey(projections.hex_a.row, projections.hex_a.column));
        hex_bAdjacent = playableHexes.containsKey(coordinatesToKey(projections.hex_b.row, projections.hex_b.column));

        return (volcanoAdjacent || hex_aAdjacent || hex_bAdjacent) ;
    }

    public int getProjectedHexLevel(ProjectionPack projections){
        int volcanoLevel, hex_aLevel, hex_bLevel ;

        if(board[projections.volcano.row][projections.volcano.column] == null)
            volcanoLevel = 1 ;
        else{
            volcanoLevel = board[projections.volcano.row][projections.volcano.column].level + 1;
        }

        if(board[projections.hex_a.row][projections.hex_a.column] == null)
            hex_aLevel = 1 ;
        else{
            hex_aLevel = board[projections.hex_a.row][projections.hex_a.column].level + 1 ;
        }

        if(board[projections.hex_b.row][projections.hex_b.column] == null)
            hex_bLevel = 1 ;
        else {
            hex_bLevel = board[projections.hex_b.row][projections.hex_b.column].level + 1;
        }

        if((hex_aLevel == hex_bLevel) && (hex_aLevel == volcanoLevel))
            return hex_aLevel ;
        else
            return 0;
    }

    public void addFreeAdjacencies(Point point){
        int row , column ;

        for (int i = 0; i < SIDES_IN_HEX; i++) {
            row = point.row + ROW_ADDS[i];
            column = point.column + COLUMN_ADDS[i];

            if (board[row][column] == null) {
                playableHexes.put(coordinatesToKey(row, column), new Point(row, column));
            }
            else if(board[row][column].settlementPointer != null){
                board[row][column].settlementPointer.hashAdjacentTerrain(board[point.row][point.column].terrain, point);
            }
        }
    }

    public void removeFreeAdjacency(Point point){
        playableHexes.remove(coordinatesToKey(point.row, point.column));
    }

    public boolean isValidOverlap(ProjectionPack projection){
        boolean volcanoAligned, totoroPresent, tigerPresent ;
        int tileUnderVolcano, tileUnderHexA, tileUnderHexB ;


        volcanoAligned = board[projection.volcano.row][projection.volcano.column].terrain == TerrainType.VOLCANO ;
        if(!volcanoAligned) return false ;

        totoroPresent = board[projection.hex_a.row][projection.hex_a.column].occupant == OccupantType.TOTORO ;
        if(totoroPresent) return false ;

        totoroPresent = board[projection.hex_b.row][projection.hex_b.column].occupant == OccupantType.TOTORO ;
        if(totoroPresent) return false ;

        tigerPresent = board[projection.hex_a.row][projection.hex_a.column].occupant == OccupantType.TIGERPLAYGROUND ;
        if(tigerPresent ) return false ;

        tigerPresent  = board[projection.hex_b.row][projection.hex_b.column].occupant == OccupantType.TIGERPLAYGROUND ;
        if(tigerPresent ) return false ;

        tileUnderVolcano = board[projection.volcano.row][projection.volcano.column].tileNumber ;
        tileUnderHexA = board[projection.hex_a.row][projection.hex_a.column].tileNumber ;
        tileUnderHexB = board[projection.hex_b.row][projection.hex_b.column].tileNumber ;

        if((tileUnderHexA == tileUnderHexB) && (tileUnderVolcano == tileUnderHexA)){
            return false ;
        }

        if(board[projection.hex_a.row][projection.hex_a.column].occupant != OccupantType.NONE){
            if(board[projection.hex_a.row][projection.hex_a.column].settlementPointer.size == 1)
                return false ;
        }
        else if(board[projection.hex_b.row][projection.hex_b.column].occupant != OccupantType.NONE){
            if(board[projection.hex_b.row][projection.hex_b.column].settlementPointer.size == 1)
                return false ;
        }

        return true ;
    }

    public boolean isValidTilePlacement(ProjectionPack projection){
        System.out.println("level is :" + projection.projectedLevel);
        if (checkAdjacency(projection) && (projection.projectedLevel == 1)) {
            System.out.println("Tile placement OK") ;
            return true ;
        }
        else if((projection.projectedLevel > 1) && (isValidOverlap(projection))){
            System.out.println("Volcanic eruption OK") ;
            return true ;
        }
        else{
            System.out.println("Invalid placement requested") ;
            return false ;
        }
    }

    public boolean isValidSettlementPosition(Point desiredPosition){
        if(board[desiredPosition.row][desiredPosition.column] == null)
            return false;

        if(board[desiredPosition.row][desiredPosition.column].terrain == TerrainType.VOLCANO)
            return false;

        if(board[desiredPosition.row][desiredPosition.column].occupant != OccupantType.NONE)
            return false;

        if(board[desiredPosition.row][desiredPosition.column].level != 1)
            return false;

        return true;
    }

    public boolean isValidTotoroPosition(Point desiredPosition, Settlement settlement){
        if(board[desiredPosition.row][desiredPosition.column] == null)
            return false;

        if(board[desiredPosition.row][desiredPosition.column].terrain == TerrainType.VOLCANO)
            return false;

        if(board[desiredPosition.row][desiredPosition.column].occupant != OccupantType.NONE)
            return false;

        if(settlement == null) return false ;

        if(settlement.size < 5) return false ;

        if(settlement.totoroSanctuaries != 0) return false ;

        int row, column ;
        boolean adjacent = false ;

        for (int i = 0; i < SIDES_IN_HEX; i++) {
            row = desiredPosition.row + ROW_ADDS[i];
            column = desiredPosition.column + COLUMN_ADDS[i];

            if (board[row][column] != null) {
                if(board[row][column].settlementPointer != null) {
                    if (board[row][column].settlementPointer == settlement) {
                        adjacent = true;
                        break;
                    }
                }
            }
        }

        if(!adjacent) return false ;

        return true;
    }

    public boolean isValidTigerPosition(Point desiredPosition, Settlement settlement){
        if(board[desiredPosition.row][desiredPosition.column] == null)
            return false;

        if(board[desiredPosition.row][desiredPosition.column].terrain == TerrainType.VOLCANO)
            return false;

        if(board[desiredPosition.row][desiredPosition.column].occupant != OccupantType.NONE)
            return false;

        if(board[desiredPosition.row][desiredPosition.column].level < 3)
            return false ;

        if(settlement == null) return false ;

        if(settlement.tigerPlaygrounds != 0) return false ;

        int row, column ;
        boolean adjacent = false ;

        for (int i = 0; i < SIDES_IN_HEX; i++) {
            row = desiredPosition.row + ROW_ADDS[i];
            column = desiredPosition.column + COLUMN_ADDS[i];

            if (board[row][column] != null) {
                if(board[row][column].settlementPointer != null) {
                    if (board[row][column].settlementPointer == settlement) {
                        adjacent = true;
                        break;
                    }
                }
            }
        }

        if(!adjacent) return false ;

        return true;
    }

    public Tile releaseTopTile(){
        return tileStack[tilePlayIndex] ;
    }

    public void setFirstTile(){
        StartingTile starter = new StartingTile() ;
        starter.assignTerrain();
        starter.setHexLevels(1);
        starter.setHexTileNumbers(tilePlayIndex);
        tilePlayIndex++;

        starter.projects.volcano = new Point(BOARD_CENTER, BOARD_CENTER) ;
        starter.projects.upLeft = new Point(BOARD_CENTER-1, BOARD_CENTER );
        starter.projects.upRight = new Point(BOARD_CENTER-1, BOARD_CENTER+1);
        starter.projects.downLeft = new Point(BOARD_CENTER+1, BOARD_CENTER-1);
        starter.projects.downRight = new Point(BOARD_CENTER+1, BOARD_CENTER);

        board[starter.projects.volcano.row][starter.projects.volcano.column] = starter.volcano ;
        board[starter.projects.upLeft.row][starter.projects.upLeft.column] = starter.hexUpLeft ;
        board[starter.projects.upRight.row][starter.projects.upRight.column] = starter.hexUpRight ;
        board[starter.projects.downLeft.row][starter.projects.downLeft.column] = starter.hexDownLeft ;
        board[starter.projects.downRight.row][starter.projects.downRight.column] = starter.hexDownRight ;

        addFreeAdjacencies(starter.projects.volcano);
        addFreeAdjacencies(starter.projects.upLeft);
        addFreeAdjacencies(starter.projects.upRight);
        addFreeAdjacencies(starter.projects.downLeft);
        addFreeAdjacencies(starter.projects.downRight);

        removeFreeAdjacency(starter.projects.volcano);
        removeFreeAdjacency(starter.projects.upLeft);
        removeFreeAdjacency(starter.projects.upRight);
        removeFreeAdjacency(starter.projects.downLeft);
        removeFreeAdjacency(starter.projects.downRight);

        leftLimit -= 2 ;
        rightLimit += 2 ;
        upperLimit -= 2 ;
        lowerLimit += 2 ;
    }

    public void setTile(Tile tileBeingPlaced, ProjectionPack projections){
        tileBeingPlaced.setHexLevels(projections.projectedLevel) ;
        tileBeingPlaced.assignTileNumber(tilePlayIndex);
        tilePlayIndex++ ;

        if(projections.projectedLevel > 1) processVolcanicDestruction(projections);
        // make sure destruction happens before placement

        board[projections.volcano.row][projections.volcano.column] = tileBeingPlaced.volcano ;
        board[projections.hex_a.row][projections.hex_a.column] = tileBeingPlaced.hexA ;
        board[projections.hex_b.row][projections.hex_b.column] = tileBeingPlaced.hexB ;

        addFreeAdjacencies(projections.volcano);
        addFreeAdjacencies(projections.hex_a);
        addFreeAdjacencies(projections.hex_b);

        removeFreeAdjacency(projections.volcano);
        removeFreeAdjacency(projections.hex_a);
        removeFreeAdjacency(projections.hex_b);

        if(leftLimit > projections.volcano.column) leftLimit -= 2 ;
        if(rightLimit < projections.volcano.column) rightLimit += 2 ;
        if(upperLimit > projections.volcano.row) upperLimit -= 2 ;
        if(lowerLimit < projections.volcano.row) lowerLimit += 2 ;
    }

    public void setPiece(Point desiredPosition, OccupantType piece, Settlement settlementPointer){
        board[desiredPosition.row][desiredPosition.column].occupant = piece ;
        board[desiredPosition.row][desiredPosition.column].settlementPointer = settlementPointer ;
    }

    public void setSettlement(Point desiredPosition, Settlement newSettlement){
        newSettlement.addAdjacentTerrains(desiredPosition);
        newSettlement.addAdjacentSettlementsForMerge(desiredPosition);
        newSettlement.mergeSettlements();
        board[desiredPosition.row][desiredPosition.column].settlementPointer = newSettlement ;
    }

    public void processVolcanicDestruction(ProjectionPack projection){
        Settlement settlementA, settlementB;

        settlementA = board[projection.hex_a.row][projection.hex_a.column].settlementPointer ;
        settlementB = board[projection.hex_b.row][projection.hex_b.column].settlementPointer ;

        if(settlementA != null) markForRemoval(projection.hex_a);
        if(settlementB != null) markForRemoval(projection.hex_b);

        if(settlementA != null) partialSettlementDestruction(settlementA);
        if((settlementB != null) && (settlementA != settlementB)){ partialSettlementDestruction(settlementB); }

        // now some reconstruction stuff goes....
        if(settlementA != null) settlementReconstruction(settlementA);
        if((settlementB != null) && (settlementA != settlementB)){ settlementReconstruction(settlementB); }
    }

    private void markForRemoval(Point target){
        board[target.row][target.column].settlementPointer.markedForRemoval.add(target);
    }

    private void partialSettlementDestruction(Settlement settlement){
        int key;

        for(Point removal : settlement.markedForRemoval){
            key = coordinatesToKey(removal.row, removal.column) ;
            settlement.occupantPositions.remove(key);
            settlement.owner.playerSettlements.remove(key);
            board[removal.row][removal.column].occupant = OccupantType.NONE ;
            board[removal.row][removal.column].settlementPointer = null ;
        }

        settlement.markedForRemoval.clear();
    }

    private void settlementReconstruction(Settlement originalSettlement){
        Settlement partialSettlement ;

        for(Point origin : originalSettlement.occupantPositions.values()){
            partialSettlement = new Settlement(this) ;
            partialSettlement.owner = originalSettlement.owner ;
            partialSettlement.beginNewSettlement(origin);
            partialSettlement.addAdjacentTerrains(origin);
            board[origin.row][origin.column].settlementPointer = partialSettlement ;
        }

        for(Point origin : originalSettlement.occupantPositions.values()) {
            partialSettlement = board[origin.row][origin.column].settlementPointer ;
            partialSettlement.addAdjacentSettlementsForMerge(origin);
            partialSettlement.mergeSettlements();
        }

        for(Point origin : originalSettlement.occupantPositions.values()) {
            partialSettlement = board[origin.row][origin.column].settlementPointer ;
            partialSettlement.owner.playerSettlements.put(coordinatesToKey(origin.row, origin.column), partialSettlement);
        }
    }

    public void printBoard(){
        for(int i = upperLimit - 2 ; i < lowerLimit+2; i++){
            for(int j = leftLimit -2 ; j < rightLimit+2; j++){
                if(board[i][j] != null) {
                    System.out.print("[(" + i + "," + j +") " + board[i][j].terrain + " ") ;
                    System.out.print("O: " + board[i][j].occupant + " ");
                    System.out.print("S: " + board[i][j].settlementPointer);
                    if(board[i][j].settlementPointer != null){
                        System.out.print(" P: " + board[i][j].settlementPointer.owner);
                        System.out.print(" S: " + board[i][j].settlementPointer.size);
                    }
                    System.out.print('\n');
                }
            }
        }
    }

    public static Tile[] getTileStack() {
        return tileStack;
    }

    public static Hexagon[][] getBoard() {
        return board;
    }
}
