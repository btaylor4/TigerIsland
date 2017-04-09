package main;

public class TigerIsland {

    public static void main(String [] args) {

        //client will be started here and will send responses to corresponding game
        //GameThread will need access to client in order to send moves out

        Thread game1 = new Thread(new GameThread(1,true));
        game1.start();
        //Thread game2 = new Thread(new GameThread(2,true));
        //game2.start();
    }
}
