package main.utils;

import main.Point;
import main.Settlement;

public class SettlePointPair {
    public Settlement settlement;
    public Point point ;

    public SettlePointPair(Settlement settlement, Point point){
        this.settlement = settlement;
        this.point = point ;
    }
}
