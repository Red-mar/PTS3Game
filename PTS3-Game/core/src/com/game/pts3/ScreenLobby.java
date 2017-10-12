package com.game.pts3;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
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
import network.Client.GameEvents;
import java.util.ArrayList;
import java.util.Random;

public class ScreenLobby implements Screen, GameEvents {
    private Game game;
    private com.game.classes.Game gameState;
    //private Player clientPlayer;
    private Stage stage;
    private Skin skin;
    private Chat chat;
    private List playerList;
    private Sound sound;
    private Music music;
    private AssetManager manager;
    private Preferences prefs;
    private float volume;

    private TiledMap tiledMap;

    private EventListener enterText;

    float red = 0;
    float green = 0;

    public ScreenLobby(final Game game, final String name, final com.game.classes.Game gameState, AssetManager assetManager){
        this.game = game;
        this.manager = assetManager;
        this.gameState = gameState;
        gameState.setClientPlayer(new Player(name));
        this.prefs = Gdx.app.getPreferences("PTS3GamePreferences");
        volume = prefs.getFloat("volume");
        stage = new Stage();
        skin = manager.get("data/uiskin.json", Skin.class);
        sound = manager.get("sound/LobbyIn.wav", Sound.class);
        music = manager.get("bgm/bgmbase1.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(volume);
        music.play();

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
                    gameState.getClient().readInput(chat.getTextField().getText());
                    chat.getTextField().setText("");
                    sound.play(volume);
                }
            }
        };
        chat.getBtnSendMessage().addListener(enterText);
        chat.getBtnSendMessage().setPosition(260, 10);
        chat.getBtnSendMessage().setWidth(250);
        chat.getBtnSendMessage().setHeight(20);

        gameState.getClient().addListener(chat);

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
                    System.out.println("Game Starting ...");
                    sound.play(volume);
                }
                gameState.getClient().sendGameStart();
            }
        });
        btnStart.setPosition(510,10);
        btnStart.setSize(120,20);

        TextButton btnReady = new TextButton("Ready", skin);
        btnReady.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (tiledMap == null){
                    chat.getTextArea().appendText("Geen map geselecteerd.\n");
                    return;
                }
                gameState.getClient().sendMessageReady();
                sound.play(volume);
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
                tiledMap = new TmxMapLoader().load("map_2.tmx");
                lblMap.setText("Selected map: " + "map_2.tmx");

                TiledMapTileLayer tileLayer = (TiledMapTileLayer)tiledMap.getLayers().get(0);
                int tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
                int tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);

                MapLayer mapLayer = tiledMap.getLayers().get("Impassable Terrain");
                MapObjects mapObjects = mapLayer.getObjects();
                ArrayList<RectangleMapObject> mapObjectList = new ArrayList<RectangleMapObject>();
                for (int i = 0; i < mapObjects.getCount(); i++){
                    RectangleMapObject rmo = (RectangleMapObject) mapObjects.get(i);
                    rmo.getRectangle().setX((rmo.getRectangle().x ) / tileWidth );
                    rmo.getRectangle().setY((rmo.getRectangle().y ) / tileHeight);
                    mapObjectList.add(rmo);
                }

                Map gameMap = new Map(tileLayer.getWidth(), tileLayer.getHeight(), tileHeight, tileWidth, mapObjectList);
                gameState.setMap(gameMap);
                sound.play(volume);
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
        stage.getViewport().update(width,height,true);
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

    @Override
    public void onStartGame() {
        addCharacter(gameState.getClientPlayer().getName());

        new Thread(new Runnable() { //Need to start the game on the open gl thread. so yeah..
            @Override
            public void run() {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new ScreenGame(game, tiledMap, gameState, chat, manager));
                        stage.clear();
                    }
                });
            }
        }).start();
    }

    @Override
    public void onEndGame() {

    }

    /**
     * hacky af
     */
    //private void addGameListener(){
    //    gameState.getClient().addGameListener(this);
    //}

    private void establishConnection(String name) {
        gameState.establishConnection(name, chat);
        gameState.getClient().addGameListener(this);
        //addGameListener();
        sound.play(volume);
    }

    private void addCharacter(String name) {
        gameState.generateCharacters(name, manager);
    }
}
