/**
 * Created by Bryan on 3/8/17.
 */
public class Player
{
    private int score;
    private int meeples;
    private int tortoro;

    public Player()
    {
        score = 0;
        meeples = 20;
        tortoro = 3;
    }

    public int getScore(){return score;}
    public int getMeeples(){return meeples;}
    public int getTortoro(){return tortoro;}

    public void placeTile()
    {
        //should decrease number of tiles if player places validly
    }

    public void placeMeeple()
    {
        //should decrease Meeple if player does it validly
    }

    public void placeTortoro()
    {
        //should decrease Tortoro if player does it validly
    }

    public void foundNewSettlement()
    {
        //should use place meeple method
    }

    public void expandNewSettlement()
    {
        //should use place meeple method
    }
}
