package com.game.pts3;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.badlogic.gdx.utils.Timer;
import com.game.classes.Map;
import com.game.classes.Player;
import com.game.classes.network.Client.Client;
import com.game.classes.network.GameEvents;
import java.util.ArrayList;

public class ScreenLobby implements Screen, GameEvents {
    private Game game;
    private com.game.classes.Game gameState;
    private Stage stage;
    private Skin skin;
    private Chat chat;
    private List playerList;
    private Sound sound;
    private Sound errorSound;
    private Music music;
    private AssetManager manager;
    private Preferences prefs;
    private float volume;
    private float backgroundWidth;
    private float backgroundHeight;

    private Table mainTable;
    private Label lblMap;
    private Label lblPlayerName;
    private EventListener enterText;
    private TextField tfMap;
    private SpriteBatch batch;

    private ScrollPane scrollPane;

    float width = -2000;

    public ScreenLobby(Game game, String name, com.game.classes.Game gameState, AssetManager assetManager){
        this.game = game;
        this.manager = assetManager;
        this.gameState = gameState;
        gameState.setClientPlayer(new Player(name));
        this.prefs = Gdx.app.getPreferences("PTS3GamePreferences");
        volume = prefs.getFloat("volume");
        stage = new Stage();
        batch = new SpriteBatch();
        skin = manager.get("data/uiskin.json", Skin.class);
        sound = manager.get("sound/LobbyIn.wav", Sound.class);
        errorSound = manager.get("sound/Error.wav", Sound.class);
        music = manager.get("bgm/battlebase1.mp3", Music.class);
        backgroundHeight = Gdx.graphics.getHeight();
        backgroundWidth = Gdx.graphics.getWidth();

        /**
         * Table
         */
        mainTable = new Table(skin);

        /**
         * Chat
         */
        TextArea t = new TextArea("Welcome to the game lobby!\nHere you can chat with fellow players.\n", skin);
        scrollPane = new ScrollPane(t, skin);
        chat = new Chat(t,
                scrollPane,
                new TextField("", skin),
                new TextButton("Send Message", skin));

        chat.getTextArea().setDisabled(true);
        chat.getTextArea().setTouchable(Touchable.disabled);
        chat.getScrollPane().setForceScroll(false, true);
        chat.getScrollPane().setFlickScroll(false);
        chat.getScrollPane().setOverscroll(false,true);
        chat.getScrollPane().setBounds(10f, 100f, 500f, 200f);
        chat.getScrollPane().setTouchable(Touchable.disabled);
        chat.getScrollPane().setFadeScrollBars(false);

        chat.getTextField().setPosition(10, 40);
        chat.getTextField().setWidth(500);
        chat.getTextField().setHeight(50);
        chat.getTextField().setMessageText("Enter Message...");
        chat.getBtnSendMessage().setPosition(260, 10);
        chat.getBtnSendMessage().setWidth(250);
        chat.getBtnSendMessage().setHeight(20);
        gameState.getClient().addListener(chat);


        /**
         * Labels
         */
        lblPlayerName = new Label("Player name: " + name, skin);
        lblMap = new Label("Selected map: N/A", skin);

        /**
         * Buttons
         */
        TextButton btnStart = new TextButton("Start Game", skin);

        TextButton btnReady = new TextButton("Ready", skin);

        TextButton btnConnect = new TextButton("Connect with server!", skin);

        TextButton btnMap = new TextButton("Choose map", skin);

        /**
         * TextFields
         */
        tfMap = new TextField("castle", skin);

        /**
         * List
         */
        playerList = new List(skin);
        playerList.setPosition(backgroundWidth /2 + 100,320);
        playerList.setSize(250,100);

        /**
         * Listeners
         */
        enterText = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                startConnection();
            }
        };

        chat.getBtnSendMessage().addListener(enterText);

        btnStart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                startGame();
            }
        });

        btnReady.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setReady();
            }
        });

        btnConnect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                establishConnection();
            }
        });

        btnMap.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                loadMap();
            }
        });

        chat.setScrollbar();

        /**
         * Table Setup
         */
        Label lblWhiteSpace = new Label("", skin);

        mainTable.add(lblPlayerName).left();
        mainTable.row();

        mainTable.add(lblMap).left();
        mainTable.add(tfMap).width(200);
        mainTable.row();
        mainTable.add(lblWhiteSpace);
        mainTable.add(btnMap).width(200);

        mainTable.row();

        mainTable.add(lblWhiteSpace);
        mainTable.row();

        mainTable.add(lblWhiteSpace);
        mainTable.row();
        mainTable.add(chat.scrollPane).left();
        mainTable.add(btnConnect).width(200).top();
        mainTable.row();

        mainTable.add(chat.textField);
        mainTable.add(btnReady).width(200);
        mainTable.row();

        mainTable.add(lblWhiteSpace);
        mainTable.row();

        mainTable.add(chat.getBtnSendMessage());
        mainTable.add(btnStart).width(200);
        mainTable.row();

        mainTable.setSize(600,250);
        mainTable.setPosition(  75, backgroundHeight /2 - 250);

        stage.addActor(playerList);
        stage.addActor(mainTable);


    }



    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(manager.get("gridscape_title.jpg", Texture.class), 0,0,backgroundWidth, backgroundHeight);
        batch.draw(manager.get("Sprites/wizard-1.png", Texture.class), width + 1800, backgroundHeight * 0.5f, 150, 150);
        batch.draw(manager.get("Sprites/bowman-2.png", Texture.class), width + 1400, backgroundHeight * 0.4f, 150, 150);
        batch.draw(manager.get("Sprites/heavy-1.png", Texture.class), width + 1000, backgroundHeight * 0.8f, 150, 150);
        batch.draw(manager.get("Sprites/horseman-2.png", Texture.class), width + 600, backgroundHeight * 0.2f, 150, 150);
        batch.draw(manager.get("Sprites/swordsman-1.png", Texture.class), width + 200, backgroundHeight * 0.7f, 150, 150);
        if (Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT) && Gdx.input.isKeyJustPressed(Input.Keys.APOSTROPHE)){
            batch.draw(manager.get("portrait/anime.png", Texture.class), width, backgroundHeight * 0.5f, 200, 300);
        }
        batch.end();

        stage.act();
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            enterText.handle(new ChangeListener.ChangeEvent());
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            try {
                gameState.getClient().stop();
            } catch (NullPointerException e){

            }
            game.setScreen(new ScreenSetup(game, manager));
        }

        if (width < Gdx.graphics.getWidth() + 100){
            width += 0.01f / delta;
        } else {
            width = -2000;
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
        if (!gameState.getClientPlayer().isSpectator()){
            addCharacter(gameState.getClientPlayer().getName());
            if (gameState.getPlayers().size() == 0 || gameState.getPlayers().get(0) == gameState.getClientPlayer()){
                gameState.getClientPlayer().setHasTurn(true);
            }
        }

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                manager.get("bgm/bgmbase1.mp3", Music.class).stop();
                chat.getTextArea().appendText("Starting the game...\n");
                chat.setScrollbar();
                manager.get("sound/nice.wav", Sound.class).play(volume);
                chat.setScrollbar();
                final Timer timer = new Timer();
                Timer.Task task = new Timer.Task() {
                    @Override
                    public void run() {
                        music.setVolume(volume);
                        music.setLooping(true);
                        music.play();

                        game.setScreen(new ScreenGame(game, gameState, chat, manager));
                        stage.clear();
                        this.cancel();
                    }
                };
                timer.scheduleTask(task, 2f);
            }
        });
    }

    @Override
    public void onEndGame() { }

    @Override
    public void onJoinGame(){
        new Thread(new Runnable() { //Need to start the game on the open gl thread. so yeah..
            @Override
            public void run() {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new ScreenGame(game, gameState, chat, manager));
                        stage.clear();
                    }
                });
            }
        }).start();
    }

    @Override
    public void onUpdateCharacter(int x, int y, String charName, String playerName) { }

    private void establishConnection() {
        if(gameState.getClient().isConnected() == null && gameState.establishConnection(gameState.getClientPlayer().getName())){
            gameState.getClient().addGameListener(this);
            sound.play(volume);
        } else {
            errorSound.play(volume);
        }
    }

    private void addCharacter(String name) {
        gameState.generateCharacters(name, manager);
    }

    private void loadMap(){
        String fileName = tfMap.getText() + ".tmx";
        if (gameState.loadMap(fileName)){
            sound.play(volume);
            lblMap.setText("Selected map: " + fileName);
        }else{
            chat.getTextArea().appendText("Could not load map: " + fileName + "\n");
            chat.setScrollbar();
        }
    }

    private void setReady(){
        if (gameState.getMap() == null){
            chat.getTextArea().appendText("Geen map geselecteerd.\n");
            chat.setScrollbar();
            return;
        }
        if (gameState.getClient().isConnected() == null){
            chat.getTextArea().appendText("Geen connectie met een server\n");
            chat.setScrollbar();
            return;
        }
        gameState.getClient().sendMessageReady();
        sound.play(volume);
    }

    private void startGame(){
        if (gameState.getMap() == null){
            chat.getTextArea().appendText("Geen map geselecteerd.\n");
            chat.setScrollbar();
            return;
        }
        if (gameState.getClient().isConnected() == null){
            onStartGame();
            return;
        }
        for (Player player:gameState.getPlayers()) {
            if (!player.isReady()) {
                chat.textArea.appendText("Niet iedereen is READY.\n");
                chat.setScrollbar();
                return;
            }
            if (player.getCharacters().size() > 0){
                gameState.getClient().sendGameJoin();
                if (!gameState.getClientPlayer().isSpectator()){
                    gameState.generateCharacters(gameState.getClientPlayer().getName(), manager);
                }
                return;
            }
        }
        System.out.println("Game Starting ...");
        sound.play(volume);
        gameState.getClient().sendGameStart();
    }

    private void startConnection(){
        if (gameState.getClient().isConnected() == null){
            chat.getTextArea().appendText("Geen connectie met een server.\n");
            chat.setScrollbar();
            chat.getTextField().setText("");
        } else {
            gameState.getClient().readInput(chat.getTextField().getText());
            chat.getTextField().setText("");
            sound.play(volume);
        }
    }

}
