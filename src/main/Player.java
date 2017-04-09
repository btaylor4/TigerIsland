package main;

import java.util.HashMap;
import java.util.Scanner;

import cucumber.api.java.eo.Se;
import main.enums.Direction;
import main.enums.OccupantType ;
import main.enums.TerrainType;

import static main.utils.constants.*;
import static main.utils.formulas.coordinatesToKey;

public class Player {
    public int designator ;

    public int score;
    private int meeples;
    private int totoro;
    private int tigers;
    private boolean isFinished;
    private boolean firstPlay;
    private boolean firstBuild;

    private GameBoard game;
    public Tile tileHeld ;
    public HashMap<Integer, Settlement> playerSettlements;

    public Player(GameBoard game, int designator) {
        this.designator = designator ;
        score = 0;
        meeples = 20;
        totoro = 3;
        tigers = 2;
        isFinished = false;
        firstPlay = false;
        firstBuild = false;

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
        while(!settlementChoice.expand(terrainChoice))
        {
            terrainChoice = determineTerrainByHuman();
        }

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

    public void placeTotoro(Point selectedPoint, Settlement settlement) {
        if(game.isValidTotoroPosition(selectedPoint, settlement))
        {
            game.setPiece(selectedPoint, OccupantType.TOTORO, settlement);
            placeTotoro();
        }
    }

    public void placeTiger(Point selectedPoint, Settlement settlement) {
        if(game.isValidTigerPosition(selectedPoint, settlement))
        {
            game.setPiece(selectedPoint, OccupantType.TIGERPLAYGROUND, settlement);
            placeTiger();
        }
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
                //placeTotoro();
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

    public int determineRotationForNukingAI(Settlement mySet)
    {
        ProjectionPack projection;
        int minimumRotationValue = 1;
        int minimumNumberOfMeeplesNuked = Integer.MAX_VALUE;

        for(int i = 1; i < 7; i++)
        {
            int meeplesBeingNuked = 0;

            tileHeld.setRotation(i);
            projection = projectTilePlacement(tileHeld, mySet.adjacentMeeples.endPointToNuke);
            projection.projectedLevel = game.getProjectedHexLevel(projection);

            if(game.isValidTilePlacement(projection))
            {
                if(game.board[projection.hex_a.row][projection.hex_a.column].occupant == OccupantType.MEEPLE)
                {
                    if(game.board[projection.hex_b.row][projection.hex_b.column].occupant == OccupantType.MEEPLE)
                    {
                        meeplesBeingNuked++;
                    }

                    meeplesBeingNuked++;
                }

                if(meeplesBeingNuked < minimumNumberOfMeeplesNuked)
                {
                    minimumNumberOfMeeplesNuked = meeplesBeingNuked;
                    minimumRotationValue = i;
                }
            }
        }

        return minimumRotationValue;
    }

    public void determineBuildByAI() {
        //Priority list
        int row = 0;
        int column = 0;
        int mostMeeplesInHex = -1;
        Settlement settlementChoice = new Settlement(game);

        if(!firstBuild)
        {
            Point firstPoint = new Point(104, 105);
            Settlement freshSettlement = new Settlement(game);
            freshSettlement.owner = this ;
            freshSettlement.ownerNumber = designator ;
            freshSettlement.beginNewSettlement(firstPoint);
            game.setSettlement(firstPoint, freshSettlement);
            placeMeeple(firstPoint, freshSettlement);
            playerSettlements.put(coordinatesToKey(firstPoint.row, firstPoint.column), freshSettlement);
            firstBuild = true;
            return;
        }

        for (Settlement mySets : playerSettlements.values())
        {
            //choose point in such away that you can nuke the settlement and only lose 1-2 pieces max
            //check if I can place a totoro
            if (mySets.totoroSanctuaries == 0)
            {
                if(mySets.size >= 5)
                {
                    Point point = mySets.findEndPoints();

                    for (int i = 0; i < SIDES_IN_HEX; i++)
                    {
                        row = point.row + ROW_ADDS[i];
                        column = point.column + COLUMN_ADDS[i];

                        if (game.board[row][column] != null)
                        {
                            if(!game.isValidTotoroPosition(new Point(row, column), mySets))
                                continue;

                            else if(game.isValidTotoroPosition(new Point(row, column), mySets))
                            {
                                if(mySets.adjacentMeeples.checkPieceAdjacencies(point) <= 1)
                                {
                                    point = new Point(row, column);
                                    break;
                                }
                            }

                            else if(game.isValidTotoroPosition(new Point(row, column), mySets))
                                point = new Point(row, column);

                        }
                    }

                    if(game.isValidTotoroPosition(point, mySets))
                    {
                        System.out.println("Totoro has been fucking placed motherfucker! Score: " + score);
                        placeTotoro(point, mySets);
                        return;
                    }
                }
            }
        }

        for (Settlement mySets : playerSettlements.values())
        {
            //check if I can place a tiger
            if (mySets.tigerPlaygrounds == 0)
            {
                Point point = mySets.findEndPoints();

                for (int i = 0; i < SIDES_IN_HEX; i++)
                {
                    row = point.row + ROW_ADDS[i];
                    column = point.column + COLUMN_ADDS[i];

                    if (game.board[row][column] != null)
                    {
                        if(!game.isValidTigerPosition(new Point(row, column), mySets))
                            continue;

                        else if(game.isValidTigerPosition(new Point(row, column), mySets))
                        {
                            if(mySets.adjacentMeeples.checkPieceAdjacencies(point) <= 1)
                            {
                                point = new Point(row, column);
                                break;
                            }
                        }

                        else if(game.isValidTigerPosition(new Point(row, column), mySets))
                            point = new Point(row, column);
                    }
                }

                if(game.isValidTigerPosition(point, mySets))
                {
                    placeTiger(point, mySets);
                    return;
                }
            }
        }

        for (Settlement mySets : playerSettlements.values())
        {
            if (mySets.size + mySets.grasslands.size() >= 1)
            {
                if(mySets.expand(TerrainType.GRASSLANDS))
                {
                    mySets.mergeSettlements();
                    return;
                }
            }

            else if (mySets.size + mySets.lakes.size() >= 1)
            {
                if(mySets.expand(TerrainType.LAKE))
                {
                    mySets.mergeSettlements();
                    return;
                }
            }

            else if (mySets.size + mySets.forests.size() >= 1)
            {

                if(mySets.expand(TerrainType.JUNGLE))
                {
                    mySets.mergeSettlements();
                    return;
                }
            }

            else if (mySets.size + mySets.rocky.size() >= 1)
            {

                if(mySets.expand(TerrainType.ROCKY))
                {
                    mySets.mergeSettlements();
                    return;
                }
            }
        }

        for(Settlement mySet : playerSettlements.values())
        {
            if(mySet.size > mostMeeplesInHex)
            {
                mostMeeplesInHex = mySet.size;
                settlementChoice = mySet;
            }
        }

        if(game.isValidSettlementPosition(placeMeepleOneAway(settlementChoice)))
        {
            Point firstPoint = placeMeepleOneAway(settlementChoice);
            Settlement freshSettlement = new Settlement(game);
            freshSettlement.owner = this ;
            freshSettlement.ownerNumber = designator ;
            freshSettlement.beginNewSettlement(firstPoint);
            game.setSettlement(firstPoint, freshSettlement);
            placeMeeple(firstPoint, freshSettlement);
            playerSettlements.put(coordinatesToKey(firstPoint.row, firstPoint.column), freshSettlement);
        }
    }

    public void determineTilePlacementByAI()
    {
        Point selectedPoint;
        ProjectionPack projection;
        drawTile(); //draw tile

        int mostMeeplesInHex = -1;
        Settlement settlementChoice = new Settlement(game);

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
                            selectedPoint = new Point(108, 103);
                            tileHeld.setRotation(2);
                            projection = projectTilePlacement(tileHeld, selectedPoint);
                            projection.projectedLevel = game.getProjectedHexLevel(projection);
                            game.setTile(tileHeld, projection);
                            break;

                        case LAKE:

                        case JUNGLE:
                            //set orientation to 5
                            selectedPoint = new Point(102, 106);
                            tileHeld.setRotation(3);
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
                            selectedPoint = new Point(108, 103);
                            tileHeld.setRotation(2);
                            projection = projectTilePlacement(tileHeld, selectedPoint);
                            projection.projectedLevel = game.getProjectedHexLevel(projection);
                            game.setTile(tileHeld, projection);
                            break;

                        case LAKE:

                        case JUNGLE:
                            //set orientation to 5
                            selectedPoint = new Point(102, 106);
                            tileHeld.setRotation(3);
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
                for(Settlement mySet : playerSettlements.values())
                {
                    if(mySet.totoroSanctuaries == 1)
                    {
                        tileHeld.setRotation(determineRotationForNukingAI(mySet));
                        projection = projectTilePlacement(tileHeld, mySet.adjacentMeeples.endPointToNuke);
                        projection.projectedLevel = game.getProjectedHexLevel(projection);

                        if(game.isValidTilePlacement(projection))
                            game.setTile(tileHeld, projection);
                    }
                }

                for(Settlement mySet : playerSettlements.values())
                {
                    if(mySet.tigerPlaygrounds == 1)
                    {
                        tileHeld.setRotation(determineRotationForNukingAI(mySet));
                        projection = projectTilePlacement(tileHeld, mySet.adjacentMeeples.endPointToNuke);
                        projection.projectedLevel = game.getProjectedHexLevel(projection);

                        if(game.isValidTilePlacement(projection))
                            game.setTile(tileHeld, projection);
                    }
                }

                for(Settlement mySet : playerSettlements.values())
                {
                    //check if I can place a tile that will add to one of the adjacency lists to make expansion better
                    //and away from volcanoes
                }

                //place next to settlement that would allow for meeple placement one away
                for(Settlement mySet : playerSettlements.values())
                {
                    if(mySet.size > mostMeeplesInHex)
                    {
                        mostMeeplesInHex = mySet.size;
                        settlementChoice = mySet;
                    }
                }

                projection = projectTilePlacement(tileHeld, determineTilePlacementForPlacingAI(settlementChoice));
                projection.projectedLevel = game.getProjectedHexLevel(projection);

                if(game.isValidTilePlacement(projection))
                {
                    game.setTile(tileHeld, projection);
                }
            }
        }

        System.out.println("AI Tile rotation: " + tileHeld.rotation);
    }

