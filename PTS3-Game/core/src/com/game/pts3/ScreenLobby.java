package com.game.pts3;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import network.Client.Client;

public class ScreenLobby implements Screen {
    private Game game;
    private com.game.classes.Game gameState;
    private Stage stage;
    private boolean isNameSet = false;
    Skin skin;
    Chat chat;
    EventListener enterText;

    float red = 0;
    float green = 0;

    public ScreenLobby(final Game game, final String name){
        this.game = game;

        stage = new Stage();

        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        TextArea t = new TextArea("Welcome to the game lobby!\nHere you can chat with fellow players.\n", skin);
        chat = new Chat(t,
                new ScrollPane(t, skin),
                new TextField("", skin),
                new TextButton("Send Message", skin));

        chat.getTextArea().setDisabled(true);

        chat.getScrollPane().setForceScroll(false, true);
        chat.getScrollPane().setFlickScroll(false);
        chat.getScrollPane().setOverscroll(false,true);
        chat.getScrollPane().setBounds(10f, 100f, 500f, 200f);

        chat.getTextField().setPosition(10, 40);
        chat.getTextField().setWidth(500);
        chat.getTextField().setHeight(50);

        TextButton btnReady = new TextButton("Start Game", skin);
        btnReady.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new ScreenGame(game));
                stage.clear();
            }
        });
        btnReady.setPosition(510,10);
        btnReady.setSize(120,20);

        TextButton btnConnect = new TextButton("Connect with server!", skin);
        btnConnect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (gameState == null){
                    gameState = new com.game.classes.Game(new Client("localhost"));
                    gameState.getClient().addListener(chat);
                }
            }
        });
        btnConnect.setPosition(10,10);
        btnConnect.setWidth(250);
        btnConnect.setHeight(20);

        Label lblPlayerName = new Label("Player name: " + name, skin);
        lblPlayerName.setPosition(10,330);
        Label lblMap = new Label("Selected map: N/A", skin);
        lblMap.setPosition(10,310);

        enterText = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (gameState == null || gameState.getClient() == null){
                    chat.getTextArea().appendText("Geen connectie met een server.\n");
                    chat.getTextField().setText("");
                } else {
                    if (!isNameSet){
                        gameState.getClient().sendMessageSetName(name);
                        isNameSet = true;
                    }
                    gameState.getClient().readInput(chat.getTextField().getText());
                    chat.getTextField().setText("");
                }
            }
        };
        chat.getBtnSendMessage().addListener(enterText);
        chat.getBtnSendMessage().setPosition(260, 10);
        chat.getBtnSendMessage().setWidth(250);
        chat.getBtnSendMessage().setHeight(20);

        stage.addActor(lblMap);
        stage.addActor(lblPlayerName);
        stage.addActor(btnConnect);
        stage.addActor(btnReady);

        stage.addActor(chat.scrollPane);
        stage.addActor(chat.textField);
        stage.addActor(chat.getBtnSendMessage());
    }



    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(red, green, 0.343f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && red > 0.1f){
            red-= 0.01f;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && red < 0.9f){
            red+= 0.01f;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && green > 0.1f){
            green-= 0.01f;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP) && green < 0.9f){
            green+= 0.01f;

        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            enterText.handle(new ChangeListener.ChangeEvent());
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}