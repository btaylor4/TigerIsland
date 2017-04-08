package main;

public class TigerIsland {

    public static void main(String [] args) {
        Thread game1 = new Thread(new GameThread());
        game1.start();
        //Thread game2 = new Thread(new GameThread());
    }
}
