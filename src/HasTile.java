/**
 * Created by Adam_Soliman on 3/14/2017.
 *
 * Player has received Tile and must now place Tile
 */
public class HasTile implements PlayerState {

    Player player;

    public HasTile(Player player){
        this.player = player;
    }

    @Override
    public Tile drawTile() {
        System.err.println("Already Drew Tile");
        return null;
    }

    @Override
    public void placeTile() {
        System.out.println("Tile Placed");
        player.setCurrentPlayerState(player.getBuildPhaseState());
    }

    @Override
    public void foundNewSettlement() {
        System.err.println("Must place Tile before founding Settlement");
    }

    @Override
    public void expandExistingSettlement() {
        System.err.println("Must place Tile before expanding Settlement");
    }

    @Override
    public void buildTotoroSanctuary() {
        System.err.println("Must place Tile before building Totoro");
    }
}
