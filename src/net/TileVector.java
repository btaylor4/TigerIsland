package net;


import main.enums.TerrainType;

public class TileVector {
    private int x;
    private int y;
    private int z;
    private int orientation;
    private String terrain;
    public TileVector(int X, int Y, int Z, int Orientation)
    {
        x = X;
        y = Y;
        z = Z;
        orientation = Orientation;
    }
    public TileVector(int X, int Y, int Z, String Terrain)
    {
        x = X;
        y = Y;
        z = Z;
        orientation = -1;
        terrain = Terrain;
    }
    public TileVector(int X, int Y, int Z)
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
    public int GetOrientation() {return orientation;}
    public void SetOrientation(int Orientation)  { orientation = Orientation;}
    public String GetTerrain(){ return terrain;}
    public TerrainType GetTerrainType(){ return TerrainType.valueOf(terrain);}

    public void SetTerrain(String Terrain)  { terrain = Terrain;}
}