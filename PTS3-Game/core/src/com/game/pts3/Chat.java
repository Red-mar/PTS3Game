package com.game.pts3;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.game.classes.Player;
import network.Client.ChatEvents;

import java.util.ArrayList;

/**
 * Collection of UI elements to form a chat box.
 */
public class Chat implements ChatEvents {
    TextArea textArea;
    ScrollPane scrollPane;
    TextField textField;
    TextButton btnSendMessage;

    public Chat(TextArea textArea,
                ScrollPane scrollPane,
                TextField textField,
                TextButton textButton) {

        this.textArea = textArea;
        this.scrollPane = scrollPane;
        this.textField = textField;
        this.btnSendMessage = textButton;
    }

    public TextArea getTextArea() {
        return textArea;
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public TextButton getBtnSendMessage() {
        return btnSendMessage;
    }

    public TextField getTextField() {
        return textField;
    }

    @Override
    public void onConnect(String serverName) {
        textArea.appendText("Connected with " + serverName + ".\n");
    }

    @Override
    public void onDisconnect() {
        textArea.appendText("Disconnected from server.\n");
    }

    @Override
    public void onMessaged(String message) {
        textArea.appendText(message + "\n");
    }
}
