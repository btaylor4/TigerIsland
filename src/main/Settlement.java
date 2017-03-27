package main;

import java.util.HashMap;

public class Settlement
{
    public int size ;
    public Player owner ;

    private HashMap<Point, Integer> collectionOfPoints;

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
}