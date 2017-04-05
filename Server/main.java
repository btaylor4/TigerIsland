/**
 * Created by zainlateef on 4/4/17.
 */
import java.io.*;
public class main {


    public static void main(String[] args)
    {
        client me = null;
        try {
            me=new client();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(me.socket_is_open())
        {
            try {
                me.listening_to_server();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
