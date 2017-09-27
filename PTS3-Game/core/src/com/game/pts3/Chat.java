package com.game.pts3;

import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import network.Client.IClientEvents;

public class Chat implements IClientEvents {
    TextArea textArea;

    public Chat(TextArea textArea){
        this.textArea = textArea;
    }

    public TextArea getTextArea() {
        return textArea;
    }

    @Override
    public void onConnect(String serverName) {
        textArea.appendText("Connected with " + serverName + "\n");
    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public void onMessaged(String message) {
        textArea.appendText(message + "\n");
    }
}
