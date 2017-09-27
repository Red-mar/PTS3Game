package network.Client;

import com.game.classes.Game;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;

public class Client {

    private ConnectionHandler connectionHandler;

    private IClientEvents clientEvents;

    /**
     * Creates a client that will make a connection with a server.
     * @param serverIP The IP the server is running on. (use localhost for local use.)
     */
    public Client(String serverIP){
        clientEvents = new ClientEventHandler();
        connectionHandler = new ConnectionHandler(this, serverIP);
    }

    /**
     * Reads text input from a console.
     * @param userInput Input from a console.
     */
    public void readInput(String userInput){
        if (userInput.startsWith("/all ")){
            sendMessageAll(userInput.substring(userInput.indexOf(" ")+1));
        }
        else if (userInput.startsWith("/whisper ")){
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
        else{
            System.out.println("Use '/all 'message'' to send everyone a message");
            System.out.println("Use /whisper 'to' 'message' to send a message to one person ");
            System.out.println("Use /name 'name' to set your name");
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
        connectionHandler.sendMessageAll(message);
    }

    /**
     * Sends a 'Whisper' message.
     * This will send a message to a specific player.
     * @param to The name of the client where the message should go to.
     * @param message The message to send.
     */
    public void sendMessageWhisper(String to, String message) {
        connectionHandler.sendMessageWhisper(to, message);
    }

    /**
     * Sends a 'SetName' message.
     * This will set the name of the client on the server.
     * @param name The name of the client.
     */
    public void sendMessageSetName(String name){
        connectionHandler.sendMessageSetName(name);
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
         * Sends a 'All' message.
         * This will send a message to everyone.
         * @param message The message to send.
         */
        private void sendMessageAll(String message){
            try {
                // Set the first byte
                // 1 = Message A
                out.writeByte(1);
                out.writeUTF(message);

                //Send the data
                out.flush();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        /**
         * Sends a 'Whisper' message.
         * This will send a message to a specific player.
         * @param to The name of the client where the message should go to.
         * @param message The message to send.
         */
        private void sendMessageWhisper(String to, String message){
            try {
                out.writeByte(2);
                out.writeUTF(to);
                out.writeUTF(message); //Split Message

                out.flush();

            } catch (IOException e){

            }
        }

        /**
         * Sends a 'SetName' message.
         * This will set the name of the client on the server.
         * @param name The name of the client.
         */
        private void sendMessageSetName(String name){
            try {
                out.writeByte(3);
                out.writeUTF(name);

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
                out.writeByte(4);
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
                out.writeByte(5);
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
                out.writeByte(5);
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
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                socket = new Socket(serverIP, 4321);

                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                while (isReceivingMessages && !socket.isClosed()){
                    int messageType = in.readByte();

                    switch (messageType) {
                        case 1: //Type A
                            System.out.println(in.readUTF());
                            break;
                        case 2: //Type B
                            System.out.println(in.readUTF());
                            break;
                        case 3: //TypeC
                            clientEvents.onMessaged("message");
                            System.out.println("Message C [1]: " + in.readUTF());
                            System.out.println("Message C [2]: " + in.readUTF());
                            break;
                        default:
                            //buffer = new byte[512];
                            System.out.println("no know?");
                    }
                }

            } catch (Exception e){
                e.printStackTrace();
                this.close();
            }
        }


    }
}
