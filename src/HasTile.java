/**
 * Created by Adam_Soliman on 3/14/2017.
 */
public class HasTile implements PlayerState {

    Player player;

    public HasTile(Player player){
        this.player = player;
    }

    @Override
    public Tile drawTile() {
        return null;
    }

    @Override
    public void placeTile() {

    }

    @Override
    public void placeMeeple() {

    }

    @Override
    public void placeTortoro() {

    }

    @Override
    public void foundNewSettlement() {

    }

    @Override
    public void expandExistingSettlement() {

    }
}
