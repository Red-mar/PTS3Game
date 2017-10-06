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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class ScreenLoad implements Screen {
    Stage stage;
    private Skin skin;
    private Game game;
    private AssetManager manager;

    private float red = 0;
    private float green = 0;

    private Label lblProgress;

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

        manager.load("sound/Alarm.wav", Sound.class);
        manager.load("sound/Damage.wav", Sound.class);
        manager.load("sound/Error.wav", Sound.class);
        manager.load("sound/Heal.wav", Sound.class);
        manager.load("sound/LobbyIn.wav", Sound.class);

        manager.load("bgm/bgmbase1.mp3", Music.class);

        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        lblProgress = new Label("0/100", skin);
        lblProgress.setPosition(10, 10);
        lblProgress.setSize(640,480);
        lblProgress.setFontScale(5);

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
        Gdx.gl.glClearColor( 0, 0, red, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        if (manager.update()){
            game.setScreen(new ScreenSetup(game, manager));
        }

        float progress = manager.getProgress();
        progress = (float)Math.round(progress * 100) / 100;

        if (red > 0.9f) {
            red = 0.1f;
        } else
            red += 0.01f;

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
