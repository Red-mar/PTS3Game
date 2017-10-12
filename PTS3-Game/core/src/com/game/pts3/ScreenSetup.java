package com.game.pts3;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
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

    Slider sliderVolume;

    public ScreenSetup(final Game game, AssetManager assetManager){
        stage = new Stage();
        this.game = game;
        this.manager = assetManager;
        this.prefs = Gdx.app.getPreferences("PTS3GamePreferences");
        testSound = manager.get("sound/LobbyIn.wav", Sound.class);
        skin = manager.get("data/uiskin.json", Skin.class);

        /**
         * Labels
         */
        Label lblWelcome = new Label("Welcome to game!\nPlease enter your name!", skin);
        lblWelcome.setPosition(10, 80);
        lblWelcome.setSize(90,30);
        Label lblIP = new Label("Enter IP address", skin);
        lblIP.setPosition(270,40);
        lblIP.setSize(90,90);
        Label lblVolumeSlider = new Label("Set volume",skin);
        lblVolumeSlider.setPosition(10,160);
        lblVolumeSlider.setSize(90,30);

        /**
         * TextField
         */
        final TextField tfName = new TextField("", skin);
        tfName.setSize(250, 30);
        tfName.setPosition(10, 40);
        final TextField tfIP = new TextField("localhost", skin);
        tfIP.setSize(250,30);
        tfIP.setPosition(270,40);

        /**
         * TextButton
         */
        TextButton btnStart = new TextButton("To game lobby", skin);
        btnStart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String string = "?";
                string = tfName.getText();
                prefs.putFloat("volume", sliderVolume.getValue());
                com.game.classes.Game gameState = new com.game.classes.Game(new Client(tfIP.getText()));
                game.setScreen(new ScreenLobby(game, string, gameState, manager));
            }
        });
        btnStart.setSize(250,20);
        btnStart.setPosition(10,10);
        TextButton btnFullscreen = new TextButton("Set Fullscreen", skin);
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
        btnFullscreen.setSize(250,20);
        btnFullscreen.setPosition(270,10);

        /**
         * Slider
         */
        sliderVolume = new Slider(0f,1f,0.01f,false,skin);
        sliderVolume.setPosition(10,150);
        sliderVolume.setSize(250f, 20f);
        sliderVolume.setValue(1.0f);
        sliderVolume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!sliderVolume.isDragging()){
                    testSound.play(sliderVolume.getValue());
                }
            }
        });

        stage.addActor(tfName);
        stage.addActor(tfIP);
        stage.addActor(lblWelcome);
        stage.addActor(lblIP);
        stage.addActor(lblVolumeSlider);
        stage.addActor(btnStart);
        stage.addActor(btnFullscreen);
        stage.addActor(sliderVolume);
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

        stage.act();
        stage.draw();
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
}
