package main;

import net.*;

import java.io.IOException;

public class TigerIsland {

    public static void main(String [] args) {

        //client will be started here and will send responses to corresponding game
        //GameThread will need access to client in order to send moves out

        Thread game1 = new Thread(new GameThread(1,true));
        game1.start();

        try {
            //me=new client(InetAddress.getLocalHost().getHostAddress(), 1025);
            NetClient ntc = new NetClient(); // gets and sets default IP and PORT
            if(ntc.IsConnected())
            {
                ntc.Listen();

                NetClientMsg msg = new NetClientMsg();
                msg.FormatAuthenticationForTournament("CRAZY");
                msg.FormatAuthenticationPlayer("USER", "PASS");
            }
        } catch (IOException | NetClientMsg.ClientError e) {
            e.printStackTrace();
        }

        //Thread game2 = new Thread(new GameThread(2,true));
        //game2.start();
    }
}
