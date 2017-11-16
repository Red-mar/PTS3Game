package com.game.pts3;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class ScreenEnd implements Screen {
    private Stage stage;
    private Game game;
    private com.game.classes.Game gameState;

    public ScreenEnd(final Game game, com.game.classes.Game gameState) {
        stage = new Stage();
        this.game = game;
        this.gameState = gameState;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
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
}
