package network.Server;

import com.game.classes.Game;
import com.game.classes.Player;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Server {

    private ServerSocket serverSocket;
    private HashMap<ConnectionHandler, Player> clients;
    private ServerManager serverManager;

    public Server(int port) throws IOException{
        serverSocket= new ServerSocket(port);
        clients = new HashMap<ConnectionHandler, Player>();
        serverManager = new ServerManager(this);
    }

    public void start(){
        serverManager.start();
    }

    public void stop(){
        serverManager.acceptingClients = false;
        for (ConnectionHandler client :clients.keySet()) {
            client.close();
        }
    }

    /**
     * Sends a chat message to all clients
     * @param from Who the message is from
     * @param message The message
     */
    public void sendMessageAll(ConnectionHandler from, String message){
        for (ConnectionHandler client:clients.keySet()) {
            client.sendMessage(MessageType.ChatMessage, message);
        }
    }

    /**
     * Sends a message to a single player
     * @param from Who the message is from
     * @param to Who the message is for
     * @param message The message
     */
    public void sendMessageWhisper(String from, String to, String message){
        for (ConnectionHandler client: clients.keySet()){
            if (client.player.getName().equals(to)){
                client.sendMessage(MessageType.ChatMessage,from + " whispers to you: " + message);
            }
        }
    }

    /**
     * Updates the players for all clients
     */
    public void sendGameMessagePlayers(){
        for (ConnectionHandler client: clients.keySet()) {
            client.sendObjectMessage(MessageType.GameSendPlayersMessage, serverManager.game.getPlayers());
        }
    }

    public void sendGameState(Game game){
        //TODO
    }

    /**
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
                    if (game.getPlayers().size() >= 4){
                        player.setSpectator(true);
                    }
                    game.addPlayer(player);
                    ConnectionHandler handler = new ConnectionHandler(server, serviceSocket, player, game);
                    server.clients.put(handler, player);

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

                server.serverManager.game.removePlayer(player.getName());
                server.clients.remove(this);
                System.out.println("Connection reset, closing connection with " + this.player.getName());
                this.close();
            } finally {

            }
        }

        private void handleMessage(MessageType type, DataInputStream in) throws IOException {

            String message;
            System.out.println("Received Message Type of:" + type.toString());

            byte[] buffer = new byte[5000];

            switch (type){
                case TestMessage:
                    break;
                case ChatMessage: /** Send a chat message to everyone **/
                    message = player.getName() + ": " + in.readUTF();
                    sendMessageAll(this, message);
                    break;
                case WhisperMessage: /** Send message to one person **/
                    String to = in.readUTF();
                    String whisper = in.readUTF();
                    sendMessageWhisper(player.getName(), to, whisper);
                    break;
                case SetNameMessage: /** Sets name of player **/
                    String previousName = player.getName();
                    player.setName(in.readUTF());
                    System.out.println("Name set to: " + player.getName() + ", was " + previousName);
                    for (Player player:server.serverManager.game.getPlayers()) {
                        if (player == this.player){
                            player.setName(this.player.getName());
                        }
                    }
                    break;
                case GameSendPlayersMessage: /** Sends player list to all clients **/
                    Server.this.sendGameMessagePlayers();
                    break;
                case GameReadyMessage: /** Changes the ready state of the sender **/
                    Player thisPlayer = server.clients.get(this);
                    if (!thisPlayer.isReady()){
                        thisPlayer.setReady(true);
                    } else {
                        thisPlayer.setReady(false);
                    }
                    Server.this.sendGameMessagePlayers();
                    break;
                case ClientSendPlayerMessage: /** Receives an updated player from the client **/
                    try {
                        in.read(buffer);
                        System.out.println(buffer.length);
                        ByteArrayInputStream bIn = new ByteArrayInputStream(buffer);
                        ObjectInputStream is = new ObjectInputStream(bIn);
                        Player newPlayer = (Player) is.readObject();

                        Iterator<Player> iterator = game.getPlayers().iterator();
                        while (iterator.hasNext()){
                            Player oldPlayer= iterator.next();
                            if (oldPlayer.getName().equals(newPlayer.getName())){
                                game.getPlayers().set(game.getPlayers().indexOf(oldPlayer), newPlayer);
                            }
                        }

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                default: /** I DON'T KNOW **/
                    System.out.println("I DON'T KNOW");
                    break;
            }
        }

        /**
         * Send a String
         * @param type The type of message (see enum)
         * @param message The message as string
         */
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

        /**
         * Sends an object to all clients
         * @param type The type of message (make sure it supports the object)
         * @param object The object to send
         */
        private void sendObjectMessage(MessageType type, Object object){
            try {
                out.writeByte(type.ordinal());
                ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(bOut);
                os.writeObject(object);
                out.write(bOut.toByteArray());
                out.flush();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        /**
         * Kills everything
         */
        private void close(){
            try {
                this.receivingMessages = false;
                this.serviceSocket.close();
                server.clients.remove(this);
                game.removePlayer(server.clients.get(this));
                Server.this.sendGameMessagePlayers();
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
