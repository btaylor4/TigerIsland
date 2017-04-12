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
                hexPoint.row -= 1;
                if(horizontal == Direction.RIGHT){
                    hexPoint.column += 1 ;
                }
                break;

            case DOWN:
                hexPoint.row += 1;
                if(horizontal == Direction.LEFT){
                    hexPoint.column -= 1 ;
                }
                break;

            case NONE:
                if(horizontal == Direction.LEFT){
                    hexPoint.column -= 1;
                }
                else if(horizontal == Direction.RIGHT){
                    hexPoint.column += 1;
                }

            default:
                break;
        }
    }
}
