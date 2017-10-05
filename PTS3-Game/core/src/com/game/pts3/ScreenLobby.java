package com.game.pts3;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.game.classes.Character;
import com.game.classes.Map;
import com.game.classes.Player;
import com.game.classes.Terrain;
import javafx.stage.FileChooser;
import network.Client.Client;
import network.Client.GameEvents;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class ScreenLobby implements Screen, GameEvents {
    private Game game;
    private com.game.classes.Game gameState;
    private Player clientPlayer;
    private Stage stage;
    private boolean isNameSet = false;
    private Skin skin;
    private Chat chat;
    private List playerList;
    private Sound sound;

    private TiledMap tiledMap;

    private EventListener enterText;

    float red = 0;
    float green = 0;

    public ScreenLobby(final Game game, final String name, final com.game.classes.Game gameState){
        this.game = game;
        this.gameState = gameState;
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        sound = Gdx.audio.newSound(Gdx.files.internal("sound/LobbyIn.wav"));

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
        chat.getScrollPane().setTouchable(Touchable.disabled);
        chat.getTextArea().setTouchable(Touchable.disabled);
        chat.getTextField().setPosition(10, 40);
        chat.getTextField().setWidth(500);
        chat.getTextField().setHeight(50);
        enterText = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (gameState == null || gameState.getClient().isConnected() == false){
                    chat.getTextArea().appendText("Geen connectie met een server.\n");
                    chat.getTextField().setText("");
                } else {
                    if (!isNameSet){
                        gameState.getClient().sendMessageSetName(name);
                        isNameSet = true;
                    }
                    gameState.getClient().readInput(chat.getTextField().getText());
                    chat.getTextField().setText("");
                    sound.play(1.0f);
                }
            }
        };
        chat.getBtnSendMessage().addListener(enterText);
        chat.getBtnSendMessage().setPosition(260, 10);
        chat.getBtnSendMessage().setWidth(250);
        chat.getBtnSendMessage().setHeight(20);

        /**
         * Labels
         */
        Label lblPlayerName = new Label("Player name: " + name, skin);
        lblPlayerName.setPosition(10,330);
        final Label lblMap = new Label("Selected map: N/A", skin);
        lblMap.setPosition(10,310);

        /**
         * Buttons
         */
        TextButton btnStart = new TextButton("Start Game", skin);
        btnStart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (gameState == null) return;
                for (Player player:gameState.getPlayers()) {
                    if (!player.isReady()) {
                        chat.textArea.appendText("Niet iedereen is READY.\n");
                        return;
                    }
                    if (tiledMap == null){
                        chat.textArea.appendText("Geen map geselecteerd.\n");
                    }
                    System.out.println("Game Starting ...");
                    sound.play(1.0f);
                }
                addCharacter(name);
                game.setScreen(new ScreenGame(game, tiledMap, gameState, clientPlayer, chat));
                stage.clear();
            }
        });
        btnStart.setPosition(510,10);
        btnStart.setSize(120,20);

        TextButton btnReady = new TextButton("Ready", skin);
        btnReady.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameState.getClient().sendMessageReady();
                sound.play(1.0f);
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

        TextButton btnMap = new TextButton("Choose map", skin);
        btnMap.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                tiledMap = new TmxMapLoader().load("map.tmx");
                lblMap.setText("Selected map: " + "map.tmx");

                TiledMapTileLayer tileLayer = (TiledMapTileLayer)tiledMap.getLayers().get(0);
                int tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
                int tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);

                Map gameMap = new Map(tileLayer.getWidth(), tileLayer.getHeight(), tileHeight, tileWidth);
                gameState.setMap(gameMap);
                sound.play(1.0f);
            }
        });
        btnMap.setPosition(510, 70);
        btnMap.setSize(120, 20);

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
        stage.addActor(btnMap);

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
        /**
         * Clears the screen.
         */
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
        if (!gameState.getClient().isConnected()){
            gameState.getClient().start();
            while (!gameState.getClient().isConnected()) {
            } //TODO betere oplossing
            gameState.getClient().addListener(chat);
            addGameListener();
            gameState.getClient().sendMessageSetName(name);
            gameState.getClient().sendMessageGetPlayers();
            sound.play(1.0f);
        }
    }

    private void addCharacter(String name){
        for (Player player: gameState.getPlayers()) {
            if (player.getName().equals(name)){
                clientPlayer = player;
            }
        }
        String textureFile = "Sprites/swordsman-1.png";
        if (clientPlayer.getName().equals("Red")){
            textureFile = "Sprites/swordsman-2.png";
        }
        Texture texture = new Texture(Gdx.files.internal(textureFile));
        Sprite sprite = new Sprite(texture);
        for (int i = 0; i < 3; i ++){
            Random rnd = new Random();
            Terrain terrain = gameState.getMap().getTerrains()[rnd.nextInt(40)][rnd.nextInt(40)];
            Character character = new Character("Pietje", 10, 4, 1, 3,sprite, terrain,textureFile,clientPlayer);
            clientPlayer.addCharacter(character);
        }
        gameState.getClient().sendGameMessagePlayer(clientPlayer);
    }
}
