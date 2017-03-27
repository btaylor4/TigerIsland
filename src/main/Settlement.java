package main;

import main.enums.TerrainType;

import java.util.HashMap;

public class Settlement
{
    public int size ;
    public Player owner ;

    private HashMap<Point, Integer> collectionOfPoints;
    private HashMap<TerrainType, Point> grasslands;
    private HashMap<TerrainType, Point> volcanos;
    private HashMap<TerrainType, Point> lakes;
    private HashMap<TerrainType, Point> forests;
    private HashMap<TerrainType, Point> rocky;
    private HashMap<TerrainType, Point> deserts;

    public Settlement() {
        size = 0 ;
        owner = null ;
        collectionOfPoints = new HashMap<>();
    }

    public void createNewSettlement(Point point)
    {
        collectionOfPoints.put(point, 1);
    }

    public boolean checkExistingSettlement(Point point){
        if(collectionOfPoints.containsKey(point))
            return false;
        else
            return true;
    }

    public void checkForAdjacencies(Point piecePlacementPoint, Hexagon[][] board)
    {
        int i = piecePlacementPoint.row;
        int j = piecePlacementPoint.column;

        Point point = null;

        if(i != 0 && board[i-1][j] != null)
        {
            point = new Point(i-1, j);
            addTerrainAdjacencies(board[i-1][j].terrain, point);
        }

        if(i != 209 && board[i+1][j] != null)
        {
            point = new Point(i+1, j);
            addTerrainAdjacencies(board[i+1][j].terrain, point);
        }

        if(j != 0 && board[i][j-1] != null)
        {
            point = new Point(i, j-1);
            addTerrainAdjacencies(board[i][j-1].terrain, point);
        }

        if(j != 209 && board[i][j+1] != null)
        {
            point = new Point(i, j+1);
            addTerrainAdjacencies(board[i][j+1].terrain, point);
        }

        if(i != 0 && board[j][i] != null)
        {
            point = new Point(j, i);
            addTerrainAdjacencies(board[j][i].terrain, point);
        }

        if(i != 209 && board[j][i+1] != null)
        {
            point = new Point(j, i+1);
            addTerrainAdjacencies(board[j][i+1].terrain, point);
        }
    }

    public void addTerrainAdjacencies(TerrainType terrain, Point point)
    {
        switch(terrain)
        {
            case FOREST:
                if(!forests.containsValue(point))
                    forests.put(terrain, point);
                break;

            case GRASS:
                if(!grasslands.containsValue(point))
                    grasslands.put(terrain, point);
                break;

            case VOLCANO:
                if(!volcanos.containsKey(point))
                    volcanos.put(terrain, point);
                break;

            case ROCKY:
                if(!rocky.containsValue(point))
                    rocky.put(terrain, point);
                break;

            case WATER:
                if(!lakes.containsValue(point))
                    lakes.put(terrain, point);
                break;

            case DESERT:
                if(!deserts.containsKey(point))
                    deserts.put(terrain, point);
                break;
        }
    }
}