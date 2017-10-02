package com.game.pts3;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.game.classes.Character;

public class ScreenGame implements Screen {

    Stage stage;
    private Skin skin;
    private Game game;
    private Texture texture;
    private TiledMap tiledMap;
    private OrthographicCamera camera;
    private TiledMapRenderer renderer;



    public ScreenGame(Game game){
        stage = new Stage();
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);
        camera.update();
        tiledMap = new TmxMapLoader().load("map.tmx");
        renderer = new OrthogonalTiledMapRenderer(tiledMap);
        this.game = game;
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        Label lblGame = new Label("Game start", skin);
        lblGame.setPosition(10,10);
        lblGame.setSize(100,100);

        stage.addActor(lblGame);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor( 0, 1, 0, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
        camera.update();
        renderer.setView(camera);
        renderer.render();

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
