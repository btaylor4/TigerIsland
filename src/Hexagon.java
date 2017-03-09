/**
 * Created by Bryan on 3/8/17.
 */
public class Hexagon
{
    private Terrain type;
    private int[] edges;
    private Terrain[] edgeType;
    private int meeplesOnHex;

    public Hexagon(Terrain type)
    {
        this.type = type;
        this.edges = new int[6];
        meeplesOnHex = 0;

        for(int i = 0; i < 6; i++)
        {
            this.edges[i] = 0;
            this.edgeType[i] = type;
        }
    }

    public int[] getEdges()
    {
        return edges;
    }

    public void setEdges(int[] edges)
    {
        this.edges = edges;
    }

    public Terrain[] getEdgeTypes()
    {
        return edgeType;
    }

    public void setEdgeTypes(Terrain[] edges)
    {
        this.edgeType = edges;
    }
}
