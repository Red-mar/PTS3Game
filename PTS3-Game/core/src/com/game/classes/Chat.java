package com.game.classes;

import com.example.network.Client.Client;

public class Chat {
    private Game game;
    private String[] chatlog;
    private Client client;

    public Chat(Client client){
        this.client = client;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String[] getChatlog() {
        return chatlog;
    }

    public void setChatlog(String[] chatlog) {
        this.chatlog = chatlog;
    }

    public void sendMessageAll(String message){
        client.sendMessageAll(message);
    }

    public void sendMessageWhisper(String playerName, String message){
        client.sendMessageWhisper(playerName, message);
    }
}
