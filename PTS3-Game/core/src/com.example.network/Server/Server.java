package com.example.network.Server;

import com.game.classes.Game;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Server {

    private ServerSocket serverSocket;
    private ArrayList<ConnectionHandler> clients;
    private ServerManager serverManager;

    public Server(int port) throws IOException{
        serverSocket= new ServerSocket(port);
        clients = new ArrayList<ConnectionHandler>();
        serverManager = new ServerManager(this);
    }

    public void start(){
        serverManager.start();
    }

    public void stop(){
        serverManager.acceptingClients = false;
        for (ConnectionHandler client :clients) {
            client.close();
        }
    }

    public void sendMessageAll(ConnectionHandler from, String message){
        for (ConnectionHandler client:clients) {
            if (from != client){
                client.sendMessageA(message);
            }
        }
    }

    public void sendMessageWhisper(String from, String to, String message){
        for (ConnectionHandler client: clients){
            if (client.getClientInfo().getName().equals(to)){
                client.sendMessageA(from + " whispers to you: " + message);
            }
        }
    }

    public void sendGameState(Game game){
        //TODO
    }

    /*
 * The ServerManager waits for a client to connect and opens a connection
 * with that client on a separate thread. It does this by creating a
 * ConnectionHandler
  * */

    private class ServerManager extends Thread {
        //private ConnectionHandler[] threads;
        private Server server;
        private boolean acceptingClients = true;

        public ServerManager(Server server){
            this.server = server;
        }

        @Override
        public void run(){

            System.out.println("Listening for clients...");
            try {
                while (acceptingClients){

                    Socket serviceSocket = serverSocket.accept();

                    System.out.println("Client found! Connecting...");
                    ConnectionHandler handler = new ConnectionHandler(server, serviceSocket);
                    server.clients.add(handler);
                    handler.start();
                }

            } catch (Exception e) {
                System.out.println("Error starting server.");
                e.printStackTrace();
            } finally {

            }
        }
    }

    private class ConnectionHandler extends Thread {

        private Socket serviceSocket;
        private DataInputStream in;
        private DataOutputStream out;
        private Server server;
        private boolean receivingMessages = true;
        private ClientInfo clientInfo = new ClientInfo("?");

        public ClientInfo getClientInfo() {
            return clientInfo;
        }

        public ConnectionHandler(Server server, Socket socket){
            this.serviceSocket = socket;
            this.server = server;
        }

        public void HandleConnection(){
            try {
                in = new DataInputStream(serviceSocket.getInputStream());
                out = new DataOutputStream(serviceSocket.getOutputStream());

                System.out.println("Connection successful!");

                while (!serviceSocket.isClosed() && receivingMessages){

                    int messageType = in.readByte();

                    handleMessage(messageType, in);
                }
            } catch (Exception e) {
                System.out.println("Connection reset, closing connection with " + this.getClientInfo().getName());
                this.close();
            } finally {

            }
        }

        private void handleMessage(int messageType, DataInputStream in) throws IOException {

            String message;

            switch (messageType){
                case 1: // SEND ALL
                    message = "Message A from: " + clientInfo.getName() + ": " + in.readUTF();
                    sendMessageAll(this, message);
                    break;
                case 2: // SEND WHISPER
                    String to = in.readUTF();
                    String whisper = in.readUTF();

                    sendMessageWhisper(clientInfo.getName(), to, whisper);
                    break;
                case 3: // SET NAME
                    String previousName = clientInfo.getName();
                    clientInfo.setName(in.readUTF());
                    System.out.println("Name set to: " + clientInfo.getName() + ", was " + previousName);
                    break;
                case 4: // START GAME
                    //TODO
                    break;
                case 5: // END TURN
                    //TODO
                    break;
                default:
                    System.out.println("I DON'T KNOW");
            }
        }

        private void sendMessageA(String message){
            try {
                out.writeByte(1);
                out.writeUTF(message);
                out.flush();
            } catch (Exception e){
                System.out.println("Error sending message A.");
                e.printStackTrace();
            }
        }

        private void close(){
            try {
                this.receivingMessages = false;
                this.serviceSocket.close();
                server.clients.remove(this);
            } catch (Exception e){
                System.out.println("Error closing connection with client.");
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            HandleConnection();
        }
    }


}
