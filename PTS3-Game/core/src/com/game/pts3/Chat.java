package com.game.pts3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.game.classes.network.ChatEvents;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Collection of UI elements to form a chat box.
 */
public class Chat implements ChatEvents {
    TextArea textArea;
    ScrollPane scrollPane;
    TextField textField;
    TextButton btnSendMessage;
    List<String> curseWords;

    public Chat(TextArea textArea,
                ScrollPane scrollPane,
                TextField textField,
                TextButton textButton){

        this.textArea = textArea;
        this.scrollPane = scrollPane;
        this.textField = textField;
        this.btnSendMessage = textButton;
        this.curseWords = new ArrayList<String>();

        readFile();
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
        setScrollbar();
    }

    @Override
    public void onDisconnect(String reason) {
        textArea.appendText("Disconnected from server. " + reason + "\n");
        setScrollbar();
    }

    @Override
    public void onMessaged(String message) {
        textArea.appendText(message.replaceAll("(?i)"+censorWords(curseWords.toArray(new String[curseWords.size()])), "*") + "\n");
    }

    // Creates the regex to censor
    public String censorWords(String... words) {
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (sb.length() > 0) sb.append("|");
            sb.append(
                    String.format("(?<=(?=%s).{0,%d}).",
                            Pattern.quote(w),
                            w.length()-1
                    )
            );
        }
        return sb.toString();
    }

    private void readFile(){
        try {
            File file = new File("data/swearWords.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line = null;
            while ((line = br.readLine()) != null) {
                // \\s+ means any number of whitespaces between tokens
                curseWords.add(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Finished reading file.");
        }
    }

    public void setScrollbar(){
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                if(getTextArea().getLines() < 30)
                    getTextArea().setPrefRows(getTextArea().getLines());
                else
                    getTextArea().setPrefRows(30);
                scrollPane.layout();
                scrollPane.setScrollPercentY(100);
            }
        });
    }
}
