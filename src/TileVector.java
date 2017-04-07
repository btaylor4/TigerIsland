package net;

import com.sun.org.apache.xpath.internal.operations.Or;

public class TileVector {
    private int x;
    private int y;
    private int z;
    private int orientation;
    private String terrain;
    TileVector(int X, int Y, int Z, int Orientation)
    {
        x = X;
        y = Y;
        z = Z;
        orientation = Orientation;
    }
    TileVector(int X, int Y, int Z, String Terrain)
    {
        x = X;
        y = Y;
        z = Z;
        orientation = -1;
        terrain = Terrain;
    }
    TileVector(int X, int Y, int Z)
    {
        x = X;
        y = Y;
        z = Z;
        orientation = -1;
    }
    public int GetX(){ return x;};
    public int GetY(){ return y;};
    public int GetZ(){ return z;};

    public void SetX(int X)
    {
        x = X;
    }
    public void SetY(int Y)
    {
        y = Y;
    }
    public void SetZ(int Z)
    {
        z = Z;
    }
    public void SetOrientation(int Orientation)  { orientation = Orientation;}
    public void SetTerrain(String Terrain)  { terrain = Terrain;}
}
