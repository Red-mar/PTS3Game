package com.game.pts3;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.game.classes.Player;
import com.game.classes.network.GameEvents;

import java.util.ArrayList;

public class ScreenEnd implements Screen {
    private Stage stage;
    private Game game;
    private com.game.classes.Game gameState;
    private Screen lastScreen;
    private GameEvents lastScreenEvents;

    public ScreenEnd(final Game game, com.game.classes.Game gameState, AssetManager manager, Screen lastScreen) {
        stage = new Stage();
        this.game = game;
        this.gameState = gameState;
        this.lastScreen = lastScreen;
        Skin skin = manager.get("data/uiskin.json", Skin.class);

        TextButton endGameButton = new TextButton("Exit the game", skin);
        endGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                endGame();
            }
        });
        endGameButton.setPosition((Gdx.graphics.getWidth() / 2f) - (250 / 2), (Gdx.graphics.getHeight() / 5f) * 2);
        endGameButton.setSize(250, 50);

        TextButton returnButton = new TextButton("Return to the game", skin);
        returnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                returnGame();
            }
        });
        returnButton.setPosition((Gdx.graphics.getWidth() / 2f) - (250 / 2), (Gdx.graphics.getHeight() / 5f));
        returnButton.setSize(250, 50);

        stage.addActor(endGameButton);
        stage.addActor(returnButton);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0.343f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            returnGame();
        }

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
