package main;

import main.enums.OccupantType;

import java.util.HashMap;

import static main.utils.constants.COLUMN_ADDS;
import static main.utils.constants.ROW_ADDS;
import static main.utils.constants.SIDES_IN_HEX;

/**
 * Created by Bryan on 4/7/17.
 */
public class AdjacentMeeples
{
    public HashMap<Point, Integer> endPoints;
    public GameBoard game;
    public Point endPointToNuke;

    public AdjacentMeeples(GameBoard game)
    {
        this.game = game;
        endPoints = new HashMap<>();
        endPointToNuke = new Point(0, 0);
    }

    public void updateAdjacencies(Point point)
    {
        if(endPoints.containsKey(point))
        {
            endPoints.put(point, endPoints.get(point) + 1);
        }

        else
            endPoints.put(point, 1);
    }

    public int checkPieceAdjacencies(Point point)
    {
        int row = 0;
        int column = 0;
        int numberOfAdjacentPieces = 0;

        for (int i = 0; i < SIDES_IN_HEX; i++)
        {
            row = point.row + ROW_ADDS[i];
            column = point.column + COLUMN_ADDS[i];

            if (game.board[row][column] != null)
            {
                if(game.board[row][column].occupant != OccupantType.NONE)
                {
                    numberOfAdjacentPieces++;
                }
            }
        }
        return numberOfAdjacentPieces;
    }

    public void setEndPointToNuke(Point point)
    {
        endPointToNuke = point;
    }
}
