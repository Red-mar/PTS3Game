package network.Server;

import com.game.classes.Game;
import com.game.classes.Player;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Server {

    private ServerSocket serverSocket;
    private HashMap<Player, ConnectionHandler> clients;
    private ServerManager serverManager;

    public Server(int port) throws IOException{
        serverSocket= new ServerSocket(port);
        clients = new HashMap<Player, ConnectionHandler>();
        serverManager = new ServerManager(this);
    }

    public void start(){
        serverManager.start();
    }

    public void stop(){
        serverManager.acceptingClients = false;
        for (ConnectionHandler client :clients.values()) {
            client.close();
        }
    }

    public void sendMessageAll(ConnectionHandler from, String message){
        for (ConnectionHandler client:clients.values()) {
            client.sendMessage(MessageType.ChatMessage, message);
        }
    }

    public void sendMessageWhisper(String from, String to, String message){
        for (ConnectionHandler client: clients.values()){
            if (client.player.getName().equals(to)){
                client.sendMessage(MessageType.ChatMessage,from + " whispers to you: " + message);
            }
        }
    }

    public void sendGameMessagePlayers(){
        for (ConnectionHandler client: clients.values()) {
            client.sendGameMessagePlayers(MessageType.GameSendPlayersMessage);
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
        private Game game;
        private boolean acceptingClients = true;

        public ServerManager(Server server){
            this.server = server;
            game = new Game(server);
        }

        @Override
        public void run(){

            System.out.println("Listening for clients...");
            try {
                while (acceptingClients){

                    Socket serviceSocket = serverSocket.accept();

                    Player player = new Player("?");
                    System.out.println("Client found! Connecting...");
                    game.addPlayer(player);
                    ConnectionHandler handler = new ConnectionHandler(server, serviceSocket, player, game);
                    server.clients.put(player, handler);

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
        private Player player;
        private Game game;

        public ConnectionHandler(Server server, Socket socket, Player player, Game game){
            this.serviceSocket = socket;
            this.server = server;
            this.player = player;
            this.game = game;
        }

        public void HandleConnection(){
            try {
                in = new DataInputStream(serviceSocket.getInputStream());
                out = new DataOutputStream(serviceSocket.getOutputStream());

                System.out.println("Connection successful!");

                while (!serviceSocket.isClosed() && receivingMessages){
                    MessageType type = MessageType.values()[in.readByte()];
                    handleMessage(type, in);
                }
            } catch (Exception e) {
                System.out.println("Connection reset, closing connection with " + this.player.getName());
                this.close();
            } finally {

            }
        }

        private void handleMessage(MessageType type, DataInputStream in) throws IOException {

            String message;
            System.out.println("Received Message Type of:" + type.toString());

            switch (type){
                case TestMessage:
                    break;
                case ChatMessage: // SEND ALL
                    message = "Message A from: " + player.getName() + ": " + in.readUTF();
                    sendMessageAll(this, message);
                    break;
                case WhisperMessage: // SEND WHISPER
                    String to = in.readUTF();
                    String whisper = in.readUTF();
                    sendMessageWhisper(player.getName(), to, whisper);
                    break;
                case SetNameMessage: // SET NAME
                    String previousName = player.getName();
                    player.setName(in.readUTF());
                    System.out.println("Name set to: " + player.getName() + ", was " + previousName);
                    break;
                case GameSendPlayersMessage: // START GAME
                    Server.this.sendGameMessagePlayers();
                    break;
                default:
                    System.out.println("I DON'T KNOW");
                    break;
            }
        }

        private void sendMessage(MessageType type, String message){
            try {
                out.writeByte(type.ordinal());
                out.writeUTF(message);
                out.flush();
            } catch (Exception e){
                System.out.println("Error sending message");
                e.printStackTrace();
            }
        }

        private void sendGameMessagePlayers(MessageType type){
            try {
                out.writeByte(type.ordinal());
                ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(bOut);
                os.writeObject(game.getPlayers());
                out.write(bOut.toByteArray());
                out.flush();
            } catch (Exception e){
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
