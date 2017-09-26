package com.example.ServerSockets.Server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
/*
public class ConnectionHandler extends Thread {

    Socket serviceSocket;
    DataInputStream input;
    BufferedReader in;
    PrintWriter out;
    String userInput;
    ArrayList<String> chatLog;
    int clientnr;
    Server server;

    public ConnectionHandler(Server server, Socket socket){
        this.serviceSocket = socket;
        this.server = server;
    }

    public void HandleConnection(){
        try {
            in = new BufferedReader(new InputStreamReader(serviceSocket.getInputStream()));
            out = new PrintWriter(serviceSocket.getOutputStream(), true);

            for (int i = 0; i < 10; i++){
                server.
            }

            System.out.println("Listening for input...");
            while ((userInput = in.readLine()) != null){
                if (userInput.contains("/who")){
                    out.println("Your number is " + clientnr);
                    continue;
                }
                if (userInput.contains("/log")){
                    out.println("--BEGIN--");
                    for (String message:chatLog) {
                        out.println(message);
                    }
                    out.println("--END--");
                    continue;
                }

                for (int i = 0; i < 10; i++){
                    if (threads[i] != null & threads[i] != this){
                        String messageText = "Client " + clientnr + " says: " + userInput;
                        System.out.println("Client: " + clientnr + " is sending: '"+ userInput + "' to output...");
                        chatLog.add(messageText);
                        threads[i].out.println(messageText);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Override
    public void run() {
        HandleConnection();
    }
}
*/