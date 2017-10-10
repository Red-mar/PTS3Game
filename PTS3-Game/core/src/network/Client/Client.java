package network.Client;

import com.game.classes.Game;
import com.game.classes.Player;
import network.Server.MessageType;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client {

    private ConnectionHandler connectionHandler;
    private ArrayList<ChatEvents> listeners = new ArrayList<ChatEvents>();
    private ArrayList<GameEvents> gameListeners = new ArrayList<GameEvents>();
    private boolean isConnected = false;

    /**
     * Checks if the client has a connection with a server.
     * @return
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Creates a client that will make a connection with a server.
     * @param serverIP The IP the server is running on. (use localhost for local use.)
     */
    public Client(String serverIP){
        connectionHandler = new ConnectionHandler(this, serverIP);
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
            connectionHandler.close();
        } else {
            sendMessageAll(userInput);
        }
    }

    /**
     * Starts the client and looks for a server.
     */
    public void start(){
        connectionHandler.start();
    }

    /**
     * Sends a 'All' message.
     * This will send a message to everyone.
     * @param message The message to send.
     */
    public void sendMessageAll(String message){
        connectionHandler.sendMessage(MessageType.ChatMessage, message);
    }

    /**
     * Sends a 'Whisper' message.
     * This will send a message to a specific player.
     * @param to The name of the client where the message should go to.
     * @param message The message to send.
     */
    public void sendMessageWhisper(String to, String message) {
        connectionHandler.sendMessage(MessageType.WhisperMessage, to, message);
    }

    /**
     * Sends a 'SetName' message.
     * This will set the name of the client on the server.
     * @param name The name of the client.
     */
    public void sendMessageSetName(String name){
        connectionHandler.sendMessage(MessageType.SetNameMessage, name);
    }

    /**
     * Asks the server to update the players for all clients.
     */
    public void sendMessageGetPlayers(){
        connectionHandler.sendMessage(MessageType.GameSendPlayersMessage);
    }

    /**
     * Asks the server to change the ready state for the sender.
     */
    public void sendMessageReady() { connectionHandler.sendMessage(MessageType.GameReadyMessage);}

    public void sendGameMessagePlayer(Player player){
        connectionHandler.sendObjectMessage(MessageType.ClientSendPlayerMessage, player);
    }

    public void sendGameEndTurn(){
        connectionHandler.sendMessage(MessageType.GameSendEndTurnMessage);
    }

    public void sendGameStart(){
        connectionHandler.sendMessage(MessageType.GameStartMessage);
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
                out.writeUTF(name);

                out.flush();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        /**
         * Send a message with no body
         * @param type The message type (see enum)
         */
        private void sendMessage(MessageType type){
            try {
                out.writeByte(type.ordinal());
                out.flush();
            } catch (IOException e){
                e.printStackTrace();
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
                out.writeUTF(firstMessage);
                out.writeUTF(secondMessage); //Split Message

                out.flush();

            } catch (IOException e){

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
                    Thread.sleep(100);
                    MessageType type = MessageType.values()[in.readByte()];
                    String message;

                    byte[] buffer = new byte[5000];
                    System.out.println("Received Message Type of:" + type.toString());

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
                                in.read(buffer);
                                System.out.println(buffer.length);
                                ByteArrayInputStream bIn = new ByteArrayInputStream(buffer);
                                ObjectInputStream is = new ObjectInputStream(bIn);
                                ArrayList<Player> players = ((ArrayList<Player>) is.readObject());

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
                        default:
                            System.out.println("no know?");
                    }
                }

            } catch (Exception e){
                e.printStackTrace();
                for (ChatEvents ce: listeners) {
                    ce.onDisconnect();
                }
                isConnected = false;
                //this.close();
            }
        }


    }
}
