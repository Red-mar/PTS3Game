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

    /**
     * Creates a client that will make a connection with a server.
     * @param serverIP The IP the server is running on. (use localhost for local use.)
     */
    public Client(String serverIP){
        connectionHandler = new ConnectionHandler(this, serverIP);
    }

    public void addListener(ChatEvents listener){
        listeners.add(listener);
    }

    public void addGameListener(GameEvents listener){
        gameListeners.add(listener);
    }

    /**
     * Reads text input from a console.
     * @param userInput Input from a console.
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
        } else {
            sendMessageAll(userInput.substring(userInput.indexOf(" ")+1));
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

    public void sendMessageGetPlayers(){
        connectionHandler.sendMessage(MessageType.GameSendPlayersMessage);
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

        private void sendMessage(MessageType type){
            try {
                out.writeByte(type.ordinal());
                out.flush();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        /**
         * Sends a 'Whisper' message.
         * This will send a message to a specific player.
         * @param firstMessage The name of the client where the message should go to.
         * @param secondMessage The message to send.
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
         * Send a 'StartGame' message.
         * This will signal to the server that the game should begin.
         * @param game The game that should start.
         */
        private void startGame(Game game){
            try {
                //out.writeByte(4);
                //TODO - game object has to be serialized into a byte array
                // http://www.java2s.com/Code/Java/File-Input-Output/Convertobjecttobytearrayandconvertbytearraytoobject.htm
                out.flush();

            } catch (IOException e){

            }
        }

        /**
         * Sends a 'endTurn' message.
         * This will signal to the server that the turn of the current
         * player has ended.
         * @param game The game.
         */
        private void endTurn(Game game){
            try {
                //out.writeByte(5);
                //TODO - Same as startGame
                out.flush();

            } catch (IOException e){

            }
        }

        /**
         * Sends a 'GameState' message.
         * This will send the current game state to the server
         * but will not end the turn. This way everyone can
         * see what the current player is doing.
         * @param game The current game.
         */
        private void sendGameState(Game game){
            try {
                //out.writeByte(5);
                //TODO - Same as startGame
                out.flush();

            } catch (IOException e){

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

                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                for (ChatEvents ce: listeners) {
                    ce.onConnect(serverIP);
                }

                while (isReceivingMessages && !socket.isClosed()){
                    MessageType type = MessageType.values()[in.readByte()];
                    String message;

                    byte[] buffer = new byte[1024];
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
                        default:
                            System.out.println("no know?");
                    }
                }

            } catch (Exception e){
                for (ChatEvents ce: listeners) {
                    ce.onDisconnect();
                }
                this.close();
            }
        }


    }
}
