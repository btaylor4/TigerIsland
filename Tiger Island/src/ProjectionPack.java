/**
 * Created by jdavi on 3/13/17.
 */
public class ProjectionPack {

    public class Point{
        public int row ;
        public int column ;

        public Point(int row, int column){
            this.row = row ;
            this.column = column ;
        }
    }

    public Point volcano;
    public Point hex_a;
    public Point hex_b;

    public int projectedLevel ;

    public ProjectionPack(int row, int column){
        volcano = new Point(row, column);
        hex_a = new Point(row, column);
        hex_b = new Point(row, column);
    }

    public void projectPoint(Point hexPoint, Direction vertical, Direction horizontal){
        switch(vertical){
            case UP:
                if(horizontal == Direction.NONE) {
                    hexPoint.row -= 1;
                    return ;
                }
                else if(hexPoint.column % 2 == 0){
                    hexPoint.row -= 1 ;
                }
                break;

            case DOWN:
                if(horizontal == Direction.NONE){
                    hexPoint.row += 1 ;
                    return ;
                }
                else if(hexPoint.column % 2 != 0){
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

    }
}
