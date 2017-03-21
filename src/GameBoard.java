import java.util.HashMap;
import java.util.Random;

/**
 * Created by jdavi on 3/10/17.
 */

public class GameBoard {

    private static final int ARRAY_DIMENSION = 210;
    private static final int NUM_TILE_COPIES = 3;
    private static final int NUM_TILES = 48;
    private static final int SIDES_IN_HEX = 6;

    private static final int EVEN_ROW_ADDS[] = {-1, -1, -1, 0, 1, 0};
    private static final int EVEN_COLUMN_ADDS[] = {-1, 0, 1, 1, 0, -1};
    private static final int ODD_ROW_ADDS[] = {0,-1,0,1,1,1};
    private static final int ODD_COLUMN_ADDS[] = {-1,0,1,1,0,-1};
    private static int tileChoice = 0;

    private int upperLimit, lowerLimit, leftLimit, rightLimit ;

    private static Hexagon[][] board = new Hexagon[ARRAY_DIMENSION][ARRAY_DIMENSION];
    public static Tile tileStack[] = new Tile[NUM_TILES];
    private HashMap playableHexes = new HashMap() ;

    public GameBoard(){
        upperLimit = lowerLimit = leftLimit = rightLimit = ARRAY_DIMENSION/2 - 1;
        createTiles();
        shuffleTiles();

        for(int i = 0; i < 210; i++)
        {
            for(int j = 0; j < 210; j++)
            {
                board[i][j] = new Hexagon(); //this may screw with some check that you(JP) may have already implemented
            }
        }
    }

    public void createTiles(){
        // the tiles, are three copies of a full permutation of the terrains
        int tileCounter = 0;

        for (TerrainType a : TerrainType.values() ) {
            if(a == TerrainType.VOLCANO) continue ;

            for (TerrainType b : TerrainType.values() ) {
                if(b == TerrainType.VOLCANO) continue ;

                for (int i = 0; i < NUM_TILE_COPIES; i++) {
                    tileStack[tileCounter] = new Tile();
                    tileStack[tileCounter].assignTileNumber(tileCounter + 1) ;
                    tileStack[tileCounter].assignTerrain(a, b) ;
                    tileCounter++;
                }
            }
        }
    }

    public void shuffleTiles(){
        Random r = new Random();
        int random_index ;
        Tile tileBeingSwapped ;

        for(int i = 0; i < NUM_TILES; i++){
            tileBeingSwapped = tileStack[i] ;
            random_index = r.nextInt(NUM_TILES) ;
            tileStack[i] = tileStack[random_index] ;
            tileStack[random_index] = tileBeingSwapped ;
        }
    }

    public ProjectionPack projectTilePlacement(Tile tileBeingPlaced, int row, int column){
        ProjectionPack projections = new ProjectionPack(row, column) ;

        switch(tileBeingPlaced.rotation){
            case 0:
                projections.projectPoint(projections.hex_a, Direction.UP, Direction.RIGHT);
                projections.projectPoint(projections.hex_b, Direction.DOWN, Direction.RIGHT);
                break;

            case 60:
                projections.projectPoint(projections.hex_a, Direction.DOWN, Direction.RIGHT);
                projections.projectPoint(projections.hex_b, Direction.DOWN, Direction.NONE);
                break;

            case 120:
                projections.projectPoint(projections.hex_a, Direction.DOWN, Direction.NONE);
                projections.projectPoint(projections.hex_b, Direction.DOWN, Direction.LEFT);
                break;

            case 180:
                projections.projectPoint(projections.hex_a, Direction.DOWN, Direction.LEFT);
                projections.projectPoint(projections.hex_b, Direction.UP, Direction.LEFT);
                break;

            case 240:
                projections.projectPoint(projections.hex_a, Direction.UP, Direction.LEFT);
                projections.projectPoint(projections.hex_b, Direction.UP, Direction.NONE);
                break;

            case 300:
                projections.projectPoint(projections.hex_a, Direction.UP, Direction.NONE);
                projections.projectPoint(projections.hex_b, Direction.UP, Direction.RIGHT);
                break;

            default:
                System.out.println("Something just fucked up");
                break;
        }

        return projections ;
    }

    public int coordinatesToKey(int row, int column){
        return (row * ARRAY_DIMENSION) + column ;
    }

    public boolean checkAdjacency(ProjectionPack projections){
        boolean volcanoAdjacent, hex_aAdjacent, hex_bAdjacent, somethingAdjacent ;

        // if one of the hexes fall in the freePlay list and nothing is underneath the tiles return true
        volcanoAdjacent = playableHexes.containsKey(coordinatesToKey(projections.volcano.row, projections.volcano.column));
        hex_aAdjacent = playableHexes.containsKey(coordinatesToKey(projections.hex_a.row, projections.hex_b.column));
        hex_bAdjacent = playableHexes.containsKey(coordinatesToKey(projections.hex_b.row, projections.hex_b.column));

        return (volcanoAdjacent || hex_aAdjacent || hex_bAdjacent) ;
    }

