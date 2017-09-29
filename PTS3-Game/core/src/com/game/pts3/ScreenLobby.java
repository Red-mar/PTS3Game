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
import com.game.classes.Player;
import network.Client.Client;
import network.Client.GameEvents;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ScreenLobby implements Screen, GameEvents {
    private Game game;
    private com.game.classes.Game gameState;
    private Stage stage;
    private boolean isNameSet = false;
    private Skin skin;
    private Chat chat;
    private List playerList;

    private EventListener enterText;

    float red = 0;
    float green = 0;

    public ScreenLobby(final Game game, final String name){
        this.game = game;

        stage = new Stage();

        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        /**
         * Chat
         */
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


        /**
         * Buttons
         */
        TextButton btnStart = new TextButton("Start Game", skin);
        btnStart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new ScreenGame(game));
                stage.clear();
            }
        });
        btnStart.setPosition(510,10);
        btnStart.setSize(120,20);
        btnStart.setDisabled(true);

        TextButton btnReady = new TextButton("Ready", skin);
        btnReady.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameState.getClient().sendMessageReady();
            }
        });
        btnReady.setPosition(510,40);
        btnReady.setSize(120,20);

        TextButton btnConnect = new TextButton("Connect with server!", skin);
        btnConnect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                establishConnection(name);
            }
        });
        btnConnect.setPosition(10,10);
        btnConnect.setWidth(250);
        btnConnect.setHeight(20);

        /**
         * Labels
         */
        Label lblPlayerName = new Label("Player name: " + name, skin);
        lblPlayerName.setPosition(10,330);
        Label lblMap = new Label("Selected map: N/A", skin);
        lblMap.setPosition(10,310);

        /**
         * Player list
         */
        playerList = new List(skin);
        playerList.setPosition(10,350);
        playerList.setSize(250,100);


        stage.addActor(playerList);
        stage.addActor(lblMap);
        stage.addActor(lblPlayerName);
        stage.addActor(btnConnect);
        stage.addActor(btnStart);
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

    @Override
    public void onGetPlayers(ArrayList<Player> players) {
        chat.getTextArea().appendText("There are now " + players.size() + " player(s).\n");
        gameState.setPlayers(players);
        playerList.clearItems();
        playerList.setItems(players.toArray());

    }

    /**
     * hacky af
     */
    private void addGameListener(){
        gameState.getClient().addGameListener(this);
    }

    private void establishConnection(String name) {
        if (gameState == null) {
            gameState = new com.game.classes.Game(new Client("localhost"));
            gameState.getClient().start();
            while (!gameState.getClient().isConnected()) {
            } //TODO betere oplossing
            gameState.getClient().addListener(chat);
            addGameListener();
            gameState.getClient().sendMessageSetName(name);
            gameState.getClient().sendMessageGetPlayers();
        }
    }
}
