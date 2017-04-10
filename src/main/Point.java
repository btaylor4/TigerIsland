package main;

import java.util.HashMap;

public class Point
{
    public int row ;
    public int column ;

    public Point(int row, int column){
        this.row = row ;
        this.column = column ;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Point))
            return false;
        if (obj == this)
            return true;

        Point rhs = (Point) obj;
        return (this.row == rhs.row) && (this.column == rhs.column);
    }

}