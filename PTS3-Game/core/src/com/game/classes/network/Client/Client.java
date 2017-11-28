package com.game.classes.network.Client;

import com.game.classes.Map;
import com.game.classes.Player;
import com.game.classes.network.ChatEvents;
import com.game.classes.network.GameEvents;
import com.game.classes.network.MessageType;
import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Client {

    private ConnectionHandler connectionHandler;
    private ArrayList<ChatEvents> listeners = new ArrayList<ChatEvents>();
    private ArrayList<GameEvents> gameListeners = new ArrayList<GameEvents>();
    private String serverIP;
    private Boolean isConnected = null;

    private RMIClient rmiClient;

    /**
     * Checks if the client has a connection with a server.
     * @return
     */
    public Boolean isConnected() {
        return isConnected;
    }

    public void setConnected(Boolean connected) {
        isConnected = connected;
    }

    public String getServerIP() {
        return serverIP;
    }

    /**
     * Creates a client that will make a connection with a server.
     * @param serverIP The IP the server is running on. (use localhost for local use.)
     */
    public Client(String serverIP){
        this.serverIP = serverIP;
        /*
        rmiClient = new RMIClient(serverIP, 1337);
        try {
            rmiClient.getInfo().sendMessage("test");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        */
    }

    /**
     * Adds an object that implements the ChatEvents interface.
     * @param listener
     */
    public void addListener(ChatEvents listener){
        listeners.add(listener);
    }

    /**
     * Adds an object that implements the GameEvents interface.
     * By adding the class it will be notified and the
     * implemented method will run.
     * @param listener
     */
    public void addGameListener(GameEvents listener){
        gameListeners.add(listener);
    }

    /**
     * Processes a string for use in a console.
     * @param userInput The input to be processed.
     */
    public void readInput(String userInput){
        if (userInput.startsWith("/whisper ")){
            String[] mSplit = userInput.split(" ");

            String textMessage = userInput.substring(userInput.indexOf(" ")+1);
            textMessage = textMessage.substring(textMessage.indexOf(" ")+1);

            sendMessageWhisper(mSplit[1], textMessage);
        }
        else if (userInput.startsWith("/name")){
            String[] mSplit = userInput.split(" ");
            if (mSplit[1].isEmpty()){
                System.out.println("Name can't be empty.");
            }
            sendMessageSetName(mSplit[1]);
        }
        else if (userInput.startsWith("/help")){
            System.out.println("Use '/all 'message'' to send everyone a message");
            System.out.println("Use /whisper 'to' 'message' to send a message to one person ");
            System.out.println("Use /name 'name' to set your name");
        }
        else if (userInput.startsWith("/players")){
            sendMessageGetPlayers();
        }
        else if (userInput.startsWith("/close")){ /* WARNING EXPERIMENTAL */
            sendGameEnd();
        }else if (userInput.startsWith("/endturn")){
            sendGameEndTurn();
        }
        else if(userInput.startsWith("/join")){
            sendGameJoin();
        }
        else {
            sendMessageAll(userInput);
        }
    }

    /**
     * Starts the client and looks for a server.
     */
    public void start(){
        connectionHandler = new ConnectionHandler(this, serverIP);
        connectionHandler.start();
    }

    /**
     * Sends a 'All' message.
     * This will send a message to everyone.
     * @param message The message to send.
     */
    public void sendMessageAll(String message){
        connectionHandler.sendMessage(
                MessageType.ChatMessage,
                message);
    }

    /**
     * Sends a 'Whisper' message.
     * This will send a message to a specific player.
     * @param to The name of the client where the message should go to.
     * @param message The message to send.
     */
    public void sendMessageWhisper(String to, String message) {
        connectionHandler.sendMessage(
                MessageType.WhisperMessage,
                to,
                message);
    }

    /**
     * Sends a 'SetName' message.
     * This will set the name of the client on the server.
     * @param name The name of the client.
     */
    public void sendMessageSetName(String name){
        connectionHandler.sendMessage(
                MessageType.SetNameMessage,
                name);
    }

    /**
     * Asks the server to update the players for all clients.
     */
    public void sendMessageGetPlayers(){
        connectionHandler.sendMessage(
                MessageType.GameSendPlayersMessage);
    }

    /**
     * Asks the server to change the ready state for the sender.
     */
    public void sendMessageReady() {
        connectionHandler.sendMessage(
                MessageType.GameReadyMessage);
    }

    /**
     * Changes ready state of the player.
     * @param player
     */
    public void sendGameMessagePlayer(Player player){
        connectionHandler.sendObjectMessage(
                MessageType.ClientSendPlayerMessage,
                player);
    }

    /**
     * Sends a map to the server.
     * @param map
     */
    public void sendGameMap(Map map){
        connectionHandler.sendObjectMessage(
                MessageType.GameSendMapMessage,
                map);
    }

    /**
     * Ends the turn of the sender.
     */
    public void sendGameEndTurn() {
        connectionHandler.sendMessage(
                MessageType.GameSendEndTurnMessage);
    }

    /**
     * Make the server start the game for every player
     */
    public void sendGameStart(){
        connectionHandler.sendMessage(
                MessageType.GameStartMessage);
    }

    public void sendGameEnd(){
            connectionHandler.sendMessage(
                    MessageType.GameEndMessage
            );
    }

    public void sendGameJoin(){
        connectionHandler.sendMessage(
                MessageType.GameJoinMessage
        );
    }

    /**
     * Send a character movement to the server.
     * @param x
     * @param y
     * @param charName
     * @param playerName
     */
    public void sendCharacterMove(int x, int y, String charName, String playerName) {
        connectionHandler.sendCharacterMove(
                MessageType.GameCharacterMoveMessage,
                x,
                y,
                charName,
                playerName);
    }

    /**
     * Stops the connection with the server.
     */
    public void stop(){
        connectionHandler.close();
    }


    private class ConnectionHandler extends Thread{
        private Client client;

        private String serverIP;
        private Socket socket;

        private DataInputStream in;
        private DataOutputStream out;
        private boolean isReceivingMessages = true;

        /**
         * This class will handle the connection with the server.
         * @param client The client that will connect with the server.
         * @param serverIP The IP address to connect to.
         */
        ConnectionHandler(Client client, String serverIP){
            this.serverIP = serverIP;
            this.client = client;
        }

        /**
         * Sends a 'SetName' message.
         * This will set the name of the client on the server.
         * @param name The name of the client.
         */
        private void sendMessage(MessageType type, String name){
            try {
                out.writeByte(type.ordinal());
                out.writeInt(name.getBytes().length); //mLength
                out.writeUTF(name);

                out.flush();
            } catch (IOException e){
                e.printStackTrace();
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }

        /**
         * Send a message with no body
         * @param type The message type (see enum)
         */
        private void sendMessage(MessageType type){
            try {
                out.writeByte(type.ordinal());
                out.writeInt(1); //mLength
                out.flush();
            } catch (IOException e){
                e.printStackTrace();
            }
            catch(Exception ex){
                ex.getMessage();
            }
        }

        /**
         * Send a message with 2 strings (used for whispers)
         * @param type The message type.
         * @param firstMessage The first string
         * @param secondMessage The second string
         */
        private void sendMessage(MessageType type, String firstMessage, String secondMessage){
            try {
                out.writeByte(type.ordinal());
                out.writeInt(firstMessage.getBytes().length + secondMessage.getBytes().length); //mLength
                out.writeUTF(firstMessage);
                out.writeUTF(secondMessage); //Split Message

                out.flush();

            } catch (IOException e){
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
                out.write(bOut.toByteArray(), 0, bOut.size());
                out.flush();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        private void sendCharacterMove(MessageType type, int x, int y, String charName, String playerName){
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

        /**
         * Closes the connection with the server.
         */
        private void close(){
            try {
                out.writeByte(-1);
                out.flush();
                isReceivingMessages = false;
                this.socket.close();
            } catch (IOException e){
                System.out.println("Server unexpectedly closed.");
            }
        }

        /**
         * Start processing received messages.
         */
        @Override
        public void run() {
            try {
                socket = new Socket(serverIP, 4321);

                if (!socket.isConnected()) return;

                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                for (ChatEvents ce: listeners) {
                    ce.onConnect(serverIP);
                }

                isConnected = true;

                while (isReceivingMessages && !socket.isClosed()){
                    MessageType type = MessageType.values()[in.readByte()];
                    int messageLength = in.readInt();
                    String message;

                    byte[] buffer = new byte[messageLength];
                    System.out.println("Received type: " + type.toString() + " length: " + messageLength);

                    switch (type) {
                        case ChatMessage: //Type A
                            message = in.readUTF();
                            for (ChatEvents ce: listeners) {
                                ce.onMessaged(message);
                            }
                            System.out.println(message);
                            break;
                        case WhisperMessage: //Type B
                            message = in.readUTF();
                            for (ChatEvents ce: listeners) {
                                ce.onMessaged(message);
                            }
                            System.out.println(message);
                            break;
                        case SetNameMessage: //TypeC
                            System.out.println("Message C [1]: " + in.readUTF());
                            System.out.println("Message C [2]: " + in.readUTF());
                            break;
                        case GameSendPlayersMessage:
                            try {
                                in.readFully(buffer);

                                ByteArrayInputStream bIn = new ByteArrayInputStream(buffer);
                                ObjectInputStream is = new ObjectInputStream(bIn);
                                ArrayList<Player> players = (ArrayList<Player>) is.readObject();

                                for (GameEvents ge : gameListeners) {
                                    ge.onGetPlayers(players);
                                }
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                            break;
                        case GameStartMessage:
                            for (GameEvents gameListener : gameListeners) {
                                gameListener.onStartGame();
                            }
                            break;
                        case GameEndMessage:
                            for (GameEvents gameListener : gameListeners) {
                                gameListener.onEndGame();
                            }
                            break;
                        case GameCharacterMoveMessage:
                            int x = in.readInt();
                            int y = in.readInt();
                            String charName = in.readUTF();
                            String playerName = in.readUTF();

                            for (GameEvents gameListener : gameListeners) {
                                gameListener.onUpdateCharacter(x, y, charName, playerName);
                            }
                            break;
                        case GameJoinMessage:
                            for (GameEvents gameListener : gameListeners) {
                                gameListener.onJoinGame();
                            }
                            break;
                        default:
                            System.out.println("no know?");
                    }
                }

            }
            catch (Exception e){
                for (ChatEvents chatEvents: listeners) {
                    chatEvents.onDisconnect(e.getMessage());
                }
                isConnected = false;
            }
        }


    }
}
