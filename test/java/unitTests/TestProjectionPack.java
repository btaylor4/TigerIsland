package unitTests;
import main.ProjectionPack;
import main.Point;
import main.enums.Direction;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by Bryan on 4/6/17.
 */
public class TestProjectionPack
{
    ProjectionPack projections;
    Point point;

    @Before
    public void initializeProjections()
    {
        projections = new ProjectionPack(new Point(0,0));
        point = new Point(0, 0);
    }

    @Test
    public void testCreationOfProjectionPack()
    {
        assertNotNull(projections);
        assertTrue("Projections are projections", projections instanceof ProjectionPack);
    }

    /*@Test
    public void testValidProjectionPoint()
    {
        int row = point.row;
        int column = point.column;

        for(Direction vertical : Direction.values())
        {
            for(Direction horizontal: Direction.values())
            {
                projections.projectPoint(point, vertical, horizontal);
                switch(vertical)
                {
                    case UP:
                        if(horizontal == Direction.NONE || point.column == 0)
                        {
                            assertEquals(point.row, row - 1);
                            row -= 1;
                        }
                        break;

                    case DOWN:
                        if(horizontal == Direction.NONE || point.column != 0)
                        {
                            assertEquals(point.row, row + 1);
                            row += 1;
                        }
                        break;
                }

                switch(horizontal)
                {
                    case LEFT:
                        assertEquals(point.column, column - 1);
                        column -= 1;
                        break;

                    case RIGHT:
                        assertEquals(point.column, column + 1);
                        column += 1;
                        break;
                }
            }
        }
    }*/

    @Test
    public void testInvalidPorjectionPoint()
    {
        int row = point.row;
        int column = point.column;

        for(Direction vertical : Direction.values())
        {
            for(Direction horizontal: Direction.values())
            {
                projections.projectPoint(point, vertical, horizontal);
                switch(vertical)
                {
                    case UP:
                        if(horizontal == Direction.NONE || point.column == 0)
                        {
                            assertNotEquals(point.row, row + 1);
                            row -= 1;
                        }
                        break;

                    case DOWN:
                        if(horizontal == Direction.NONE || point.column != 0)
                        {
                            assertNotEquals(point.row, row - 1);
                            row += 1;
                        }
                        break;
                }

                switch(horizontal)
                {
                    case LEFT:
                        assertNotEquals(point.column, column + 1);
                        column -= 1;
                        break;

                    case RIGHT:
                        assertNotEquals(point.column, column - 1);
                        column += 1;
                        break;
                }
            }
        }
    }
}
