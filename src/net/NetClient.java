package net;

import jdk.internal.org.objectweb.asm.Handle;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class NetClient {

    private String IP;
    public int Port = 6969; //Port of the process you want to communicate with

    public String UserName = "admin";
    public String Password = "password1";


    private Socket socket;
    private NetServerMsg msg;
    private Scanner reader;
    private PrintStream output;

    public NetClient() throws IOException
    {
        IP = InetAddress.getLocalHost().getHostAddress();
        Initialize();
    }

    public NetClient(int port) throws IOException
    {
        IP = InetAddress.getLocalHost().getHostAddress();
        Port = port;
        Initialize();
    }

    public NetClient(String ip, int port) throws IOException
    {
        IP = ip;
        Port = port;
        Initialize();
        Start();
    }

    private void Initialize() throws IOException
    {
        System.out.println("connecting to: " + IP + ":" + Port);
        socket = new Socket(IP, Port);
        msg = new NetServerMsg();
    }

    public void Start() throws IOException {
        reader = new Scanner(socket.getInputStream());
    }

    public void Listen() throws IOException {
        reader = new Scanner(socket.getInputStream());

        String message = "";
        while ((message = reader.nextLine()) != null)
        {
            if(!message.isEmpty()) {
                HandleMessage(message);
            }
        }
    }

    public NetServerMsg getNextMessageFromServer() throws IOException {
        String message ;
        if ((message = reader.nextLine()) != null) {
            if(!message.isEmpty()) {
                HandleMessage(message);
                return msg;
            }
        }
        return null;
    }

    public void Send(String message) throws IOException {
        output = new PrintStream(socket.getOutputStream(), true);
        output.println(message);
    }

    public boolean IsConnected()
    {
        return socket.isConnected();
    }

    private void HandleMessage(String message) {
        msg.ParseLine(message);

        System.out.println(message);
    }

    public NetServerMsg GetCurrentMessage()
    {
        return msg;
    }
}