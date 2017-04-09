package main.players;

import main.GameBoard;
import main.Player;
import main.Point;
import main.enums.TerrainType;
import main.enums.BuildOptions;

import java.util.Scanner;

public class HumanPlayer extends Player {


    public HumanPlayer(GameBoard gamePointer, int designator){
        super(gamePointer, designator);
    }

    @Override
    protected void determineTilePlacement(){
        // for human player placement determined by prompts

        do {
            tilePlacement = determineTilePositionByHuman();
            tileProjection = projectTilePlacement(tileHeld, tilePlacement);
            tileProjection.projectedLevel = game.getProjectedHexLevel(tileProjection) ;
        } while(!game.isValidTilePlacement(tileProjection));

    }

    public Point determineTilePositionByHuman(){
        int row, column, rotation ;

        System.out.print("Volcano row choice: ");
        row = getOptionFromTerminal();

        System.out.print("Volcano column choice: ");
        column = getOptionFromTerminal();

        System.out.print("Rotation choice 1 - 6: ");
        rotation = getOptionFromTerminal();

        tileHeld.setRotation(rotation);

        return new Point(row, column) ;
    }

    @Override
    protected void determineBuildOption(){
        // for human player placement determined by prompts
        determineBuildOptionByHuman();
    }

    private void determineBuildOptionByHuman(){
        System.out.print("Build options 1: new  2: expand  3: Totoro  4: Tiger  -- ");
        int buildOption = getOptionFromTerminal();

        switch(buildOption) {
            case 1:
                buildDecision = BuildOptions.FOUND_SETTLEMENT ;
                determineSettlementPositionHuman();
                System.out.println("Successfully Founded");
                break;

            case 2:
                buildDecision = BuildOptions.EXPAND ;
                determineSettlementExpansionHuman();
                break;

            case 3:
                buildDecision = BuildOptions.TOTORO_SANCTUARY ;
                determineTotoroPlacement();
                break;

            case 4:
                buildDecision = BuildOptions.TIGER_PLAYGROUND ;
                determineTigerPlaygroundPlacement();
                break;

            default:
                System.out.println("Invalid choice");
        }
    }

    public Point determinePiecePositionByHuman(){
        int row, column ;

        System.out.print("Piece row choice: ");
        row = getOptionFromTerminal();

        System.out.print("Piece col choice: ");
        column = getOptionFromTerminal();

        return (new Point(row, column));
    }

    public void determineSettlementPositionHuman(){
        do{
            buildPoint = determinePiecePositionByHuman();
        } while(!game.isValidSettlementPosition(buildPoint)) ;
    }

    public void determineSettlementExpansionHuman(){
        buildPoint = determineSettlementByHuman();
        terrainSelection = determineTerrainByHuman();
    }

    public int getOptionFromTerminal() {
        Scanner input = new Scanner(System.in);
        return input.nextInt();
    }

    private void determineTotoroPlacement(){
        Point finder ;

        do{
            finder = determineSettlementByHuman();
            selectedSettlement = game.board[finder.row][finder.column].settlementPointer ;
            buildPoint = determinePiecePositionByHuman() ;
        } while(!game.isValidTotoroPosition(buildPoint, selectedSettlement));
    }

    private void determineTigerPlaygroundPlacement(){
        Point finder ;

        do{
            finder = determineSettlementByHuman();
            selectedSettlement = game.board[finder.row][finder.column].settlementPointer ;
            buildPoint = determinePiecePositionByHuman() ;
        } while(!game.isValidTigerPosition(buildPoint, selectedSettlement));
    }

    private Point determineSettlementByHuman() {
        System.out.print("Row of existing settlement: ");
        int settlementRow = getOptionFromTerminal();

        System.out.print("Column of existing settlement: ");
        int settlementColumn = getOptionFromTerminal();

        return new Point(settlementRow,settlementColumn);
    }

    private TerrainType determineTerrainByHuman() {
        System.out.println("What terrain type to expand on: j l g r");
        Scanner input = new Scanner(System.in);
        String terrains = input.next();

        switch (terrains.charAt(0)){
            case 'g':
                return TerrainType.GRASSLANDS;

            case 'r':
                return  TerrainType.ROCKY;

            case 'l':
            case 'w':
                return TerrainType.LAKE;

            case 'j':
            case 'f':
                return TerrainType.JUNGLE;
        }

        return TerrainType.VOLCANO;
    }

}
