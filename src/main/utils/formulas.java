package main.utils;

public class formulas {

    public static int coordinatesToKey(int row, int column){
        return (row * constants.ARRAY_DIMENSION) + column ;
    }

}
