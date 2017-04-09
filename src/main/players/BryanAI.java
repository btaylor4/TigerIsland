package main.players;

import main.*;
import main.enums.OccupantType;
import main.enums.TerrainType;
import main.utils.SettlePointPair;

import static main.utils.constants.*;
import static main.utils.constants.columnOneAway;
import static main.utils.constants.rowOneAway;
import static main.utils.formulas.coordinatesToKey;

public class BryanAI extends Player {

    private boolean isFinished;
    private boolean firstPlay;
    private boolean firstBuild;

    public BryanAI(GameBoard gamePointer, int designator){
        super(gamePointer, designator);

        isFinished = false;
        firstPlay = false;
        firstBuild = false;

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
            projection = projectTilePlacement(tileHeld, mySet.endPointToNuke);
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

    public void determineBuildByAI()
    {
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
            playerSettlements.put(coordinatesToKey(firstPoint.row, firstPoint.column),
                    new SettlePointPair(freshSettlement, firstPoint));
            firstBuild = true;
            return;
        }

        for (SettlePointPair mySets : playerSettlements.values())
        {
            //choose point in such away that you can nuke the settlement and only lose 1-2 pieces max
            //check if I can place a totoro
            if (mySets.settlement.totoroSanctuaries == 0)
            {
                if(mySets.settlement.size >= 5)
                {
                    Point point = mySets.settlement.findEndPoints();

                    for (int i = 0; i < SIDES_IN_HEX; i++)
                    {
                        row = point.row + ROW_ADDS[i];
                        column = point.column + COLUMN_ADDS[i];

                        if (game.board[row][column] != null)
                        {
                            if(!game.isValidTotoroPosition(new Point(row, column), mySets.settlement))
                                continue;

                            else if(game.isValidTotoroPosition(new Point(row, column), mySets.settlement))
                            {
                                if(mySets.settlement.checkPieceAdjacencies(point) <= 1)
                                {
                                    point = new Point(row, column);
                                    break;
                                }
                            }

                            else if(game.isValidTotoroPosition(new Point(row, column), mySets.settlement))
                                point = new Point(row, column);

                        }
                    }

                    if(game.isValidTotoroPosition(point, mySets.settlement))
                    {
                        System.out.println("Totoro has been fucking placed motherfucker! Score: " + score);
                        placeTotoro(point, mySets.settlement);
                        return;
                    }
                }
            }
        }

        for (SettlePointPair mySets : playerSettlements.values())
        {
            //check if I can place a tiger
            if (mySets.settlement.tigerPlaygrounds == 0)
            {
                Point point = mySets.settlement.findEndPoints();

                for (int i = 0; i < SIDES_IN_HEX; i++)
                {
                    row = point.row + ROW_ADDS[i];
                    column = point.column + COLUMN_ADDS[i];

                    if (game.board[row][column] != null)
                    {
                        if(!game.isValidTigerPosition(new Point(row, column), mySets.settlement))
                            continue;

                        else if(game.isValidTigerPosition(new Point(row, column), mySets.settlement))
                        {
                            if(mySets.settlement.checkPieceAdjacencies(point) <= 1)
                            {
                                point = new Point(row, column);
                                break;
                            }
                        }

                        else if(game.isValidTigerPosition(new Point(row, column), mySets.settlement))
                            point = new Point(row, column);
                    }
                }

                if(game.isValidTigerPosition(point, mySets.settlement))
                {
                    placeTiger(point, mySets.settlement);
                    return;
                }
            }
        }

        for (SettlePointPair mySets : playerSettlements.values())
        {
            if (mySets.settlement.size + mySets.settlement.grasslands.size() >= 1)
            {
                if(mySets.settlement.grassExpansions > 0)
                {
                    mySets.settlement.expand(TerrainType.GRASSLANDS);
                    mySets.settlement.mergeSettlements();
                    return;
                }
            }

            else if (mySets.settlement.size + mySets.settlement.lakes.size() >= 1)
            {
                if(mySets.settlement.lakeExpansions > 0)
                {
                    mySets.settlement.expand(TerrainType.LAKE);
                    mySets.settlement.mergeSettlements();
                    return;
                }
            }

            else if (mySets.settlement.size + mySets.settlement.jungles.size() >= 1)
            {

                if(mySets.settlement.jungleExpansions > 0)
                {
                    mySets.settlement.expand(TerrainType.JUNGLE);
                    mySets.settlement.mergeSettlements();
                    return;
                }
            }

            else if (mySets.settlement.size + mySets.settlement.rocky.size() >= 1)
            {

                if(mySets.settlement.rockyExpansions > 0)
                {
                    mySets.settlement.expand(TerrainType.ROCKY);
                    mySets.settlement.mergeSettlements();
                    return;
                }
            }
        }

        for(SettlePointPair mySet : playerSettlements.values())
        {
            if(mySet.settlement.size > mostMeeplesInHex)
            {
                mostMeeplesInHex = mySet.settlement.size;
                settlementChoice = mySet.settlement;
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
            playerSettlements.put(coordinatesToKey(firstPoint.row, firstPoint.column),
                    new SettlePointPair(freshSettlement, firstPoint));
        }
    }

    public Tile determineTilePlacementByAI()
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
                            tileHeld.serverPoint = projection.volcano;
                            return tileHeld;

                        case LAKE:

                        case JUNGLE:
                            //set orientation to 5
                            selectedPoint = new Point(102, 106);
                            tileHeld.setRotation(3);
                            projection = projectTilePlacement(tileHeld, selectedPoint);
                            projection.projectedLevel = game.getProjectedHexLevel(projection);
                            game.setTile(tileHeld, projection);
                            tileHeld.serverPoint = projection.volcano;
                            return tileHeld;
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
                            tileHeld.serverPoint = projection.volcano;
                            return tileHeld;

                        case LAKE:

                        case JUNGLE:
                            //set orientation to 5
                            selectedPoint = new Point(102, 106);
                            tileHeld.setRotation(3);
                            projection = projectTilePlacement(tileHeld, selectedPoint);
                            projection.projectedLevel = game.getProjectedHexLevel(projection);
                            game.setTile(tileHeld, projection);
                            tileHeld.serverPoint = projection.volcano;
                            return tileHeld;
                    }
                }

                firstPlay = true;
            }

            else
            {
                //priority list
                for(SettlePointPair mySet : playerSettlements.values())
                {
                    if(mySet.settlement.totoroSanctuaries == 1)
                    {
                        tileHeld.setRotation(determineRotationForNukingAI(mySet.settlement));
                        projection = projectTilePlacement(tileHeld, mySet.settlement.endPointToNuke);
                        projection.projectedLevel = game.getProjectedHexLevel(projection);

                        if(game.isValidTilePlacement(projection))
                        {
                            game.setTile(tileHeld, projection);
                            tileHeld.serverPoint = projection.volcano;
                            return tileHeld;
                        }
                    }
                }

                for(SettlePointPair mySet : playerSettlements.values())
                {
                    if(mySet.settlement.tigerPlaygrounds == 1)
                    {
                        tileHeld.setRotation(determineRotationForNukingAI(mySet.settlement));
                        projection = projectTilePlacement(tileHeld, mySet.settlement.endPointToNuke);
                        projection.projectedLevel = game.getProjectedHexLevel(projection);

                        if(game.isValidTilePlacement(projection)) {
                            game.setTile(tileHeld, projection);
                            tileHeld.serverPoint = projection.volcano;
                            return tileHeld;
                        }
                    }
                }

                for(SettlePointPair mySet : playerSettlements.values())
                {
                    //check if I can place a tile that will add to one of the adjacency lists to make expansion better
                    //and away from volcanoes
                }

                //place next to settlement that would allow for meeple placement one away
                for(SettlePointPair mySet : playerSettlements.values())
                {
                    if(mySet.settlement.size > mostMeeplesInHex)
                    {
                        mostMeeplesInHex = mySet.settlement.size;
                        settlementChoice = mySet.settlement;
                    }
                }

                projection = projectTilePlacement(tileHeld, determineTilePlacementForPlacingAI(settlementChoice));
                projection.projectedLevel = game.getProjectedHexLevel(projection);

                if(game.isValidTilePlacement(projection))
                {
                    game.setTile(tileHeld, projection);
                    tileHeld.serverPoint = projection.volcano;
                    return tileHeld;
                }
            }
        }

        System.out.println("AI Tile rotation: " + tileHeld.rotation);
        return tileHeld;
    }

    public boolean determineTilePlacementForExpansion(Settlement mySet, TerrainType terrain)
    {
        switch(terrain)
        {
            case JUNGLE:
                if(mySet.jungles.size() >= 1)
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

        for(Point myPoint : mySet.occupantPositions.values())
        {
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

            if(selectedPoint != null)
                return  selectedPoint;
        }

        return selectedPoint;
    }

    public boolean hasPlayerLost(){ // what is this even for?
        if (meeples != 0)
            return false;

        else{
            for(SettlePointPair mySets : playerSettlements.values())
            {
                if(mySets.settlement.totoroSanctuaries != 0)
                    continue;

                else if(mySets.settlement.size >= 5) //do we also have to check if there's a hex that we can put on?
                    return false;
            }

            for(SettlePointPair mySets : playerSettlements.values())
            {
                if(mySets.settlement.tigerPlaygrounds != 0)
                    continue;
            }
        }

        return true;
    }

}
