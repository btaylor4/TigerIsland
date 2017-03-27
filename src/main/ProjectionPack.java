package main;

import main.enums.Direction;


public class ProjectionPack {

    public Point volcano;
    public Point hex_a;
    public Point hex_b;
    public int projectedLevel ;

    public ProjectionPack(Point point){
        volcano = new Point(point.row, point.column);
        hex_a = new Point(point.row, point.column);
        hex_b = new Point(point.row, point.column);
        projectedLevel = 0 ;
    }

    public void projectPoint(Point hexPoint, Direction vertical, Direction horizontal){
        switch(vertical){
            case UP:
                if(horizontal == Direction.NONE) {
                    hexPoint.row -= 1;
                }
                else if((hexPoint.column % 2) == 0){
                    hexPoint.row -= 1 ;
                }
                break;

            case DOWN:
                if(horizontal == Direction.NONE){
                    hexPoint.row += 1 ;
                }
                else if((hexPoint.column % 2) != 0){
                    hexPoint.row += 1 ;
                }
                break;

            default:
                break;
        }

        switch(horizontal) {
            case LEFT:
                hexPoint.column -= 1;
                break;

            case RIGHT:
                hexPoint.column += 1;
                break;

            default:
                break;
        }

        System.out.println(hexPoint.row + " " + hexPoint.column);
    }
}
