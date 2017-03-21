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
    private int buildOption;
    private boolean isFinished;
    private GameBoard game;
    HashMap<Settlement, Integer> mySettlements;

    public B_Player(GameBoard game)
    {
        score = 0;
        meeples = 20;
        tortoro = 3;
        mySettlements = new HashMap<>();
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

        System.out.println("Row choice");
        rowChoice = chooseOption();

        System.out.println("Col choice");
        colChoice = chooseOption();

        System.out.println("Rotation choice");
        rotationChoice = chooseOption();

        tile.setRotation(rotationChoice);

        ProjectionPack tileProjection = new ProjectionPack(rowChoice, colChoice);

        while(!game.selectTilePlacement(tile, rowChoice, colChoice))
        {
            game.placeTile(tile, tileProjection);
        }

        return isFinished;
    }

    public int chooseOption()
    {
        Scanner input = new Scanner(System.in);
        return input.nextInt();
    }

    public void placeMeeple()
    {
        meeples--;
    }

    public void placeTortoro()
    {
        //should decrease Tortoro if player does it validly
    }

    public boolean foundNewSettlement()
    {
        //should use place meeple method

        boolean isHexAVolcano = false;

        System.out.println("Row choice");
        rowChoice = chooseOption();

        System.out.println("Col choice");
        colChoice = chooseOption();

        Point point = new Point(rowChoice, colChoice);
        Hexagon hex = game.getHex(point);

        if(hex.getTerrain() == TerrainType.VOLCANO)
        {
            System.out.println("Placing on Volcano");
            return false;
        }

        else if(hex.getLevel() != 1)
        {
            System.out.println("Placing on level not 1");
            return false;
        }

        else if(mySettlements.isEmpty())
        {
            Settlement settlement = new Settlement();
            settlement.createNewSettlement(point);
            mySettlements.put(settlement, 1);
            return true;
        }

        else
        {
            for(Settlement sets: mySettlements.keySet())
            {
                if(sets.checkExistingSettlement(point))
                    return false;
            }
        }

        return true;
    }

    public void expandNewSettlement() {
        //should use place meeple method
    }

    public void build()
    {
        boolean validMove = false;

        System.out.println("Build options 1-3");
        buildOption = chooseOption();

        switch(buildOption)
        {
            case 1:
                validMove = foundNewSettlement();

                while(!validMove)
                {
                    validMove = foundNewSettlement();
                }

                System.out.println("Successfully Founded");
                placeMeeple();
                break;

            case 2:
                expandNewSettlement();
                break;

            case 3:
                placeTortoro();
                break;

            default:
                System.out.println("Invalid choice");
        }
    }
}
