package com.game.pts3;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.game.classes.Player;
import com.game.classes.network.GameEvents;

import java.util.ArrayList;

public class ScreenEnd implements Screen {
    private Stage stage;
    private Game game;
    private com.game.classes.Game gameState;
    private Screen lastScreen;
    private float volume = 1.0f;
    private Slider sliderVolume;
    private Preferences prefs;
    private Window optionWindow;

    public ScreenEnd(final Game game, com.game.classes.Game gameState, final AssetManager manager, Screen lastScreen) {
        stage = new Stage();
        this.game = game;
        this.gameState = gameState;
        this.lastScreen = lastScreen;
        this.prefs = Gdx.app.getPreferences("PTS3GamePreferences");
        Skin skin = manager.get("data/uiskin.json", Skin.class);

        optionWindow = new Window("Options", skin);
        optionWindow.setColor(Color.GRAY);
        //optionWindow.setSize(250, 500);

        TextButton endGameButton = new TextButton("Exit the game", skin);
        endGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                endGame();
            }
        });
        //endGameButton.setPosition((Gdx.graphics.getWidth() / 2f) - (250 / 2), (Gdx.graphics.getHeight() / 5f));
        endGameButton.setSize(250, 50);

        TextButton returnButton = new TextButton("Return to the game", skin);
        returnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                returnGame();
            }
        });
        //returnButton.setPosition((Gdx.graphics.getWidth() / 2f) - (250 / 2), (Gdx.graphics.getHeight() / 5f)* 2);
        returnButton.setSize(250, 50);

        sliderVolume = new Slider(0f,1f,0.01f,false,skin);
        //sliderVolume.setPosition((Gdx.graphics.getWidth()/2f)-(250/2),(Gdx.graphics.getHeight()/5f) * 3);
        sliderVolume.setSize(250f, 20f);
        sliderVolume.setValue(prefs.getFloat("volume"));
        sliderVolume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!sliderVolume.isDragging()){
                    volume = sliderVolume.getValue();
                    manager.get("sound/LobbyIn.wav", Sound.class).play(volume);
                    manager.get("bgm/battlebase1.mp3", Music.class).setVolume(volume);
                    prefs.putFloat("volume", volume);
                }
            }
        });

        optionWindow.add(sliderVolume);
        optionWindow.row();
        optionWindow.add(returnButton);
        optionWindow.row();
        optionWindow.add(endGameButton);
        optionWindow.row();
        optionWindow.setSize(250f, 250f);

        optionWindow.setPosition((Gdx.graphics.getWidth() / 2f) - optionWindow.getWidth(), (Gdx.graphics.getHeight() / 2f) - optionWindow.getHeight());

        stage.addActor(optionWindow);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 0.5f);
        Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize ( int width, int height){
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause () {

    }

    @Override
    public void resume () {

    }

    @Override
    public void hide () {

    }

    @Override
    public void dispose () {
        stage.dispose();
    }

    private void endGame(){
        game.setScreen(lastScreen);
        lastScreen.show();
        if (gameState.getClient() == null){
            Gdx.app.exit();
        }else {
            gameState.getClient().sendGameEnd();
        }
    }

    private void returnGame(){
        game.setScreen(lastScreen);
        lastScreen.show();
    }
}