    public int getProjectedHexLevel(ProjectionPack projections){
        int volcanoLevel, hex_aLevel, hex_bLevel ;

        if(board[projections.volcano.row][projections.volcano.column] != null)
            volcanoLevel = board[projections.volcano.row][projections.volcano.column].level + 1;
        else{
            volcanoLevel = 1 ;
        }

        if(board[projections.hex_a.row][projections.hex_a.column] != null)
            hex_aLevel = board[projections.hex_a.row][projections.hex_b.column].level + 1 ;
        else{
            hex_aLevel = 1 ;
        }

        if(board[projections.hex_b.row][projections.hex_b.column] != null)
            hex_bLevel = board[projections.hex_b.row][projections.hex_b.column].level + 1;
        else {
            hex_bLevel = 1 ;
        }

        if((hex_aLevel == hex_bLevel) && (hex_aLevel == volcanoLevel))
            return hex_aLevel ;
        else
            return 0;
    }

    public void addFreeAdjacencies(Point point){
        // up-left, up, up-right, down-right, down, down-left,
        int rowAddArray[], columnAddArray[] ;
        int row , column ;

        if(point.column % 2 == 0){
            rowAddArray = EVEN_ROW_ADDS;
            columnAddArray = EVEN_COLUMN_ADDS;
        }
        else{
            rowAddArray = ODD_ROW_ADDS;
            columnAddArray = ODD_COLUMN_ADDS;
        }

        for (int i = 0; i < SIDES_IN_HEX; i++) {
            row = point.row + rowAddArray[i];
            column = point.column + columnAddArray[i];

            if (board[row][column] == null) {
                playableHexes.put(coordinatesToKey(row, column), 1);
            }
        }
    }

    public void removeFreeAdjacency(Point point){
        playableHexes.remove(coordinatesToKey(point.row, point.column));
    }

    public void placeTile(Tile tileBeingPlaced, ProjectionPack projections){
        tileBeingPlaced.setHexLevels(projections.projectedLevel) ;

        board[projections.volcano.row][projections.volcano.column] = tileBeingPlaced.volcano ;
        board[projections.hex_a.row][projections.hex_a.column] = tileBeingPlaced.hexA ;
        board[projections.hex_b.row][projections.hex_b.column] = tileBeingPlaced.hexB ;

        addFreeAdjacencies(projections.volcano);
        addFreeAdjacencies(projections.hex_a);
        addFreeAdjacencies(projections.hex_b);

        removeFreeAdjacency(projections.volcano);
        removeFreeAdjacency(projections.hex_a);
        removeFreeAdjacency(projections.hex_b);

        if(leftLimit > projections.volcano.column) leftLimit -= 2 ;
        if(rightLimit < projections.volcano.column) rightLimit += 2 ;
        if(upperLimit > projections.volcano.row) upperLimit -= 2 ;
        if(lowerLimit < projections.volcano.row) lowerLimit += 2 ;
    }

    public void setFirstTile(){
        placeTile(tileStack[0], projectTilePlacement(tileStack[0], upperLimit, leftLimit));
        tileChoice++;
    }

    public boolean selectTilePlacement(Tile tileBeingPlaced, int row, int column){
        ProjectionPack projects = projectTilePlacement(tileBeingPlaced, row, column);

        projects.projectedLevel = getProjectedHexLevel(projects);

        if (checkAdjacency(projects) && (projects.projectedLevel == 1)) {
            placeTile(tileBeingPlaced, projects);
            System.out.println("Tile placement OK") ;
            return true;
        }
        else{
            System.out.println("Invalid placement requested") ;
            return false;
        }
    }

    public void printBoard(){
        for(int i = upperLimit - 2 ; i < lowerLimit+2; i++){
            for(int j = leftLimit -2 ; j < rightLimit+2; j++){
                if(board[i][j] != null) {
                    System.out.print(board[i][j].terrain + "(" + i + "," + j +") ") ;
                }
            }
            System.out.print('\n');
        }
    }

    public Tile generateTile()
    {
        tileChoice++;
        return tileStack[++tileChoice];
    }

    public Tile[] getTileStack()
    {
        return tileStack;
    }

    public Hexagon getHex(Point point)
    {
        return board[point.row][point.column];
    }

    public void setPieceOnHex(Point point, OccupantType occupant)
    {
        board[point.row][point.column].setOccupantOnHex(occupant);
    }
}
