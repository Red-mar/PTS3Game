package com.game.classes.network.Server;

import com.game.classes.Character;
import com.game.classes.Game;
import com.game.classes.Map;
import com.game.classes.Player;
import com.game.classes.network.Client.Client;
import com.game.classes.network.MessageType;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Server {

    private ServerSocket serverSocket;
    private HashMap<ConnectionHandler, Player> clients;
    private ServerManager serverManager;
    private RMIServer rmiServer;

    public Server(int port) throws IOException{
        serverSocket= new ServerSocket(port);
        clients = new HashMap<ConnectionHandler, Player>();
        serverManager = new ServerManager(this);

        rmiServer = new RMIServer();
    }

    public void start(){
        serverManager.start();
    }

    public void stop(){
        for (ConnectionHandler connectionHandler : clients.keySet()) {
            connectionHandler.close();
        }
        clients = new HashMap<ConnectionHandler, Player>();
        serverManager.game.setPlayers(new ArrayList<Player>());
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
        ArrayList<Player> players = serverManager.game.getPlayers();

        for (ConnectionHandler client: clients.keySet()) {
            client.sendObjectMessage(MessageType.GameSendPlayersMessage, players);
            System.out.println(players.size());
        }
    }

    /**
     * Starts the game for all clients
     */
    public void sendGameStart(){
        for (ConnectionHandler client : clients.keySet()) {
            client.sendMessage(MessageType.GameStartMessage);
        }
    }

    /**
     * Ends the game for all clients
     */
    public void sendGameEnd(){
        for (ConnectionHandler client : clients.keySet()) {
            client.sendMessage(MessageType.GameEndMessage);
        }
    }

    /**
     * Sends a character movement update to all clients.
     * @param x
     * @param y
     * @param charName
     * @param playerName
     */
    public void sendCharacter(int x, int y, String charName, String playerName){
        for (ConnectionHandler client : clients.keySet()) {
            client.sendCharacterMessage(MessageType.GameCharacterMoveMessage, x, y, charName, playerName);
        }
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
                    if (game.getPlayers().size() >= 2){
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
                    handleMessages();
                }
            } catch (Exception e) {
                e.printStackTrace();
                server.serverManager.game.removePlayer(player.getName());
                server.clients.remove(this);
                System.out.println("Connection reset, closing connection with " + this.player.getName());
                this.close();
            } finally {

            }
        }


        /**
         * Handles the messages from a client.
         * @throws IOException
         */
        private void handleMessages() throws IOException {
            MessageType type = MessageType.values()[in.readByte()];
            int messageLength = in.readInt();

            if (messageLength > 100000){
                System.out.println("Message received is huge");
                return;
            }

            String message;
            Player thisPlayer;
            System.out.println("Received type: " + type.toString() + " length: " + messageLength);

            byte[] buffer = new byte[messageLength];

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
                    String newName = in.readUTF();
                    for (Player player:server.serverManager.game.getPlayers()) {
                        if (newName.equals(player.getName())){
                            newName += "nooblord";
                        }
                    }
                    for (Player player:server.serverManager.game.getPlayers()) {
                        if (player == this.player){
                            player.setName(this.player.getName());
                            player.setName(newName);
                        }
                    }
                    System.out.println("Name set to: " + player.getName() + ", was " + previousName);
                    break;
                case GameSendPlayersMessage: /** Sends player list to all clients **/
                    Server.this.sendGameMessagePlayers();
                    break;
                case GameReadyMessage: /** Changes the ready state of the sender **/
                    thisPlayer = server.clients.get(this);
                    thisPlayer.setReady(!thisPlayer.isReady());
                    Server.this.sendGameMessagePlayers();
                    break;
                case ClientSendPlayerMessage: /** Receives an updated player from the client **/
                    try {
                        in.readFully(buffer);

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
                case GameSendEndTurnMessage:
                    thisPlayer = server.clients.get(this);
                    for (Player player : game.getPlayers()) {
                        if (player.getName().equals(thisPlayer.getName())){
                            thisPlayer = player;
                            continue;
                        }
                    }

                    thisPlayer.setHasTurn(false);
                    for (Character character : thisPlayer.getCharacters()) {
                        character.setCurrentMovementPoints(character.getMovementPoints());
                        character.setHasAttacked(false);
                    }

                    for (Player player : game.getPlayers()) {
                        if (player.isSpectator()) continue;
                        int deadCharacters = 0;
                        for (Character character : player.getCharacters()) {
                            if (character.isDead()){
                                deadCharacters++;
                            }
                        }
                        if (deadCharacters == player.getCharacters().size()){
                            sendGameEnd();
                        }
                    }

                    int position = game.getPlayers().indexOf(thisPlayer);

                    if (position == 0 && game.getPlayers().size() >= 2){
                        game.getPlayers().get(1).setHasTurn(true);
                    } else {
                        game.getPlayers().get(0).setHasTurn(true);
                    }

                    Server.this.sendGameMessagePlayers();
                    break;
                case GameStartMessage:
                    Server.this.sendGameStart();
                    break;
                case GameCharacterMoveMessage:
                    int x = in.readInt();
                    int y = in.readInt();
                    String charName = in.readUTF();
                    String playerName = in.readUTF();


                    Server.this.sendCharacter(x,y,charName,playerName);
                    break;
                case GameSendMapMessage:
                    try {
                        in.readFully(buffer);
                        ByteArrayInputStream bIn = new ByteArrayInputStream(buffer);
                        ObjectInputStream is = new ObjectInputStream(bIn);
                        Map newMap = (Map) is.readObject();

                        game.setMap(newMap);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case GameJoinMessage:
                    this.sendMessage(MessageType.GameJoinMessage);
                    break;
                case GameEndMessage:
                    sendGameEnd();
                    Server.this.stop();
                    break;
                default: /** I DON'T KNOW **/
                    System.out.println("I DON'T KNOW");
                    break;
            }
        }

        /**
         * Send only a message type.
         * @param type
         */
        private void sendMessage(MessageType type){
            try {
                out.writeByte(type.ordinal());
                out.writeInt(1); //mLength
                out.flush();
            } catch (Exception e){
                e.printStackTrace();
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
                out.writeInt(message.getBytes().length); //mLength
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
                out.writeInt(bOut.size()); //mLength
                out.write(bOut.toByteArray());
                out.flush();
            } catch (Exception e){
                e.printStackTrace();
            }
        }


        private void sendCharacterMessage(MessageType type, int x, int y, String charName, String playerName){
            try {
                out.writeByte(type.ordinal()); // Message Type
                out.writeInt(9 + charName.getBytes().length + playerName.getBytes().length); // Message length
                out.writeInt(x); // x Cord
                out.writeInt(y); // y Cord
                out.writeUTF(charName); // character name
                out.writeUTF(playerName); // player name
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
