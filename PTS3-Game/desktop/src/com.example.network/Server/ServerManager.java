package com.example.ServerSockets.Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/*
 * The ServerManager waits for a client to connect and opens a connection
 * with that client on a separate thread. It does this by creating a
 * ConnectionHandler which at the moment can use a list of all the
 * connected clients, the chat log etc.
  *

public class ServerManager implements Runnable {

    private int portNumber;
    private ServerSocket myService;
    private ConnectionHandler[] threads;
    private ArrayList<String> chatLog;

    private boolean acceptingClients = true;

    Socket serviceSocket = null;

    public ServerManager(int portnumber){
        threads = new ConnectionHandler[10];
        this.portNumber = portnumber;

        chatLog = new ArrayList<>();
    }

    @Override
    public void run(){
        try {
            myService = new ServerSocket(portNumber);

            int i = 0;
            while (acceptingClients){
                Socket serviceSocket = null;
                serviceSocket = myService.accept();
                System.out.println("Creating new Thread...");
                if (threads[i] == null){
                    threads[i] = new ConnectionHandler(serviceSocket, threads, chatLog);
                    new Thread(threads[i]).start();
                }
                i++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }
}*/
