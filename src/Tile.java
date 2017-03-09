/**
 * Created by Bryan on 3/8/17.
 */
import java.util.ArrayList;
import java.util.List;

public class Tile
{
    private int level;
    private Hexagon topHex;
    private Hexagon leftHex;
    private Hexagon rightHex;

    public Tile(Terrain topHex, Terrain leftHex, Terrain rightHex)
    {
        level = 1;
        this.topHex = new Hexagon(topHex);
        this.leftHex = new Hexagon(leftHex);
        this.rightHex = new Hexagon(rightHex);

        addLeftAndRightDependencies(this.leftHex, this.rightHex);
        addTopAndRightDependencies(this.topHex, this.rightHex);
        addTopAndLeftEdgeDependencies(this.topHex, this.leftHex);
    }

    public void addTopAndLeftEdgeDependencies(Hexagon topHex, Hexagon leftHex)
    {
        Terrain[] topEdges = topHex.getEdgeTypes();
        Terrain[] leftEdges = leftHex.getEdgeTypes();

        topEdges[4] = leftEdges[1];
        leftEdges[1] = topEdges[4];
        topHex.setEdgeTypes(topEdges);
        leftHex.setEdgeTypes(leftEdges);
    }

    public void addLeftAndRightDependencies(Hexagon leftHex, Hexagon rightHex)
    {
        Terrain[] rightEdges = rightHex.getEdgeTypes();
        Terrain[] leftEdges = leftHex.getEdgeTypes();

        rightEdges[4] = leftEdges[1];
        leftEdges[1] = rightEdges[4];
        rightHex.setEdgeTypes(rightEdges);
        leftHex.setEdgeTypes(leftEdges);
    }

    public void addTopAndRightDependencies(Hexagon topHex, Hexagon rightHex)
    {
        Terrain[] topEdges = topHex.getEdgeTypes();
        Terrain[] rightEdges = rightHex.getEdgeTypes();

        topEdges[4] = rightEdges[1];
        rightEdges[1] = topEdges[4];
        topHex.setEdgeTypes(topEdges);
        leftHex.setEdgeTypes(rightEdges);
    }
}
