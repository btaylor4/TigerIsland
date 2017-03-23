import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Bryan on 3/21/17.
 */
public class TestProjectionPack
{
    @Test
    public void testCreationOfProjectionPack()
    {
        ProjectionPack projections = new ProjectionPack(50, 50);
        assertNotNull(projections);
    }

    @Test
    public void testProjectPoint()
    {
        int row = 50;
        int col = 50;

        for(Direction d : Direction.values())
        {
            ProjectionPack projections = new ProjectionPack(row, col);
            Point point = new Point(row, col);
            row = 50;
            col = 50;

            switch(d)
            {
                case DOWN:
                    projections.projectPoint(point, Direction.DOWN, Direction.NONE);
                    assertEquals(row + 1, point.row);
                    assertEquals(col, point.column);
                    break;

                case LEFT:
                    projections.projectPoint(point, Direction.NONE, Direction.LEFT);
                    assertEquals(row, point.row);
                    assertEquals(col - 1, point.column);
                    break;

                case NONE:
                    projections.projectPoint(point, Direction.NONE, Direction.NONE);
                    assertEquals(row, point.row);
                    assertEquals(col, point.column);
                    break;

                case RIGHT:
                    projections.projectPoint(point, Direction.NONE, Direction.RIGHT);
                    assertEquals(row, point.row);
                    assertEquals(col + 1, point.column);
                    break;

                case UP:
                    projections.projectPoint(point, Direction.UP, Direction.NONE);
                    assertEquals(row - 1, point.row);
                    assertEquals(col, point.column);
                    break;
            }
        }
    }
}
