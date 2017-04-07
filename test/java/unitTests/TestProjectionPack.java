package unitTests;
import main.ProjectionPack;
import main.Point;
import main.enums.Direction;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by Bryan on 4/6/17.
 */
public class TestProjectionPack
{
    ProjectionPack testProjection;
    Point testPoint ;

    @Before
    public void initializeProjections(){
        testProjection = new ProjectionPack(new Point(0,0));
    }

    @Test
    public void testCreationOfProjectionPack(){
        assertNotNull(testProjection);
        assertTrue("Projections are projections", testProjection instanceof ProjectionPack);
    }

    @Test
    public void testCreationOfPoint(){
        testPoint = new Point(2, 2) ;
        assertNotNull(testPoint);
        assertTrue(testPoint.row == 2);
        assertTrue(testPoint.column == 2);
    }

    @Test
    public void testLeftProjection() {
        testPoint = new Point(10, 10);
        testProjection = new ProjectionPack(testPoint) ;
        testProjection.projectPoint(testPoint, Direction.UP, Direction.LEFT);

        assertTrue(testPoint.row == 9);
        assertTrue(testPoint.column == 10);
    }

    @Test
    public void testInvalidLeftProjection() {
        testPoint = new Point(10, 10);
        testProjection = new ProjectionPack(testPoint) ;
        testProjection.projectPoint(testPoint, Direction.UP, Direction.LEFT);

        assertFalse(testPoint.row != 9);
        assertFalse(testPoint.column != 10);
    }

    @Test
    public void testUpperRightProjection() {
        testPoint = new Point(10, 10);
        testProjection = new ProjectionPack(testPoint) ;
        testProjection.projectPoint(testPoint, Direction.UP, Direction.RIGHT);

        assertTrue(testPoint.row == 9);
        assertTrue(testPoint.column == 11);
    }

    @Test
    public void testInvalidUpperRightProjection() {
        testPoint = new Point(10, 10);
        testProjection = new ProjectionPack(testPoint) ;
        testProjection.projectPoint(testPoint, Direction.UP, Direction.RIGHT);

        assertFalse(testPoint.row != 9);
        assertFalse(testPoint.column != 11);
    }

    @Test
    public void testRightProjection() {
        testPoint = new Point(10, 10);
        testProjection = new ProjectionPack(testPoint) ;
        testProjection.projectPoint(testPoint, Direction.NONE, Direction.RIGHT);

        assertTrue(testPoint.row == 10);
        assertTrue(testPoint.column == 11);
    }

    @Test
    public void testInvalidRightProjection() {
        testPoint = new Point(10, 10);
        testProjection = new ProjectionPack(testPoint) ;
        testProjection.projectPoint(testPoint, Direction.NONE, Direction.RIGHT);

        assertFalse(testPoint.row != 10);
        assertFalse(testPoint.column != 11);
    }


    @Test
    public void testDownRightProjection() {
        testPoint = new Point(10, 10);
        testProjection = new ProjectionPack(testPoint) ;
        testProjection.projectPoint(testPoint, Direction.UP, Direction.LEFT);

        assertTrue(testPoint.row == 9);
        assertTrue(testPoint.column == 10);
    }

    @Test
    public void testInvalidDownRightProjection() {
        testPoint = new Point(10, 10);
        testProjection = new ProjectionPack(testPoint) ;
        testProjection.projectPoint(testPoint, Direction.UP, Direction.LEFT);

        assertFalse(testPoint.row != 9);
        assertFalse(testPoint.column != 10);
    }
    @Test
    public void testDownLeftProjection() {
        testPoint = new Point(10, 10);
        testProjection = new ProjectionPack(testPoint) ;
        testProjection.projectPoint(testPoint, Direction.UP, Direction.LEFT);

        assertTrue(testPoint.row == 9);
        assertTrue(testPoint.column == 10);
    }

    @Test
    public void testInvalidDownLeftProjection() {
        testPoint = new Point(10, 10);
        testProjection = new ProjectionPack(testPoint) ;
        testProjection.projectPoint(testPoint, Direction.UP, Direction.LEFT);

        assertFalse(testPoint.row != 9);
        assertFalse(testPoint.column != 10);
    }
}
