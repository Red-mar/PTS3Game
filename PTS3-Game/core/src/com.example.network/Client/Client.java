package com.example.network.Client;

import com.game.classes.Game;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;

public class Client {

    private ConnectionHandler connectionHandler;

    public Client(String serverIP){
        connectionHandler = new ConnectionHandler(this, serverIP);
    }

    /*
        Reads the input from a text input.
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

    /*
        Starts the client and looks for the server.
     */
    public void start(){
        connectionHandler.start();
    }

    public void sendMessageAll(String message){
        connectionHandler.sendMessageAll(message);
    }

    public void sendMessageWhisper(String message, String message2) {
        connectionHandler.sendMessageWhisper(message, message2);
    }

    public void sendMessageSetName(String name){
        connectionHandler.sendMessageSetName(name);
    }

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

        ConnectionHandler(Client client, String serverIP){
            this.serverIP = serverIP;
            this.client = client;
        }

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

        private void sendMessageWhisper(String to, String message){
            try {
                out.writeByte(2);
                out.writeUTF(to);
                out.writeUTF(message); //Split Message

                out.flush();

            } catch (IOException e){

            }
        }

        private void sendMessageSetName(String name){
            try {
                out.writeByte(3);
                out.writeUTF(name);

                out.flush();
            } catch (IOException e){

            }
        }

        private void startGame(Game game){
            try {
                out.writeByte(4);
                //TODO - game object has to be serialized into a byte array
                // http://www.java2s.com/Code/Java/File-Input-Output/Convertobjecttobytearrayandconvertbytearraytoobject.htm
                out.flush();

            } catch (IOException e){

            }
        }

        /*
            Ends the turn.
            Sends
         */

        private void endTurn(Game game){
            try {
                out.writeByte(5);
                //TODO - Same as startGame
                out.flush();

            } catch (IOException e){

            }
        }

        /*
            Sends the game state but does not end the turn
            Does not have to be implemented yet.
         */
        private void sendGameState(Game game){
            try {
                out.writeByte(5);
                //TODO - Same as startGame
                out.flush();

            } catch (IOException e){

            }
        }

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
