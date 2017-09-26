package com.game.classes;

public class Chat {
    private Game game;
    private String message;
    private String[] chatlog;

    public Chat(){
        chatlog = new String[50];
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String[] getChatlog() {
        return chatlog;
    }

    public void setChatlog(String[] chatlog) {
        this.chatlog = chatlog;
    }
}
