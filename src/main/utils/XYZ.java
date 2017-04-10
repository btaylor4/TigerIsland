package main.utils;

import main.Point;

import static main.utils.constants.BOARD_CENTER;

public class XYZ {
    public int x ;
    public int y ;
    public int z ;

    public XYZ(){
        x = 0;
        y = 0;
        z = 0;
    }

    public XYZ(Point twoDim){
        this.z = twoDim.row - BOARD_CENTER ;
        this.x = twoDim.column - BOARD_CENTER ;
        this.y = -x-z ;
    }

    public XYZ(int x, int y, int z){
        this.x = x ;
        this.y = y ;
        this.z = z ;
    }

    public void setFromBoard(Point twoDim){
        this.z = twoDim.row - BOARD_CENTER ;
        this.x = twoDim.column - BOARD_CENTER ;
        this.y = -x-z ;
    }

    public Point get2DTranslation(){
        return new Point(z + BOARD_CENTER, x + BOARD_CENTER) ;
    }
}
