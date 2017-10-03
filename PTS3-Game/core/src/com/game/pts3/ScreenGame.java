package com.game.pts3;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.game.classes.Character;

public class ScreenGame implements Screen, InputProcessor {

    Stage stage;
    private Skin skin;
    private Game game;
    private Texture texture;
    private TiledMap tiledMap;
    private com.game.classes.Game gameState;
    private OrthographicCamera camera;
    private TiledMapRenderer renderer;

    private SpriteBatch batch;
    private Sprite sprite;
    private ShapeRenderer shapeRenderer;
    private float selectedTileX = 0;
    private float selectedTileY = 0;

    public ScreenGame(Game game, TiledMap map, com.game.classes.Game gameState){
        stage = new Stage();
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);
        camera.update();
        tiledMap = map;
        this.gameState = gameState;
        renderer = new OrthogonalTiledMapRenderer(tiledMap);
        this.game = game;
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        /**
         * UI
         */
        Label lblGame = new Label("Game start", skin);
        lblGame.setPosition(10,10);
        lblGame.setSize(100,100);

        stage.addActor(lblGame);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void show() {
        //Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor( 0, 0, 0, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
        camera.update();

        /**
         * Tilemap
         */
        renderer.setView(camera);
        renderer.render();

        
        /**
         * other stuff
         */
        batch.setProjectionMatrix(camera.combined);
        //batch.begin();
        //batch.draw(sprite, 200,200,64,64);
        /**
         * Grid
         */

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0,0,0,1);
        for (int i = 0; i < gameState.getMap().getSizeX(); i++){
            for (int j = 0; j < gameState.getMap().getSizeY(); j++){
                tiledMap.getLayers().get(1);
                shapeRenderer.rect(gameState.getMap().getTileWidth() * i, gameState.getMap().getTileHeight() * j, gameState.getMap().getTileWidth(), gameState.getMap().getTileHeight());
            }
        }
        /**
         * Selection Rectangle
         */
        //shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        //shapeRenderer.setColor(1, 1, 1, 1);
        //shapeRenderer.rect(selectedTileX, selectedTileY, 15, 15); //x,y of specific tile
        shapeRenderer.end();
        //batch.end();

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

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.LEFT)
            camera.translate(-32,0);
        if(keycode == Input.Keys.RIGHT)
            camera.translate(32,0);
        if(keycode == Input.Keys.UP)
            camera.translate(0,32);
        if(keycode == Input.Keys.DOWN)
            camera.translate(0,-32);
        if(keycode == Input.Keys.NUM_1)
            tiledMap.getLayers().get(0).setVisible(!tiledMap.getLayers().get(0).isVisible());
        if(keycode == Input.Keys.NUM_2)
            tiledMap.getLayers().get(1).setVisible(!tiledMap.getLayers().get(1).isVisible());
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        selectedTileX = screenX;
        selectedTileY = 480 - screenY; // screen height - y
        System.out.println("Clickered." + screenX + ":" + screenY);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
