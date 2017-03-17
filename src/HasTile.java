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
        return player.getTileInHand();
    }

    @Override
    public void placeTile(Tile tileBeingPlaced, int row, int column) {

        if(player.getGame().selectTilePlacement(tileBeingPlaced, row, column)){
            System.out.println("Tile Successfully Placed");
            player.setCurrentPlayerState(player.getBuildPhaseState());
        }
        else{
            System.out.println("Tile Not Placed");
        }
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
