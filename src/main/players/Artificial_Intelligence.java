package main.players;
import main.*;
import main.enums.OccupantType;
import main.enums.TerrainType;
import main.utils.SettlePointPair;

import static main.utils.constants.*;
import static main.utils.formulas.coordinatesToKey;


/**
 * Created by Rahul on 4/7/2017.
 */
public class Artificial_Intelligence extends Player
{
    private Point point;
    private Settlement settle;
    private int row, column, rotation;
    //TESTING GIT ISH

    public Artificial_Intelligence(GameBoard gamePointer, int designator)
    {
        super(gamePointer, designator);
    }

    public void Place_Tile_By_AI()
    {
        ProjectionPack place;
        Point point;
        Settlement set = new Settlement(game);
        drawTile();

        if(designator == 1)
        {
            point = new Point(107,103); //Put the values to anything random if you are first
            tileHeld.setRotation(4);
            place = projectTilePlacement(tileHeld, point);
            place.projectedLevel = game.getProjectedHexLevel(place);

            if(game.isValidTilePlacement(place))
            {
                game.setTile(tileHeld,place);
                return;
            }
        }

        else
        {
            place = projectTilePlacement(tileHeld, AI_tile_placement(set));
            place.projectedLevel = game.getProjectedHexLevel(place);

            if(game.isValidTilePlacement(place))
            {
                game.setTile(tileHeld, place);
                return;
            }
        }
    }

    public Point AI_tile_placement(Settlement settle)
    {
        Point volcano = null;
        ProjectionPack place;

        int volcano_row;
        int volcano_column;

        for(int i = 0; i < SIDES_IN_HEX; i++)
        {
            volcano_row = point.row + ROW_ADDS[i];
            volcano_column = point.column + COLUMN_ADDS[i];

            if(game.board[volcano_row][volcano_column] == null)
            {
                for(int j = 1; j < 7; j++)
                {
                    tileHeld.setRotation(j);
                    place = projectTilePlacement(tileHeld, new Point(volcano_row, volcano_column));
                    place.projectedLevel = game.getProjectedHexLevel(place);

                    if(game.isValidTilePlacement(place))
                    {
                        volcano = new Point(volcano_row, volcano_column);
                        tileHeld.setRotation(j);
                        place = projectTilePlacement(tileHeld, new Point(volcano_row, volcano_column));
                        place.projectedLevel = game.getProjectedHexLevel(place);
                        return volcano;
                    }
                }
            }
        }

        return volcano;
    }

    public void AI_Build_Options()
    {
        Settlement sets = new Settlement(game);
        Point new_point;

        for(SettlePointPair settlements : playerSettlements.values())
        {
            if(settlements.settlement.totoroSanctuaries == 0)
            {
                if(settlements.settlement.size >= 5)
                {
                    for(int i = 0; i < SIDES_IN_HEX; i++)
                    {
                        row = point.row + ROW_ADDS[i];
                        column = point.column + COLUMN_ADDS[i];

                        if(game.isValidTotoroPosition(point, settlements.settlement ))
                        {
                            point = new Point(row, column);
                        }
                    }

                    if(game.isValidTotoroPosition(point, settlements.settlement ))
                    {
                        placeTotoro(point, settlements.settlement);
                    }
                }
            }
        }

        for(SettlePointPair settlements : playerSettlements.values())
        {
            if(settlements.settlement.tigerPlaygrounds == 0)
            {
                for(int i = 0; i < SIDES_IN_HEX; i++)
                {
                    row = point.row + ROW_ADDS[i];
                    column = point.column + COLUMN_ADDS[i];

                    if(game.isValidTigerPosition(point, settlements.settlement ))
                    {
                        point = new Point(row, column);
                    }
                }

                if(game.isValidTigerPosition(point, settlements.settlement ))
                {
                    placeTotoro(point, settlements.settlement);
                }
            }
        }

        for(Point point : sets.occupantPositions.values())
        {
            for(int i = 0; i < SIDES_IN_HEX; i++)
            {
                row = point.row + ROW_ADDS[i];
                column = point.column + COLUMN_ADDS[i];

                if(game.isValidSettlementPosition(new Point(row,column)))
                {
                    point = new Point(row, column);
                }
            }

            if(game.isValidSettlementPosition(point))
            {
                new_point = point;
                Settlement newSettlement = new Settlement(game);
                newSettlement.beginNewSettlement(new_point);
                game.setSettlement(new_point, newSettlement);
                placeMeeple(new_point, newSettlement);
            }
        }
    }

    public void FindSettlement()
    {
        point = new Point(row, column);
        settle = new Settlement(game);
        if(game.isValidSettlementPosition(point))
        {
            game.setPiece(point, OccupantType.MEEPLE, settle);
        }
    }

    public void AI_ExpandSettlement()
    {
        Settlement temp = new Settlement(game);
        point = new Point(row, column);
        for(SettlePointPair settlements : playerSettlements.values())
        {
            if(game.isValidSettlementPosition(point))
            {
                game.setPiece(point, OccupantType.MEEPLE, temp);
            }
        }
    }

    public void AI_PlaceTotoro()
    {
        point = new Point(row,column);
        if(settle.size > 5)             //NOT SURE ABOUT THIS.
        {
            if(game.isValidTotoroPosition(point,settle))
            {
                placeTotoro(point, selectedSettlement);
            }
        }
    }

    public void AI_PlaceTigerPlayground()
    {
        point = new Point(row, column);
    }

}
