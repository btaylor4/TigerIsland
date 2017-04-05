/**
 * Created by zainlateef on 4/3/17.
 */
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.*;

public class client {

    //Tournament Configuration Values-----

    public String IP_Address="172.20.10.3"; //IP Address of the machine (server) you want to connect to
    public int port_number=1025; //Port of the process you want to communicate with
    public String tournament_password="Hello World";
    public String username="admin";
    public String password="password1";

    //-----------------------------------

    public Socket socket;
    public String message_from_server;
    public String[] split_by_space;
    public String our_pid,opponent_pid,cid;
    public int total_rounds,current_round;


    public client() throws IOException
    {
        socket=new Socket(IP_Address,port_number);
    }


    public void listening_to_server() throws IOException
    {
        Scanner reader=new Scanner(socket.getInputStream());
        String message=reader.nextLine();
        message_from_server=message;
        System.out.println("Client Recieved: "+message);
        split_by_space=message_from_server.split(" ");
        interpret(message_from_server);
    }

    public void send_to_server(String message) throws IOException
    {
        PrintStream send_this= new PrintStream(socket.getOutputStream());
        send_this.println(message);
    }

    public boolean socket_is_open()
    {
        if(socket.isClosed())
            return false;
        return true;
    }

    public void interpret(String message) throws IOException
    {
        if(message.contains("WELCOME TO ANOTHER EDITION OF THUNDERDOME!"))
            welcome_to();
        else if(message.contains("TWO SHALL ENTER, ONE SHALL LEAVE"))
            two_shall();
        else if(message.contains("WAIT FOR THE TOURNAMENT TO BEGIN"))
            wait_for();
        else if(message.contains("NEW CHALLENGE"))
            new_challenge();
        else if(message.contains("BEGIN ROUND"))
            begin_round();
        else if(message.contains("NEW MATCH BEGINNING NOW YOUR OPPONENT IS PLAYER"))
            new_match();
        else if(message.contains("MAKE YOUR MOVE IN GAME"))
            make_your();
        else if(message.contains("PLACED"))
            placed(message);
        else if(message.contains("FORFEITED")||message.contains("LOST")||message.contains("OVER"))
            end_of_round();
        else if(message.contains("THANK YOU FOR PLAYING"))
            socket.close();
    }

    public void welcome_to() throws IOException
    {
        String message="ENTER THUNDERDOME "+password;
        send_to_server(message);
    }

    public void two_shall() throws IOException
    {
        String message="I AM "+username+" "+password;
        send_to_server(message);
    }

    public void wait_for()
    {
        our_pid=split_by_space[6];
        System.out.println("Clients PID set to:"+our_pid);
    }

    public void new_challenge()
    {
        cid=split_by_space[2];
        total_rounds= Integer.parseInt(split_by_space[6]);
        System.out.println("Clients CID set to:"+cid);
        System.out.println("Clients total_rounds set to:"+total_rounds);
    }

    public void begin_round()
    {
        current_round= Integer.parseInt(split_by_space[2]);
        System.out.println("Clients current_round set to:"+current_round);
       // AI.reset_board(); TODO: AI needs to reset the game at the beginning of each round
    }

    public void new_match()
    {
        opponent_pid=split_by_space[8];
        System.out.println("Clients opponent_pid set to:"+opponent_pid);
    }

    public void make_your()
    {
        String game_id=split_by_space[5];
        int time=Integer.parseInt(split_by_space[7]);
        String tile=split_by_space[12];
        System.out.println("AI will make a move on game "+game_id+" with tile "+tile+" in "+time+" seconds");
       //AI.make_move(game_id,tile,time); TODO: AI recives this information and sends its move back to the server

    }

    public void placed(String message)
    {
        //TODO: Make sure integers can handle negative numbers
        int x=0,y=0,z=0,x_build=0,y_build=0,z_build=0;
        String gid=split_by_space[1];
        String message_pid=split_by_space[5];
        String build_type="none";
        String expansion_terrain="none";
        String orientation="none";

        if(Objects.equals(message_pid, opponent_pid))
        {
            String game_id=split_by_space[1];
            String tile=split_by_space[7];
            x=Integer.parseInt(split_by_space[9]);
            y=Integer.parseInt(split_by_space[10]);
            z=Integer.parseInt(split_by_space[11]);
            orientation=split_by_space[12];

            if(message.contains("FOUNDED"))
            {
                build_type="found";
                x_build=Integer.parseInt(split_by_space[16]);
                y_build=Integer.parseInt(split_by_space[17]);
                z_build=Integer.parseInt(split_by_space[18]);
            }
            else if(message.contains("EXPANDED"))
            {
                build_type = "expand";
                x_build=Integer.parseInt(split_by_space[16]);
                y_build=Integer.parseInt(split_by_space[17]);
                z_build=Integer.parseInt(split_by_space[18]);
                expansion_terrain=split_by_space[19];
            }
            else if(message.contains("TOTORO SANCTUARY"))
            {
                build_type = "totoro";
                x_build=Integer.parseInt(split_by_space[17]);
                y_build=Integer.parseInt(split_by_space[18]);
                z_build=Integer.parseInt(split_by_space[19]);
            }
            else if(message.contains("TIGER PLAYGROUND"))
            {
                build_type = "tiger";
                x_build=Integer.parseInt(split_by_space[17]);
                y_build=Integer.parseInt(split_by_space[18]);
                z_build=Integer.parseInt(split_by_space[19]);
            }

            //AI.opponents_move(game_id,tile,x,y,z,orientation,build_type,x_build,y_build,z_build,expansion_terrain)
            System.out.println("AI will register opponents move on game "+game_id+" with tile "+tile+" at xyz coordinates "+x+" "+y+" "+z+" with build type "+build_type+" and build coordinates xyz of "+x_build+" "+y_build+" "+z_build+" orientation: "+orientation+" with an expansion terrain of "+expansion_terrain);

        }

    }

    public void end_of_round()
    {
       System.out.println("Round has ended");
        //share relevant end of game details
    }
};
