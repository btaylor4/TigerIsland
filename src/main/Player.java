package main;

import java.util.HashMap;
import java.util.Scanner;

import main.enums.Direction;
import main.enums.OccupantType ;
import main.enums.TerrainType;

import static main.utils.formulas.coordinatesToKey;

public class Player {
    public int designator ;

    public int score;
    private int meeples;
    private int totoro;
    private int tigers;
    private boolean isFinished;
    private boolean firstPlay;

    private GameBoard game;
    private Tile tileHeld ;
    public HashMap<Integer, Settlement> playerSettlements;

    public Player(GameBoard game, int designator) {
        this.designator = designator ;
        score = 0;
        meeples = 20;
        totoro = 3;
        tigers = 2;
        isFinished = false;
        firstPlay = false;

        this.game = game;
        this.tileHeld = null ;
        playerSettlements = new HashMap<>() ;
    }

    public void drawTile() {
        // tileHeld = game.releaseTopTile() ;
        getTileFromConsole();
    }

    public void getTileFromConsole(){
        System.out.println("Enter two terrains");
        Scanner input = new Scanner(System.in);
        String terrains = input.next();
        TerrainType hexAterrain, hexBterrain ;

        switch (terrains.charAt(0)){
            case 'g':
                hexAterrain = TerrainType.GRASSLANDS;
                break;
            case 'r':
                hexAterrain = TerrainType.ROCKY ;
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
                hexBterrain = TerrainType.GRASSLANDS;
                break;
            case 'r':
                hexBterrain = TerrainType.ROCKY ;
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
                projections.projectPoint(projections.hex_b, Direction.NONE, Direction.RIGHT);
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
                break;
        }

        return projections ;
    }

    public void playFirstTile(){
        game.setFirstTile();
        tileHeld = null ;
    }

    public Point determineTilePositionByHuman(){
        int row, column, rotation ;

        System.out.print("Volcano row choice: ");
        row = chooseOption();

        System.out.print("Volcano column choice: ");
        column = chooseOption();

        System.out.print("Rotation choice 1 - 6: ");
        rotation = chooseOption();

        tileHeld.setRotation(rotation);

        return (new Point(row, column));
    }

    public Point determinePiecePositionByHuman(){
        int row, column ;

        System.out.print("Piece row choice: ");
        row = chooseOption();

        System.out.print("Piece col choice: ");
        column = chooseOption();

        return (new Point(row, column));
    }

    public void placeTile(){
        Point selectedPoint ;
        ProjectionPack projection ;

        do{
            selectedPoint = determineTilePositionByHuman();
            projection = projectTilePlacement(tileHeld, selectedPoint);
            projection.projectedLevel = game.getProjectedHexLevel(projection) ;
        } while(!game.isValidTilePlacement(projection)) ;

        game.setTile(tileHeld, projection);
        tileHeld = null ;
    }

    public void playTilePhase(){
        drawTile();
        placeTile();
    }

    public int chooseOption() {
        Scanner input = new Scanner(System.in);
        return input.nextInt();
    }

    private void foundNewSettlement() {
        Point selectedPoint ;

        do{
            selectedPoint = determinePiecePositionByHuman();
        } while(!game.isValidSettlementPosition(selectedPoint)) ;
        // TODO: The board currently does not/cannot check if it is an underhanded expansion

        Settlement freshSettlement = new Settlement(game);
        freshSettlement.owner = this ;
        freshSettlement.ownerNumber = designator ;
        freshSettlement.beginNewSettlement(selectedPoint);

        game.setSettlement(selectedPoint, freshSettlement);
        placeMeeple(selectedPoint, freshSettlement);

        playerSettlements.put(coordinatesToKey(selectedPoint.row, selectedPoint.column), freshSettlement);
    }

    private void expandSettlementMeeple() {
        Settlement settlementChoice = determineSettlementByHuman();
        TerrainType terrainChoice = determineTerrainByHuman();

        System.out.println("terrain is: " + terrainChoice);
        settlementChoice.expand(terrainChoice);
        settlementChoice.mergeSettlements();
    }

    private Settlement determineSettlementByHuman() {
        System.out.print("Row of existing settlement: ");
        int settlementRow = chooseOption();

        System.out.print("Column of existing settlement: ");
        int settlementColumn = chooseOption();

        return playerSettlements.get(coordinatesToKey(settlementRow,settlementColumn));
    }

    private TerrainType determineTerrainByHuman() {
        System.out.println("What terrain type to expand on: j l g r");
        Scanner input = new Scanner(System.in);
        String terrains = input.next();

        switch (terrains.charAt(0)){
            case 'g':
                return TerrainType.GRASSLANDS;

            case 'r':
                return  TerrainType.ROCKY;

            case 'l':
            case 'w':
                return TerrainType.LAKE;

            case 'j':
            case 'f':
                return TerrainType.JUNGLE;
        }

        return TerrainType.VOLCANO;
    }

    public void expandSettlementTotoro(Point selectedPoint, Settlement settlement) {
        //game.isValidTotoroPosition(selectedPoint, settlement);
        score += 200;
        totoro--;
    }

    public void expandSettlementPlayground(Point selectedPoint, Settlement settlement) {
        //game.isValidPlaygroundPosition(selectedPoint, settlement);
        score += 75;
        tigers--;
    }

