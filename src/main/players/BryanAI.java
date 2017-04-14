package main.players;

import main.*;
import main.enums.BuildOptions;
import main.enums.OccupantType;
import main.enums.TerrainType;
import main.players.AIUtils.SettlementData;
import main.utils.SettlePointPair;

import java.util.ArrayList;
import java.util.HashMap;

import static main.utils.constants.*;
import static main.utils.constants.columnOneAway;
import static main.utils.constants.rowOneAway;
import static main.utils.formulas.coordinatesToKey;

public class BryanAI extends Player {

    private boolean isFinished;
    private boolean firstPlay;
    private boolean firstBuild;
    private HashMap<Integer, Point> foundableSpots;
    public BuildOptions buildDecision ;
    public Point buildPoint ;
    public TerrainType expansionAction;
    public HashMap<Settlement, SettlementData> setData;
    public ArrayList<SettlementData> mySettlementData;

    public BryanAI(GameBoard gamePointer, int designator){
        super(gamePointer, designator);

        isFinished = false;
        firstPlay = false;
        firstBuild = false;
        foundableSpots = new HashMap<>();
        buildDecision = BuildOptions.NONE;
        buildPoint = null;
        expansionAction = TerrainType.GRASS;
        setData = new HashMap<>();
        mySettlementData = new ArrayList<>();
    }

