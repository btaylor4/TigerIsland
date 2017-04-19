package main;

import java.util.HashMap;
import java.util.Scanner;

import main.enums.BuildOptions;
import main.enums.Direction;
import main.enums.OccupantType ;
import main.enums.TerrainType;
import main.utils.SettlePointPair;

public class Player {
    public int designator ;

    public int score;
    public int meeples;
    public int totoro;
    public int tigers;

    protected Tile tileHeld ;
    protected Point tilePlacement ;
    protected ProjectionPack tileProjection ;

    protected BuildOptions buildDecision ;
    protected Point buildPoint ;
    protected TerrainType terrainSelection ;
    protected Settlement selectedSettlement ;

    protected GameBoard game ;
    public HashMap<Integer, SettlePointPair> playerSettlements;
    public HashMap<Settlement, Point> uniqueSettlements ;


    public Player(GameBoard game, int designator) {
        this.designator = designator ;
        score = 0;
        meeples = 20;
        totoro = 3;
        tigers = 2;

        this.game = game;
        this.tileHeld = null;

        tilePlacement = null;
        tileProjection = null;

        buildDecision = null ;
        buildPoint = null ;
        terrainSelection = null ;
        selectedSettlement = null ;

        playerSettlements = new HashMap<>() ;
        uniqueSettlements = new HashMap<>() ;
    }

    public void getTileFromConsole(){
        System.out.println("Enter two terrains");
        Scanner input = new Scanner(System.in);
        String terrains = input.next();
        TerrainType hexAterrain, hexBterrain ;

        switch (terrains.charAt(0)){
            case 'g':
                hexAterrain = TerrainType.GRASS;
                break;
            case 'r':
                hexAterrain = TerrainType.ROCK;
                break;
            case 'l':
            case 'w':
                hexAterrain = TerrainType.LAKE;
                break;
            case 'f':
            case 'j':
                hexAterrain = TerrainType.JUNGLE;
                break;
            default:
                hexAterrain = TerrainType.VOLCANO ;
                break;
        }

        terrains = input.next();

        switch (terrains.charAt(0)){
            case 'g':
                hexBterrain = TerrainType.GRASS;
                break;
            case 'r':
                hexBterrain = TerrainType.ROCK;
                break;
            case 'l':
            case 'w':
                hexBterrain = TerrainType.LAKE;
                break;
            case 'f':
            case 'j':
                hexBterrain = TerrainType.JUNGLE;
                break;
            default:
                hexBterrain = TerrainType.VOLCANO ;
                break;
        }

        tileHeld = new Tile();
        tileHeld.assignTerrain(hexAterrain, hexBterrain);
    }

    public ProjectionPack projectTilePlacement(Tile tileBeingPlaced, Point desiredPoint){
        ProjectionPack projections = new ProjectionPack(desiredPoint) ;

        switch(tileBeingPlaced.rotation){
            case 1:
                projections.projectPoint(projections.hex_a, Direction.UP, Direction.LEFT);
                projections.projectPoint(projections.hex_b, Direction.UP, Direction.RIGHT);
                break;

            case 2:
                projections.projectPoint(projections.hex_a, Direction.UP, Direction.RIGHT);
                projections.projectPoint(projections.hex_b, Direction.NONE, Direction.RIGHT); //changing this because incorrect
                break;

            case 3:
                projections.projectPoint(projections.hex_a, Direction.NONE, Direction.RIGHT);
                projections.projectPoint(projections.hex_b, Direction.DOWN, Direction.RIGHT);
                break;

            case 4:
                projections.projectPoint(projections.hex_a, Direction.DOWN, Direction.RIGHT);
                projections.projectPoint(projections.hex_b, Direction.DOWN, Direction.LEFT);
                break;

            case 5:
                projections.projectPoint(projections.hex_a, Direction.DOWN, Direction.LEFT);
                projections.projectPoint(projections.hex_b, Direction.NONE, Direction.LEFT);
                break;

            case 6:
                projections.projectPoint(projections.hex_a, Direction.NONE, Direction.LEFT);
                projections.projectPoint(projections.hex_b, Direction.UP, Direction.LEFT);
                break;

            default:
                System.out.println("Something just fucked up");
                //MAIN.log.println("Something just fucked up");
                break;
        }

        return projections ;
    }

    public void playFirstTile(){
        game.setFirstTile();
        tileHeld = null ;
    }

    public void playTilePhase(Tile passedIn){
        drawTile(passedIn);
        determineTilePlacement();
        placeTile();
    }

    public void drawTile(Tile fromServer) {
        // tileHeld = game.releaseTopTile() ;
        //getTileFromConsole();
        tileHeld = fromServer ;
    }

    protected void determineTilePlacement(){
        // for human player placement determined by prompts
        // Ai determined by algorithm
        // server controlled player decisions are passed in
    }

