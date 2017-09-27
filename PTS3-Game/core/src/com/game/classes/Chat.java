package com.game.classes;

import com.example.network.Client.Client;

public class Chat {
    private Game game;
    private String[] chatlog;
    private Client client;

    /**
     * Creates a chat object.
     * @param client Requires a client, this client will handle the connection
     *               with the server.
     */
    public Chat(Client client){
        this.client = client;
    }

    /**
     * Get the game that belongs to this chat.
     * @return Returns a game object.
     */
    public Game getGame() {
        return game;
    }

    /**
     * TODO is nooit needed??
     * Set the game that belongs to this chat
     * @param game Requires a game object.
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Get the entire chatlog.
     * @return Returns a string array.
     */
    public String[] getChatlog() {
        return chatlog;
    }

    /**
     * TODO add message?
     * Sets the entire chatlog.
     * @param chatlog Requires a string array.
     */
    public void setChatlog(String[] chatlog) {
        this.chatlog = chatlog;
    }

    /**
     * Requires a successful connection with a server.
     * Sends a message to everyone in the chat.
     * @param message Requires a message as string.
     */
    public void sendMessageAll(String message){
        client.sendMessageAll(message);
    }

    /**
     * Requires a successful connection with a server.
     * Sends a whisper to a specific player.
     * @param playerName The name of the player you want to send a message to.
     * @param message The message as string.
     */
    public void sendMessageWhisper(String playerName, String message){
        client.sendMessageWhisper(playerName, message);
    }
}