    public void setTile(Tile tile)
    {
        super.tileHeld = tile;
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
        updateFoundPositions();
        getMySettlements();
        updateSettlementCounts();
        getMySettlements();
        //Priority list
        int row = 0;
        int column = 0;
        int mostMeeplesInHex = -1;
        Settlement settlementChoice = new Settlement(game);

        if(!firstBuild)
        {
            for(Point point: foundableSpots.values())
            {
                if(game.isValidSettlementPosition(point))
                {
                    Settlement freshSettlement = new Settlement(game);
                    freshSettlement.owner = this ;
                    freshSettlement.ownerNumber = designator ;
                    freshSettlement.beginNewSettlement(point);
                    setData.put(freshSettlement, new SettlementData(freshSettlement, game));
                    game.setSettlement(point, freshSettlement);
                    placeMeeple(point, freshSettlement);
                    firstBuild = true;
                    buildDecision = BuildOptions.FOUND_SETTLEMENT;
                    buildPoint = point;
                    return;
                }
            }

            return;
        }

        if(totoro != 0)
        {
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
                                    if(mySets.settlement.checkPieceAdjacencies(point) <= 1 && mySets.settlement.checkPieceAdjacencies(point) != 0)
                                    {
                                        point = new Point(row, column);
                                        break;
                                    }

                                    else if(game.isValidTotoroPosition(new Point(row, column), mySets.settlement))
                                        point = new Point(row, column);
                                }
                            }
                        }

                        if(game.isValidTotoroPosition(point, mySets.settlement))
                        {
                            buildDecision = BuildOptions.TOTORO_SANCTUARY;
                            buildPoint = point;
                            placeTotoro(point, mySets.settlement);
                            System.out.println("Totoro has been fucking placed motherfucker! Score: " + score);
                            return;
                        }
                    }
                }
            }
        }

        if(tigers != 0)
        {
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
                                if(mySets.settlement.checkPieceAdjacencies(point) <= 1 && mySets.settlement.checkPieceAdjacencies(point) != 0)
                                {
                                    point = new Point(row, column);
                                    break;
                                }

                                else if(game.isValidTigerPosition(new Point(row, column), mySets.settlement))
                                    point = new Point(row, column);
                            }
                        }
                    }

                    if(game.isValidTigerPosition(point, mySets.settlement))
                    {
                        System.out.println("Placed TIGER!");
                        buildDecision = BuildOptions.TIGER_PLAYGROUND;
                        buildPoint = point;
                        placeTiger(point, mySets.settlement);
                        return;
                    }
                }
            }
        }

        for (SettlePointPair mySets : playerSettlements.values())
        {
            setData.get(mySets.settlement).compileSettlementData();

            if (mySets.settlement.size + mySets.settlement.grasslands.size() > 1 && meeples > mySets.settlement.grasslands.size())
            {
                if(setData.get(mySets.settlement).afterGrass > meeples)
                {
                    continue;
                }

                else if(mySets.settlement.grassExpansions <= 0)
                    continue;

                else if(mySets.settlement.grasslands.size() > 0)
                {
                    buildDecision = BuildOptions.EXPAND;
                    buildPoint = mySets.settlement.findEndPoints();
                    terrainSelection = TerrainType.GRASS;
                    game.expandSettlement(buildPoint, expansionAction);
                    return;
                }
            }

            else if (mySets.settlement.size + mySets.settlement.lakes.size() > 1 && meeples > mySets.settlement.lakes.size())
            {
                if(setData.get(mySets.settlement).afterLake > meeples)
                    continue;

                else if(mySets.settlement.grassExpansions <= 0)
                    continue;

                else if(mySets.settlement.lakes.size() > 0)
                {
                    buildDecision = BuildOptions.EXPAND;
                    buildPoint = mySets.settlement.findEndPoints();
                    terrainSelection = TerrainType.LAKE;
                    game.expandSettlement(buildPoint, expansionAction);
                    return;
                }
            }

            else if (mySets.settlement.size + mySets.settlement.jungles.size() > 1 && meeples > mySets.settlement.jungles.size())
            {
                if(setData.get(mySets.settlement).afterJungle > meeples)
                    continue;

                else if(mySets.settlement.grassExpansions <= 0)
                    continue;

                else if(mySets.settlement.jungles.size() > 0)
                {
                    buildDecision = BuildOptions.EXPAND;
                    buildPoint = mySets.settlement.findEndPoints();
                    terrainSelection = TerrainType.JUNGLE;
                    game.expandSettlement(buildPoint, expansionAction);
                    return;
                }
            }

            else if (mySets.settlement.size + mySets.settlement.rocky.size() > 1 && meeples > mySets.settlement.rocky.size())
            {
                if(setData.get(mySets.settlement).afterRocky > meeples)
                    continue;

                else if(mySets.settlement.grassExpansions <= 0)
                    continue;

                else if(mySets.settlement.rocky.size() > 0)
                {
                    buildDecision = BuildOptions.EXPAND;
                    buildPoint = mySets.settlement.findEndPoints();
                    terrainSelection = TerrainType.ROCK;
                    game.expandSettlement(buildPoint, expansionAction);
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

        Point PlaceOneAway = placeMeepleOneAway(settlementChoice);

        if(PlaceOneAway != null && game.isValidSettlementPosition(PlaceOneAway))
        {
            Settlement freshSettlement = new Settlement(game);
            freshSettlement.owner = this ;
            freshSettlement.ownerNumber = designator ;
            freshSettlement.beginNewSettlement(PlaceOneAway);
            setData.put(freshSettlement, new SettlementData(freshSettlement, game));
            game.setSettlement(PlaceOneAway, freshSettlement);
            placeMeeple(PlaceOneAway, freshSettlement);
            buildDecision = BuildOptions.FOUND_SETTLEMENT;
            buildPoint = PlaceOneAway;
            return;
        }

        for(Point point : foundableSpots.values())
        {
            if(game.isValidSettlementPosition(point))
            {
                Settlement freshSettlement = new Settlement(game);
                freshSettlement.owner = this ;
                freshSettlement.ownerNumber = designator ;
                freshSettlement.beginNewSettlement(point);
                setData.put(freshSettlement, new SettlementData(freshSettlement, game));
                game.setSettlement(point, freshSettlement);
                placeMeeple(point, freshSettlement);
                buildDecision = BuildOptions.FOUND_SETTLEMENT;
                buildPoint = point;
                return;
            }
        }
    }

    public void updateFoundPositions()
    {
        for(int i = game.upperLimit - 2 ; i < game.lowerLimit+2; i++)
        {
            for(int j = game.leftLimit -2 ; j < game.rightLimit+2; j++)
            {
                if(game.isValidSettlementPosition(new Point(i, j)))
                    foundableSpots.put(coordinatesToKey(i, j), new Point(i, j));
                else if(foundableSpots.containsKey(coordinatesToKey(i, j)))
                    foundableSpots.remove(coordinatesToKey(i, j));
            }
        }
    }

    public Tile determineTilePlacementByAI()
    {
        Point selectedPoint;
        ProjectionPack projection;

        int mostMeeplesInHex = -1;
        Settlement settlementChoice = new Settlement(game);

        if(!firstPlay)
        {
            for(Point point : game.playableHexes.values())
            {
                for(int i = 1; i < 7; i++)
                {
                    tileHeld.setRotation(i);
                    projection = projectTilePlacement(tileHeld, point);
                    projection.projectedLevel = game.getProjectedHexLevel(projection);

                    if(game.isValidTilePlacement(projection) && projection.projectedLevel == 1)
                    {
                        game.setTile(tileHeld, projection);
                        tileHeld.serverPoint = projection.volcano;
                        firstPlay = true;
                        return tileHeld;
                    }
                }
            }
        }

        else
        {
            //priority list
            for(SettlePointPair mySet : playerSettlements.values())
            {
                if(mySet.settlement.totoroSanctuaries == 1)
                {
                    int nukingRotation = determineRotationForNukingAI(mySet.settlement);

                    if(nukingRotation != 0)
                    {
                        tileHeld.setRotation(determineRotationForNukingAI(mySet.settlement));
                        projection = projectTilePlacement(tileHeld, mySet.settlement.endPointToNuke);
                        projection.projectedLevel = game.getProjectedHexLevel(projection);

                        if(game.isValidTilePlacement(projection) && projection.projectedLevel == 1) {
                            game.setTile(tileHeld, projection);
                            tileHeld.serverPoint = projection.volcano;
                            return tileHeld;
                        }

                        else if(projection.projectedLevel > 1 && game.isValidOverlap(projection))
                        {
                            tileHeld.serverPoint = projection.volcano;
                            game.setTile(tileHeld, projection);
                            return tileHeld;
                        }
                    }
                }
            }

            for(SettlePointPair mySet : playerSettlements.values())
            {
                if(mySet.settlement.tigerPlaygrounds == 1)
                {
                    int nukingRotation = determineRotationForNukingAI(mySet.settlement);

                    if(nukingRotation != 0)
                    {
                        tileHeld.setRotation(determineRotationForNukingAI(mySet.settlement));
                        projection = projectTilePlacement(tileHeld, mySet.settlement.endPointToNuke);
                        projection.projectedLevel = game.getProjectedHexLevel(projection);

                        if(game.isValidTilePlacement(projection) && projection.projectedLevel == 1) {
                            game.setTile(tileHeld, projection);
                            tileHeld.serverPoint = projection.volcano;
                            return tileHeld;
                        }

                        else if(projection.projectedLevel > 1 && game.isValidOverlap(projection))
                        {
                            tileHeld.serverPoint = projection.volcano;
                            game.setTile(tileHeld, projection);
                            return tileHeld;
                        }
                    }
                }
            }

            for(SettlePointPair mySet : playerSettlements.values())
            {
                //check if I can place a tile that will add to one of the adjacency lists to make expansion better
                //and away from volcanoes
                if(determineTilePlacementForExpansion(mySet, tileHeld.hexA.terrain))
                    return tileHeld;

                else if(determineTilePlacementForExpansion(mySet, tileHeld.hexB.terrain))
                    return tileHeld;
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

            if(determineTilePlacementForPlacingAI(settlementChoice) != null)
            {
                projection = projectTilePlacement(tileHeld, determineTilePlacementForPlacingAI(settlementChoice));
                projection.projectedLevel = game.getProjectedHexLevel(projection);

                if(game.isValidTilePlacement(projection) && projection.projectedLevel == 1)
                {
                    game.setTile(tileHeld, projection);
                    tileHeld.serverPoint = projection.volcano;
                    return tileHeld;
                }

                if(projection.projectedLevel > 1 && game.isValidOverlap(projection))
                {
                    tileHeld.serverPoint = projection.volcano;
                    game.setTile(tileHeld, projection);
                    return tileHeld;
                }
            }

            else
            {
                for(Point point : game.playableHexes.values())
                {
                    for(int i = 1; i < 7; i++)
                    {
                        tileHeld.setRotation(i);
                        projection = projectTilePlacement(tileHeld, point);
                        projection.projectedLevel = game.getProjectedHexLevel(projection);

                        if(game.isValidTilePlacement(projection) && projection.projectedLevel == 1)
                        {
                            game.setTile(tileHeld, projection);
                            tileHeld.serverPoint = projection.volcano;
                            return tileHeld;
                        }

                        else if(projection.projectedLevel > 1 && game.isValidOverlap(projection))
                        {
                            tileHeld.serverPoint = projection.volcano;
                            game.setTile(tileHeld, projection);
                            return tileHeld;
                        }
                    }
                }
            }
        }

        System.out.println("AI Tile rotation: " + tileHeld.rotation);
        return tileHeld;
    }

    public boolean determineTilePlacementForExpansion(SettlePointPair mySet, TerrainType terrain)
    {
        ProjectionPack projection = null;

        switch(terrain)
        {
            case JUNGLE:
                if(mySet.settlement.jungles.size() > 1)
                {
                    for(Point point : mySet.settlement.occupantPositions.values())
                    {
                        for(int i = 1; i < 7; i++)
                        {
                            tileHeld.setRotation(i);
                            projection = projectTilePlacement(tileHeld, mySet.settlement.endPointToNuke);
                            projection.projectedLevel = game.getProjectedHexLevel(projection);

                            if(projection != null) {
                                if (game.isValidTilePlacement(projection) && projection.projectedLevel == 1) {
                                    tileHeld.serverPoint = projection.volcano;
                                    game.setTile(tileHeld, projection);
                                    return true;
                                } else if (projection.projectedLevel > 1 && game.isValidOverlap(projection)) {
                                    tileHeld.serverPoint = projection.volcano;
                                    game.setTile(tileHeld, projection);
                                    return true;
                                }
                            }
                        }
                    }
                }
                return false;

            case LAKE:
                if(mySet.settlement.jungles.size() > 1)
                {
                    for(Point point : mySet.settlement.occupantPositions.values())
                    {
                        for(int i = 1; i < 7; i++)
                        {
                            tileHeld.setRotation(i);
                            projection = projectTilePlacement(tileHeld, mySet.settlement.endPointToNuke);
                            projection.projectedLevel = game.getProjectedHexLevel(projection);

                            if(projection != null) {
                                if (game.isValidTilePlacement(projection) && projection.projectedLevel == 1) {
                                    tileHeld.serverPoint = projection.volcano;
                                    game.setTile(tileHeld, projection);
                                    return true;
                                } else if (projection.projectedLevel > 1 && game.isValidOverlap(projection)) {
                                    tileHeld.serverPoint = projection.volcano;
                                    game.setTile(tileHeld, projection);
                                    return true;
                                }
                            }
                        }
                    }
                }
                return false;

            case GRASS:
                if(mySet.settlement.jungles.size() > 1)
                {
                    for(Point point : mySet.settlement.occupantPositions.values())
                    {
                        for(int i = 1; i < 7; i++)
                        {
                            tileHeld.setRotation(i);
                            projection = projectTilePlacement(tileHeld, mySet.settlement.endPointToNuke);
                            projection.projectedLevel = game.getProjectedHexLevel(projection);

                            if(projection != null)
                            {
                                if(game.isValidTilePlacement(projection) && projection.projectedLevel == 1)
                                {
                                    tileHeld.serverPoint = projection.volcano;
                                    game.setTile(tileHeld, projection);
                                    return true;
                                }

                                else if(projection.projectedLevel > 1 && game.isValidOverlap(projection))
                                {
                                    tileHeld.serverPoint = projection.volcano;
                                    game.setTile(tileHeld, projection);
                                    return true;
                                }
                            }
                        }
                    }
                }
                return false;

            case ROCK:
                if(mySet.settlement.jungles.size() > 1)
                {
                    for(Point point : mySet.settlement.occupantPositions.values())
                    {
                        for(int i = 1; i < 7; i++)
                        {
                            tileHeld.setRotation(i);
                            projection = projectTilePlacement(tileHeld, mySet.settlement.endPointToNuke);
                            projection.projectedLevel = game.getProjectedHexLevel(projection);

                            if(projection != null) {
                                if (game.isValidTilePlacement(projection) && projection.projectedLevel == 1) {
                                    tileHeld.serverPoint = projection.volcano;
                                    game.setTile(tileHeld, projection);
                                    return true;
                                } else if (projection.projectedLevel > 1 && game.isValidOverlap(projection)) {
                                    tileHeld.serverPoint = projection.volcano;
                                    game.setTile(tileHeld, projection);
                                    return true;
                                }
                            }
                        }
                    }
                }
                return false;
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

                        if(game.isValidTilePlacement(projection) && projection.projectedLevel == 1)
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

                        else if(projection.projectedLevel > 1 && game.isValidOverlap(projection))
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

    private void getMySettlements(){
        for(SettlePointPair pair: playerSettlements.values()){
            uniqueSettlements.put(pair.settlement, pair.point);
        }

        for(Point point : uniqueSettlements.values()){
            setData.put(game.board[point.row][point.column].settlementPointer, new SettlementData(game.board[point.row][point.column].settlementPointer, game));
            //mySettlementData.add(new SettlementData(game.board[point.row][point.column].settlementPointer, game));
        }

        uniqueSettlements.clear();
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
                row = myPoint.row + rowOneAway[i];
                column = myPoint.column + columnOneAway[i];

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

        else
        {
            for(SettlePointPair mySets : playerSettlements.values())
            {
                if(mySets.settlement.totoroSanctuaries != 0)
                    continue;

                else if(mySets.settlement.size >= 5) //do we also have to check if there's a hex that we can put on?
                {
                    for(Point point : mySets.settlement.occupantPositions.values())
                    {
                        for(int i = 0; i < SIDES_IN_HEX; i++)
                        {
                            if(game.isValidTotoroPosition(new Point(point.row + ROW_ADDS[i], point.column + COLUMN_ADDS[i]), mySets.settlement))
                            {
                                return false;
                            }
                        }
                    }
                }
            }

            for(SettlePointPair mySets : playerSettlements.values())
            {
                if(mySets.settlement.tigerPlaygrounds != 0)
                    continue;

                for(Point point : mySets.settlement.occupantPositions.values())
                {
                    for(int i = 0; i < SIDES_IN_HEX; i++)
                    {
                        if(game.isValidTigerPosition(new Point(point.row + ROW_ADDS[i], point.column + COLUMN_ADDS[i]), mySets.settlement))
                        {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

}
