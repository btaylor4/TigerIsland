package main.states;

import main.Tile ;
import main.Point ;
/**
 * Created by Adam_Soliman on 3/14/2017.
 */
public interface PlayerState {

    Tile drawTile();

    void placeTile();

    void foundNewSettlement();

    void expandExistingSettlement();

    void buildTotoroSanctuary();
}