    private void determineBuildOptionByHuman() {
        System.out.println("Build options 1: new settlement, 2: expand settlement");
        int buildOption = chooseOption();

        switch(buildOption) {
            case 1:
                foundNewSettlement();
                System.out.println("Successfully Founded");
                break;

            case 2:
                expandSettlementMeeple();
                break;

            case 3:
                //expandSettlementTotoro();
                break;

            default:
                System.out.println("Invalid choice");
        }
    }

    public void playBuildPhase(){
        determineBuildOptionByHuman();
    }

    public boolean isOutOfPieces()
    {
        return (meeples == 0 && totoro == 0) || (meeples == 0 && tigers == 0) || (tigers == 0 && totoro == 0);
    }

    public boolean hasPlayerLost() {
        if (meeples != 0)
            return false;

        else
        {
            for(Settlement mySets : playerSettlements.values())
            {
                if(mySets.totoroSanctuaries != 0)
                    continue;

                else if(mySets.size >= 5) //do we also have to check if there's a hex that we can put on?
                    return false;
            }

            for(Settlement mySets : playerSettlements.values())
            {
                if(mySets.tigerPlaygrounds != 0)
                    continue;
            }
        }

        return true;
    }

    public void placeMeeple(Point selectedPoint, Settlement settlement) {
        game.setPiece(selectedPoint, OccupantType.MEEPLE, settlement);
        int level = game.board[selectedPoint.row][selectedPoint.column].level ;
        score += (level * level) ;
        meeples -= level ;
    }

    public void determineBuildByAI() {
        //Priority list
        for (Settlement mySets : playerSettlements.values()) {
            //check if I can place a totoro
            if (mySets.totoroSanctuaries == 0) {
                //choose point in such away that you can nuke the settlement and only lose 1-2 pieces max
                //placeTotoro();
            }

            //check if I can place a tiger
            else if (mySets.tigerPlaygrounds == 0) {
                //choose point in such away that you can nuke the settlement and only lose 1-2 pieces max
                //placeTiger();
            }

            //not sure to check if I can expand a settlement that won't be able to nuke?
            //next 4 if statements check if I can expand a settlement giving me a a totoro placement on next turn
            else if (mySets.size + mySets.grasslands.size() >= 5) {
                expandSettlementMeeple();
            } else if (mySets.size + mySets.lakes.size() >= 5) {
                expandSettlementMeeple();
            } else if (mySets.size + mySets.forests.size() >= 5) {
                expandSettlementMeeple();
            } else if (mySets.size + mySets.rocky.size() >= 5) {
                expandSettlementMeeple();
            }

            //found new settlement 1 or 2 away from a pre existing settlement
            foundNewAISettlement();
        }
    }

    public void foundNewAISettlement()
    {
        //check best place to place meeple
    }

    public void determineTilePlacementByAI()
    {
        Point selectedPoint;
        ProjectionPack projection;
        drawTile(); //draw tile

        if (designator == 1)
        {
            if (firstPlay == false)
            {
                if (tileHeld.hexA.terrain == tileHeld.hexB.terrain)//check if terrains are equal
                {
                    switch (tileHeld.hexA.terrain) {
                        case ROCKY:

                        case GRASSLANDS:
                            //set orientation to 2
                            selectedPoint = new Point(107, 102);
                            tileHeld.setRotation(2);
                            projection = projectTilePlacement(tileHeld, selectedPoint);
                            projection.projectedLevel = game.getProjectedHexLevel(projection);
                            game.setTile(tileHeld, projection);
                            break;

                        case LAKE:

                        case JUNGLE:
                            //set orientation to 5
                            selectedPoint = new Point(101, 106);
                            tileHeld.setRotation(5);
                            projection = projectTilePlacement(tileHeld, selectedPoint);
                            projection.projectedLevel = game.getProjectedHexLevel(projection);
                            game.setTile(tileHeld, projection);
                            break;
                    }
                }

                else
                {
                    switch (tileHeld.hexA.terrain)
                    {
                        case ROCKY:

                        case GRASSLANDS:
                            selectedPoint = new Point(107, 102);
                            tileHeld.setRotation(2);
                            projection = projectTilePlacement(tileHeld, selectedPoint);
                            projection.projectedLevel = game.getProjectedHexLevel(projection);
                            game.setTile(tileHeld, projection);
                            break;

                        case LAKE:

                        case JUNGLE:
                            //set orientation to 5
                            selectedPoint = new Point(101, 106);
                            tileHeld.setRotation(5);
                            projection = projectTilePlacement(tileHeld, selectedPoint);
                            projection.projectedLevel = game.getProjectedHexLevel(projection);
                            game.setTile(tileHeld, projection);
                            break;
                    }
                }

                firstPlay = true;
            }

            else
            {
                //priority list
                // check if I can nuke in such a way to make different settlements

                for(Settlement mySet : playerSettlements.values())
                {
                    /*
                       if I have a settlement that has a totoro,
                       see if I can nuke it in such a way to only lose pieces in a way to have a totoro
                       option in one or two more turns:
                       (settlement size >= 5 or expansion options gives me >= 5)
                    */

                    if(mySet.totoroSanctuaries > 0)
                    {

                    }

                    else if(mySet.tigerPlaygrounds > 0)
                    {

                    }
                }
                //place tile in such a way that I can expand away from volcanos
                //build one settlement at a time
            }
        }
    }

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
