import java.util.HashMap;

/**
 * Created by Bryan on 3/20/17.
 */
public class Settlement
{
    private HashMap<Point, Integer> collectionOfPoints;

    public Settlement()
    {
        collectionOfPoints = new HashMap<>();
    }

    public void createNewSettlement(Point point)
    {
        collectionOfPoints.put(point, 1);
    }

    public boolean checkExistingSettlement(Point point)
    {
        if(collectionOfPoints.containsKey(point))
            return false;
        else
            return true;
    }
}
