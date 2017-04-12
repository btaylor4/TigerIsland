package main.utils;

public class constants {
    public static final int ARRAY_DIMENSION = 210;
    public static final int BOARD_CENTER = ARRAY_DIMENSION/2 ;

    public static final int NUM_TILE_COPIES = 3;
    public static final int NUM_TILES = 48;
    public static final int SIDES_IN_HEX = 6;

    public static final int ROW_ADDS[] = {0, -1, -1, 0, 1, 1};
    public static final int COLUMN_ADDS[] = {-1, 0, 1, 1, 0, -1};

    public static final int rowOneAway[] = {2, 1, 0, -1, -2, -2, -2, -1, 0, 1};
    public static final int columnOneAway[] = {-2, -2, -2, -1, 0, 1, 2, 2, 2, 1};

    public static final int TRAVERSE_ROW_ADDS[] = {-1, 0, 1, 1, 0, -1};
    public static final int TRAVERSE_COLUMN_ADDS[] = {1, 1, 0, -1, -1, 0};

}
