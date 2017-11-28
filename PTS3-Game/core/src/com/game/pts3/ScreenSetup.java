package com.game.pts3;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.game.classes.network.Client.Client;

public class ScreenSetup implements Screen {
    Stage stage;
    private Skin skin;
    private Game game;
    private Preferences prefs;
    private Sound testSound;
    final private AssetManager manager;
    private SpriteBatch batch;
    private Texture backgroundImage;
    private float backgroundWidth;
    private float backgroundHeight;
    private float volume = 1.0f;
    private Music music;

    private Table mainTable;
    private Label lblVolume;
    private Slider sliderVolume;
    private Label lblWelcome;
    private Label lblName;
    private TextField tfName;
    private Button btnLobby;
    private Label lblIP;
    private TextField tfIP;
    private Button btnFullscreen;




    public ScreenSetup(final Game game, AssetManager assetManager){
        stage = new Stage();
        this.game = game;
        this.manager = assetManager;
        this.prefs = Gdx.app.getPreferences("PTS3GamePreferences");
        testSound = manager.get("sound/LobbyIn.wav", Sound.class);
        music = manager.get("bgm/bgmbase1.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(volume);
        music.play();

        skin = manager.get("data/uiskin.json", Skin.class);
        backgroundImage = manager.get("gridscape_title.jpg", Texture.class);
        backgroundHeight = Gdx.graphics.getHeight();
        backgroundWidth = Gdx.graphics.getWidth();
        batch = new SpriteBatch();
        /**
         * Table
         */
        mainTable = new Table(skin);


        /**
         * Labels
         */
        lblWelcome = new Label("Welcome to game!", skin);
        lblIP = new Label("Enter IP address", skin);
        lblName = new Label("Please enter your name!", skin);
        lblVolume = new Label("Set volume", skin);

        /**
         * TextField
         */
        tfName = new TextField("ENTER NAME", skin);
        tfIP = new TextField("localhost", skin);

        /**
         * TextButton
         */
        btnLobby = new TextButton("To game lobby", skin);
        btnLobby.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String string = "?";
                string = tfName.getText();
                prefs.putFloat("volume", volume);
                testSound.play(volume);
                com.game.classes.Game gameState = new com.game.classes.Game(new Client(tfIP.getText()));
                game.setScreen(new ScreenLobby(game, string, gameState, manager));
            }
        });


        btnFullscreen = new TextButton("Set Fullscreen", skin);
        btnFullscreen.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Graphics.Monitor currMonitor = Gdx.graphics.getMonitor();
                Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode(currMonitor);
                if (!Gdx.graphics.setFullscreenMode(displayMode)){
                    System.out.println("Could not enter fullscreen mode.");
                }
            }
        });

        /**
         * Slider
         */
        sliderVolume = new Slider(0f,1f,0.01f,false,skin);
        sliderVolume.setValue(1.0f);
        sliderVolume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!sliderVolume.isDragging()){
                    volume = sliderVolume.getValue();
                    music.setVolume(volume);
                    testSound.play(volume);
                }
            }
        });


        mainTable.add(lblVolume).width(200).spaceBottom(2);
        mainTable.row();

        mainTable.add(sliderVolume).width(400).colspan(4);
        mainTable.row();
        Label lblWhiteSpace = new Label("", skin);
        mainTable.add(lblWhiteSpace);
        mainTable.row();
        mainTable.add(lblWhiteSpace);
        mainTable.row();

        mainTable.add(lblWelcome).width(200).spaceBottom(2);
        mainTable.row();

        mainTable.add(lblName).width(200).spaceBottom(2).spaceRight(2);
        mainTable.add(lblIP).width(200).spaceBottom(2);
        mainTable.row();

        mainTable.add(tfName).width(200).spaceBottom(2).spaceRight(2);
        mainTable.add(tfIP).width(200).spaceBottom(2);
        mainTable.row();

        mainTable.add(btnLobby).width(200).spaceBottom(2).spaceRight(2);
        mainTable.add(btnFullscreen).width(200).spaceBottom(2);

        mainTable.setSize(200,200);
        mainTable.setPosition(  backgroundWidth /2 -100, backgroundHeight - 700);
        
        stage.addActor(mainTable);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        /**
         * Clear screen and set colour.
         */
        Gdx.gl.glClearColor( 0, 0.1f, 0.1f, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        batch.begin();
        batch.draw(backgroundImage, 0,0,backgroundWidth, backgroundHeight);
        batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        //backgroundWidth = width;
        //backgroundHeight = height;
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
}