    public boolean determineTilePlacementForExpansion(Settlement mySet, TerrainType terrain)
    {
        switch(terrain)
        {
            case JUNGLE:
                if(mySet.forests.size() >= 1)
                {

                }
                break;

            case LAKE:
                if(mySet.lakes.size() >= 1)
                {

                }

                break;

            case GRASSLANDS:
                if(mySet.grasslands.size() >= 1)
                {

                }
                break;

            case ROCKY:
                if(mySet.rocky.size() >= 1)
                {

                }
                break;
        }
        return false;
    }

    public Point determineTilePlacementForPlacingAI(Settlement settlement)
    {
        Point point = settlement.findEndPoints();
        Point volcanoPlacement = null;

        ProjectionPack projection = null;
        int defaultRotation = 1;

        int row = point.row;
        int column = point.column;

        for(int i = 0; i < rowOneAway.length; i++)
        {
            row = point.row + rowOneAway[i];
            column = point.column + columnOneAway[i];

            for(int k = 0; k < SIDES_IN_HEX; k++)
            {
                int volcanoRow = row + ROW_ADDS[k];
                int volcanoColumn = column += COLUMN_ADDS[k];

                if(game.board[volcanoRow][volcanoColumn] == null)
                {
                    for(int j = 1; j < 7; j++)
                    {
                        tileHeld.setRotation(j);
                        projection = projectTilePlacement(tileHeld, new Point(row, column));
                        projection.projectedLevel = game.getProjectedHexLevel(projection);

                        if(game.isValidTilePlacement(projection))
                        {
                            if(!checkForAdjacaentVolcanoes(projection))
                            {
                                volcanoPlacement = new Point(row, column);
                                tileHeld.setRotation(j);
                                projection = projectTilePlacement(tileHeld, new Point(row, column));
                                projection.projectedLevel = game.getProjectedHexLevel(projection);
                                return volcanoPlacement;
                            }

                            else
                            {
                                defaultRotation = j;
                                volcanoPlacement = new Point(row, column);
                            }
                        }
                    }
                }
            }
        }

        tileHeld.setRotation(defaultRotation);
        projection = projectTilePlacement(tileHeld, new Point(row, column));
        projection.projectedLevel = game.getProjectedHexLevel(projection);

        return volcanoPlacement;
    }

