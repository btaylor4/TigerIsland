/**
 * Created by Adam_Soliman on 3/14/2017.
 */
public interface PlayerState {

    Tile drawTile();

    void placeTile(Tile tileBeingPlaced, int row, int column);

    void foundNewSettlement();

    void expandExistingSettlement();

    void buildTotoroSanctuary();
}
