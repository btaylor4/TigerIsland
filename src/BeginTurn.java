/**
 * Created by Adam_Soliman on 3/14/2017.
 *
 * Player has just started turn and does not have a Tile yet
 */
public class BeginTurn implements PlayerState{

    Player player;

    public BeginTurn(Player player){
        this.player = player;
    }

    @Override
    public Tile drawTile() {
        System.out.println("Drew Tile");
        player.setCurrentPlayerState(player.getHasTileState());
        return null;
    }

    @Override
    public void placeTile(Tile tileBeingPlaced, int row, int column) {
        System.err.println("No Tile to Place");
    }

    @Override
    public void foundNewSettlement() {
        System.err.println("Cannot found Settlement before placing Tile");
    }

    @Override
    public void expandExistingSettlement() {
        System.err.println("Cannot expand Settlement before placing Tile");
    }

    @Override
    public void buildTotoroSanctuary() {
        System.err.println("Cannot place Totoro before placing Tile");
    }
}
