/**
 * Created by Bryan on 3/8/17.
 */
import java.util.HashMap;
import java.util.Scanner;

public class B_Player
{
    private int score;
    private int meeples;
    private int tortoro;
    private int rowChoice;
    private int colChoice;
    private int rotationChoice;
    private boolean isFinished;
    private GameBoard game;
    //HashMap<Settlement, Integer> mySettlements = new HashMap<>();

    public B_Player(GameBoard game)
    {
        score = 0;
        meeples = 20;
        tortoro = 3;
        isFinished = false;
        this.game = game;
    }

    public int getScore(){return score;}
    public int getMeeples(){return meeples;}
    public int getTortoro(){return tortoro;}

    public boolean placeTileToPlayOn()
    {
        //should decrease number of tiles if player places validly
        Tile tile = game.generateTile();
        rowChoice = chooseTile();
        colChoice = chooseTile();
        rotationChoice = chooseTile();
        tile.setRotation(rotationChoice);

        ProjectionPack tileProjection = new ProjectionPack(rowChoice, colChoice);

        while(!game.selectTilePlacement(tile, rowChoice, colChoice))
        {
            game.placeTile(tile, tileProjection);
        }

        return isFinished;
    }

    public int chooseTile()
    {
        Scanner input = new Scanner(System.in);
        return input.nextInt();
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

    public void expandNewSettlement() {
        //should use place meeple method
    }
}
