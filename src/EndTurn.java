/**
 * Created by Adam_Soliman on 3/16/2017.
 */
public class EndTurn implements PlayerState {

    Player player;

    public EndTurn(Player player){this.player = player;}

    @Override
    public Tile drawTile() {
        System.err.println("Not players turn");
        return null;
    }

    @Override
    public void placeTile(Tile tileBeingPlaced, int row, int column) {
        System.err.println("Not players turn");
    }

    @Override
    public void foundNewSettlement() {
        System.err.println("Not players turn");
    }

    @Override
    public void expandExistingSettlement() {
        System.err.println("Not players turn");
    }

    @Override
    public void buildTotoroSanctuary() {
        System.err.println("Not players turn");
    }
}
