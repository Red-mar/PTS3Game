package com.game.pts3;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import javax.xml.soap.Text;


public class ScreenLoad implements Screen {
    Stage stage;
    private Skin skin;
    private Game game;
    private AssetManager manager;

    private Label lblProgress;
    private ProgressBar progressBar;

    public ScreenLoad(final Game game){
        stage = new Stage();
        this.game = game;
        manager = new AssetManager();
        manager.load("data/uiskin.json", Skin.class);

        manager.load("Sprites/bowman-1.png", Texture.class);
        manager.load("Sprites/bowman-2.png", Texture.class);
        manager.load("Sprites/heavy-1.png", Texture.class);
        manager.load("Sprites/heavy-2.png", Texture.class);
        manager.load("Sprites/horseman-1.png", Texture.class);
        manager.load("Sprites/horseman-2.png", Texture.class);
        manager.load("Sprites/swordsman-1.png", Texture.class);
        manager.load("Sprites/swordsman-2.png", Texture.class);
        manager.load("Sprites/wizard-1.png", Texture.class);
        manager.load("Sprites/wizard-2.png", Texture.class);
        manager.load("maan.png", Texture.class);
        manager.load("badlogic.jpg", Texture.class);
        manager.load("gridscape_title.jpg", Texture.class);

        manager.load("sound/Alarm.wav", Sound.class);
        manager.load("sound/Damage.wav", Sound.class);
        manager.load("sound/Error.wav", Sound.class);
        manager.load("sound/Heal.wav", Sound.class);
        manager.load("sound/LobbyIn.wav", Sound.class);
        manager.load("sound/wololo.wav", Sound.class);

        manager.load("bgm/bgmbase1.mp3", Music.class);
        manager.load("bgm/battlebase1.mp3", Music.class);

        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        lblProgress = new Label("0/100", skin);
        lblProgress.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        lblProgress.setSize(100,100);

        progressBar = new ProgressBar(0,1,0.01f,false,skin);
        progressBar.setPosition(Gdx.graphics.getHeight()/2,Gdx.graphics.getHeight()/2);
        progressBar.setSize(Gdx.graphics.getWidth()/2,100);

        stage.addActor(progressBar);
        stage.addActor(lblProgress);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        /**
         * Clear screen and set colour.
         */
        Gdx.gl.glClearColor( 0, 0, 0, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        if (manager.update()){
            game.setScreen(new ScreenSetup(game, manager));
        }

        float progress = manager.getProgress();
        progressBar.setValue(progress);

        progress = (float)Math.round(progress * 100) / 100;
        lblProgress.setText("Loading... " + (progress * 100) + "/100");

        stage.act();
        stage.draw();
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
