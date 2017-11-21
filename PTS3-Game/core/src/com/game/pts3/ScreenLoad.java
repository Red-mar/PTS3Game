package com.game.pts3;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Timer;

import javax.xml.soap.Text;


public class ScreenLoad implements Screen {
    Stage stage;
    private Skin skin;
    private Game game;
    private AssetManager manager;

    private Label lblProgress;
    private ProgressBar progressBar;
    private SpriteBatch batch;
    private Timer timer;
    private Timer.Task task;
    private float logoFade = 0f;
    private float musicFade = 1f;
    private Texture logo;
    private boolean isLoading = false;
    private Music loadingMusic;
    private float red = 1f;
    private float green = 1f;

    public ScreenLoad(final Game game){
        logo = new Texture(Gdx.files.internal("AALogo.png"));
        loadingMusic = Gdx.audio.newMusic(Gdx.files.internal("bgm/loadbase1.mp3"));
        loadingMusic.play();
        manager = new AssetManager();
        stage = new Stage();
        timer = new Timer();
        timer.scheduleTask(new LogoTask(), 0f, 0.05f);
        timer.scheduleTask(new LoadTask(), 20.0f);
        batch = new SpriteBatch();
        this.game = game;

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

        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)){
            new LoadTask().run();
        }

        if (isLoading){
            float progress = manager.getProgress();
            progressBar.setValue(progress);

            progress = (float)Math.round(progress * 100) / 100;
            lblProgress.setText("Loading... " + (progress * 100) + "/100");

            if (manager.update()){
                game.setScreen(new ScreenSetup(game, manager));
            }

            stage.act();
            stage.draw();
        } else {
            batch.begin();
            batch.setColor(red,green,1f, logoFade);
            batch.draw(logo, Gdx.graphics.getWidth() / 4.0f,Gdx.graphics.getHeight() / 8.0f);
            batch.end();
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

    private class LogoTask extends Timer.Task{

        @Override
        public void run() {
            if (logoFade < 1.0f){
                logoFade += 0.003f;
            } else {
                musicFade -= 0.015f;
                loadingMusic.setVolume(musicFade);
            }

            if (red > 0.0f){
                red -= 0.05f;
            } else {
                red = 1f;
            }
            if (green > 0.0f){
                green -= 0.010f;
            } else {
                green = 1f;
            }
        }
    }

    private class LoadTask extends Timer.Task{

        @Override
        public void run() {
            isLoading = true;
            loadingMusic.stop();

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

            System.out.println();
        }
    }
}