    public boolean checkForAdjacaentVolcanoes(ProjectionPack projection)
    {
        int hexARow = projection.hex_a.row;
        int hexAColumn = projection.hex_a.column;
        int hexBRow = projection.hex_b.row;
        int hexBColumn = projection.hex_b.column;

        for(int i = 0; i < SIDES_IN_HEX; i++)
        {
            hexARow += ROW_ADDS[i];
            hexAColumn += COLUMN_ADDS[i];
            hexBRow += ROW_ADDS[i];
            hexBColumn += COLUMN_ADDS[i];

            if(game.board[hexARow][hexAColumn] != null && hexARow != projection.volcano.row && hexAColumn != projection.volcano.column)
            {
                if(game.board[hexARow][hexAColumn].terrain == TerrainType.VOLCANO)
                {
                    if(game.board[hexBRow][hexBColumn] != null && hexBRow != projection.volcano.row && hexBColumn != projection.volcano.column)
                    {
                        if(game.board[hexARow][hexAColumn].terrain == TerrainType.VOLCANO)
                        {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean checkForVolcanoesNearMeeplePlacement(Point point)
    {
        int row = point.row;
        int column = point.column;

        for(int i = 0; i < SIDES_IN_HEX; i++)
        {
            row += ROW_ADDS[i];
            column += COLUMN_ADDS[i];

            if(game.board[row][column] != null)
            {
                if(game.board[row][column].terrain == TerrainType.VOLCANO)
                {
                    if(game.board[row][column].tileNumber == game.board[point.row][point.column].tileNumber)
                        continue;
                    else
                        return true;
                }
            }
        }

        return false;
    }

    public Point placeMeepleOneAway(Settlement mySet)
    {
        Point point = mySet.findEndPoints();
        Point selectedPoint = null;

        int row = point.row;
        int column = point.column;

        for(int i = 0; i < SIDES_IN_HEX; i++)
        {
            row = point.row + rowOneAway[i];
            column = point.column + columnOneAway[i];

            if(game.board[row][column] != null && game.isValidSettlementPosition(new Point(row, column)))
            {
                if(!checkForVolcanoesNearMeeplePlacement(new Point(row, column)))
                {
                    selectedPoint = new Point(row, column);
                    break;
                }

                else
                    selectedPoint = new Point(row, column);
            }
        }

        return selectedPoint;
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

    public void placeTotoro(){
        //mainly for testing purposes
        totoro--;
        score += 200;
    }

    public void placeTiger(){
        tigers--;
        score += 75;
    }
}
