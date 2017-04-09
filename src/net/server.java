package net;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Created by zainlateef on 4/3/17.
 */
public class server {


    static ArrayList<String> test_statements = new ArrayList<>();

    public static void main(String args[]) throws IOException
    {
        populate_vector();
        String message_incoming;
        String message;

        ServerSocket s1 = new ServerSocket(1025);
        Socket socket = s1.accept();
        System.out.println("Server running @" + get_time() + "\n");

        Scanner input_from_client = new Scanner(socket.getInputStream());
        PrintStream send_to_client = new PrintStream(socket.getOutputStream(), true);


        for (int i = 0; i < test_statements.size(); i++)
        {
            message = test_statements.get(i);

            send_to_client.println(message);
            // read from client
            String clientMsg = input_from_client.nextLine();
            System.out.println(clientMsg);


        }

        input_from_client.close();
        send_to_client.close();
    }

    public static void populate_vector()
    {
        test_statements.add("WELCOME TO ANOTHER EDITION OF THUNDERDOME!");
        test_statements.add("TWO SHALL ENTER, ONE SHALL LEAVE");
        test_statements.add("WAIT FOR THE TOURNAMENT TO BEGIN Player1");
        test_statements.add("NEW CHALLENGE 20 YOU WILL PLAY 2 MATCHES");
        test_statements.add("BEGIN ROUND 1 OF 3");
        test_statements.add("NEW MATCH BEGINNING NOW YOUR OPPONENT IS PLAYER Player2");
        //test_statements.add("MAKE YOUR MOVE IN GAME B WITHIN 8 SECONDS: MOVE 3 PLACE ROCK+GRASS");
        //test_statements.add("GAME B MOVE 3 PLAYER Player2 PLACED GRASS+WATER AT 1 3 2 -4 FOUNDED SETTLEMENT AT 9 2 4");
        //test_statements.add("GAME A MOVE 4 PLAYER Player2 PLACED GRASS+WATER AT -1 6 2 8 EXPANDED SETTLEMENT AT 0 -5 4 GRASS");
        //test_statements.add("GAME B MOVE 3 PLAYER Player2 PLACED GRASS+WATER AT 1 -3 2 12 BUILD TOTORO SANCTUARY AT 9 2 4");
        //test_statements.add("GAME B MOVE 3 PLAYER Player2 PLACED GRASS+WATER AT 1 3 2 765 BUILD TIGER PLAYGROUND AT -9 2 4");
        //test_statements.add("GAME MOVE <#> PLAYER FORFEITED: TIMEOUT ");
       // test_statements.add("GAME MOVE <#> PLAYER LOST: UNABLE TO BUILD");
       // test_statements.add("GAME <gid> OVER PLAYER <pid> <score> PLAYER <pid> <score>");
        test_statements.add("THANK YOU FOR PLAYING!");
    }

    public static void delay(int milliseconds)
    {
        try {
            Thread.sleep(10);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public static String get_time()
    {
        String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        return timeStamp;
    }

}









