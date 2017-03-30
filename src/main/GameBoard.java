package main;

import main.enums.OccupantType;
import main.enums.TerrainType;

import java.util.HashMap;
import java.util.Random;

import static main.utils.formulas.coordinatesToKey;


public class GameBoard {

    private static final int ARRAY_DIMENSION = 210;
    private static final int NUM_TILE_COPIES = 3;
    private static final int NUM_TILES = 48;
    private static final int SIDES_IN_HEX = 6;

    private static final int EVEN_ROW_ADDS[] = {-1, -1, -1, 0, 1, 0};
    private static final int EVEN_COLUMN_ADDS[] = {-1, 0, 1, 1, 0, -1};
    private static final int ODD_ROW_ADDS[] = {0,-1,0,1,1,1};
    private static final int ODD_COLUMN_ADDS[] = {-1,0,1,1,0,-1};

    public static final int BOARD_CENTER = ARRAY_DIMENSION/2 ;
    private int upperLimit, lowerLimit, leftLimit, rightLimit ;

    private static Hexagon[][] board = new Hexagon[ARRAY_DIMENSION][ARRAY_DIMENSION];
    private HashMap<Integer,Integer> playableHexes = new HashMap<>() ;
    public static Tile tileStack[] = new Tile[NUM_TILES];
    private int tilePlayIndex;

    public GameBoard(){
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
        hex_aAdjacent = playableHexes.containsKey(coordinatesToKey(projections.hex_a.row, projections.hex_b.column));
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
            hex_aLevel = board[projections.hex_a.row][projections.hex_b.column].level + 1 ;
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
        // up-left, up, up-right, down-right, down, down-left,
        int rowAddArray[], columnAddArray[] ;
        int row , column ;

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

            if (board[row][column] == null) {
                playableHexes.put(coordinatesToKey(row, column), 1);
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
        boolean volcanoAligned, totoroPresent ;
        int tileUnderVolcano, tileUnderHexA, tileUnderHexB ;


        volcanoAligned = board[projection.volcano.row][projection.volcano.column].terrain == TerrainType.VOLCANO ;
        if(!volcanoAligned) return false ;

        totoroPresent = board[projection.hex_a.row][projection.hex_a.column].occupant == OccupantType.TOTORO ;
        if(totoroPresent) return false ;

        totoroPresent = board[projection.hex_b.row][projection.hex_b.column].occupant == OccupantType.TOTORO ;
        if(totoroPresent) return false ;

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

    public Tile releaseTopTile(){
        return tileStack[tilePlayIndex++] ;
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
        newSettlement.addAdjacentTerrains(desiredPosition, board);

        board[desiredPosition.row][desiredPosition.column].settlementPointer = newSettlement ;
        board[desiredPosition.row][desiredPosition.column].adjacencyList = new HashMap<>();
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

        for(Point remove : settlement.markedForRemoval){
            key = coordinatesToKey(remove.row, remove.column) ;
            settlement.occupantPositions.remove(key);
            board[remove.row][remove.column].occupant = OccupantType.NONE ;
            board[remove.row][remove.column].settlementPointer = null ;
        }
    }

    private void settlementReconstruction(Settlement settlement){
        Settlement partialSettlement = new Settlement();

        for(Point point : settlement.occupantPositions.values()){
            board[point.row][point.column].settlementPointer = partialSettlement ;
            partialSettlement.owner = settlement.owner ;
            partialSettlement.beginNewSettlement(point);
            partialSettlement.addAdjacentTerrains(point, board);
            partialSettlement.addAdjacentSettlementsForMerge(point, board);
            partialSettlement.mergeSettlements(board);
            partialSettlement.owner.playerSettlements.put(coordinatesToKey(point.row, point.column), partialSettlement);
        }
    }

    public void printBoard(){
        for(int i = upperLimit - 2 ; i < lowerLimit+2; i++){
            for(int j = leftLimit -2 ; j < rightLimit+2; j++){
                if(board[i][j] != null) {
                    System.out.print(board[i][j].terrain + "(" + i + "," + j +") ") ;
                }
            }
            System.out.print('\n');
        }
    }

    public Hexagon[][] getBoard(){return board;}

}
