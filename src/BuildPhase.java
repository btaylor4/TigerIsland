/**
 * Created by Adam_Soliman on 3/14/2017.
 *
 * Player has placed Tile and must now select a build action
 * once build action is executed then player ends turn
 */
public class BuildPhase implements PlayerState {

    Player player;

    public BuildPhase (Player player){
        this.player = player;
    }
    @Override
    public Tile drawTile() {
        System.err.println("Cannot Draw Tile during build phase");
        return null;
    }

    @Override
    public void placeTile() {
        System.err.println("Tile already placed");
    }

    @Override
    public void foundNewSettlement() {
        player.decrementMeeple(1);
        System.out.println("Settlement Founded");
        player.setCurrentPlayerState(player.getEndTurnState());
    }

    @Override
    public void expandExistingSettlement() {
        //method may change since <1 meeples can be placed at once
        player.decrementMeeple(1);
        System.out.println("Settlement Expanded");
        player.setCurrentPlayerState(player.getEndTurnState());
    }

    @Override
    public void buildTotoroSanctuary() {
        player.decrementTotoro();
        System.out.println("Totoro Placed");
        player.setCurrentPlayerState(player.getEndTurnState());
    }
}