    public void placeTile(){
        tileHeld.serverPoint = tilePlacement;
        game.setTile(tileHeld, tileProjection);
        System.out.println("Setting Tile At: (" + tilePlacement.row + ", " + tilePlacement.column + ") R: " + tileHeld.rotation);
        System.out.println("Volcano r:" + tileProjection.volcano.row + " c:" + tileProjection.volcano.column);
        System.out.println("Hex_A   r:" + tileProjection.hex_a.row + " c:" + tileProjection.hex_a.column) ;
        System.out.println("Hex_B   r:" + tileProjection.hex_b.row + " c:" + tileProjection.hex_b.column) ;

//        MAIN.log.println("Setting Tile At: (" + tilePlacement.row + ", " + tilePlacement.column + ") R: " + tileHeld.rotation);
//        MAIN.log.println("Volcano r:" + tileProjection.volcano.row + " c:" + tileProjection.volcano.column);
//        MAIN.log.println("Hex_A   r:" + tileProjection.hex_a.row + " c:" + tileProjection.hex_a.column);
//        MAIN.log.println("Hex_B   r:" + tileProjection.hex_b.row + " c:" + tileProjection.hex_b.column);
    }


    public void playBuildPhase(){
        updateSettlementCounts();
        determineBuildOption();
        placeBuildDecision();
    }

    public void updateSettlementCounts(){
        for(SettlePointPair pair: playerSettlements.values()){
            uniqueSettlements.put(pair.settlement, pair.point);
        }

        for(Point point : uniqueSettlements.values()){
            game.board[point.row][point.column].settlementPointer.updateExpansionCounts();
        }

        uniqueSettlements.clear();
    }

    protected void determineBuildOption(){
        // for human player placement determined by prompts
        // Ai determined by algorithm
        // server controlled player decisions are passed in
    }

    private void placeBuildDecision(){
        switch(buildDecision){
            case FOUND_SETTLEMENT:
                foundNewSettlement(buildPoint);
                System.out.println("New settlement at: " + buildPoint.row + " " + buildPoint.column);
//                MAIN.log.println("New settlement at: " + buildPoint.row + " " + buildPoint.column);
                break;

            case EXPAND:
                System.out.println("Expanding " + terrainSelection + " at: " + buildPoint.row + " " + buildPoint.column);
                expandSettlementMeeple(buildPoint, terrainSelection);
//                MAIN.log.println("Expanding " + terrainSelection + " at: " + buildPoint.row + " " + buildPoint.column);
                break;

            case TOTORO_SANCTUARY:
                System.out.println("Totoro at: " + buildPoint.row + " " + buildPoint.column + " " + selectedSettlement);
                placeTotoro(buildPoint, selectedSettlement);
//                MAIN.log.println("Totoro at: " + buildPoint.row + " " + buildPoint.column + " " + selectedSettlement);
                break;

            case TIGER_PLAYGROUND:
                System.out.println("Tiger at: " + buildPoint.row + " " + buildPoint.column + " " + selectedSettlement);
                placeTiger(buildPoint, selectedSettlement);
//                MAIN.log.println("Tiger at: " + buildPoint.row + " " + buildPoint.column + " " + selectedSettlement);
                break;

            default: // lets hope this never happens
//                MAIN.log.println("*******HIT DEFAULT CASE IN placeBuildDecision*********");
                break;
        }
    }

    private void foundNewSettlement(Point selectedPoint) {
        Settlement freshSettlement = new Settlement(game);
        freshSettlement.owner = this ;
        freshSettlement.ownerNumber = designator ;

        game.setSettlement(selectedPoint, freshSettlement);
        placeMeeple(selectedPoint, freshSettlement);
    }

    private void expandSettlementMeeple(Point settlementPoint, TerrainType terrainChoice){
        game.expandSettlement(settlementPoint, terrainChoice);
    }

    public void placeMeeple(Point selectedPoint, Settlement settlement) {
        playerSettlements.put(game.board[selectedPoint.row][selectedPoint.column].key,
                new SettlePointPair(settlement,selectedPoint));
        game.setPiece(selectedPoint, OccupantType.MEEPLE, settlement);
        int level = game.board[selectedPoint.row][selectedPoint.column].level ;
        score += (level * level) ;
        meeples -= level ;
    }

    public void placeTotoro(Point selectedPoint, Settlement settlement) {
        playerSettlements.put(game.board[selectedPoint.row][selectedPoint.column].key,
                new SettlePointPair(settlement,selectedPoint));
        game.setPiece(selectedPoint, OccupantType.TOTORO, settlement);
        score += 200;
        totoro--;
    }

    public void placeTiger(Point selectedPoint, Settlement settlement) {
        playerSettlements.put(game.board[selectedPoint.row][selectedPoint.column].key,
                new SettlePointPair(settlement,selectedPoint));
        game.setPiece(selectedPoint, OccupantType.TIGER, settlement);
        score += 75;
        tigers--;
    }


    public boolean isOutOfPieces(){
        return (meeples == 0 && totoro == 0) || (meeples == 0 && tigers == 0) || (tigers == 0 && totoro == 0);
    }

    // using for tests
    public void setMeeples(int meeples)
    {
        this.meeples = meeples;
    }

    public void setTotoro(int totoro)
    {
        this.totoro = totoro;
    }

    public void setTigers(int tigers)
    {
        this.tigers = tigers;
    }

    public int getMeeples() {
        return meeples;
    }

    public int getScore() {
        return score;
    }

    public int getTotoro() {
        return totoro;
    }

    public int getTigerPlayground() {
        return tigers;
    }

}
