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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.game.classes.Character;
import com.game.classes.Player;
import com.game.classes.Terrain;

import java.util.ArrayList;

public class ScreenGame implements Screen, InputProcessor {

    Stage stage;
    private Skin skin;
    private Game game;
    private TiledMap tiledMap;
    private com.game.classes.Game gameState;
    private OrthographicCamera camera;
    private TiledMapRenderer renderer;

    private SpriteBatch batch;
    private Sprite sprite;
    private Texture texture;
    private ShapeRenderer shapeRenderer;
    private float selectedTileX = 0;
    private float selectedTileY = 0;
    private Terrain selectedTile;
    private Character selectedCharacter;

    public ScreenGame(Game game, TiledMap map, com.game.classes.Game gameState){
        stage = new Stage();
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);
        camera.update();
        tiledMap = map;
        this.gameState = gameState;
        selectedTile = gameState.getMap().getTerrains()[0][0];
        renderer = new OrthogonalTiledMapRenderer(tiledMap);
        this.game = game;
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        texture = new Texture(Gdx.files.internal("Sprites/swordsman-1.png"));
        sprite = new Sprite(texture);

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

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.setColor(0, 0, 0, 0.2f);
        for (int i = 0; i < gameState.getMap().getSizeX(); i++){
            for (int j = 0; j < gameState.getMap().getSizeY(); j++){
                shapeRenderer.rect(gameState.getMap().getTileWidth() * i, gameState.getMap().getTileHeight() * j, gameState.getMap().getTileWidth(), gameState.getMap().getTileHeight());
            }
        }
        shapeRenderer.setColor(1, 0, 0, 1f);
        shapeRenderer.rect((float)Math.ceil((int)selectedTileX / 15) * 15, (float)Math.ceil((int)selectedTileY / 15) * 15, gameState.getMap().getTileWidth(), gameState.getMap().getTileHeight());
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        /**
         * Character
         */
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        for (Player player: gameState.getPlayers()) {
            ArrayList<Character> characters = player.getCharacters();
            for (Character character:characters) {
                character.getSprite().draw(batch);
                character.getSprite().setPosition(
                        character.getCurrentTerrain().getX() * gameState.getMap().getTileWidth(),
                        character.getCurrentTerrain().getY() * gameState.getMap().getTileHeight()
                );
            }
        }
        batch.end();

        stage.act();
        stage.draw();

        moveCamera();
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
        if(keycode == Input.Keys.NUM_1)
            tiledMap.getLayers().get(0).setVisible(!tiledMap.getLayers().get(0).isVisible());
        if(keycode == Input.Keys.NUM_2)
            tiledMap.getLayers().get(1).setVisible(!tiledMap.getLayers().get(1).isVisible());
        return false;
    }

    public void moveCamera(){
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            camera.translate(-5, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            camera.translate(5, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            camera.translate(0, 5);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            camera.translate(0, -5);

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
        Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
        camera.unproject(worldCoordinates);
        selectedTileX = worldCoordinates.x;
        selectedTileY = worldCoordinates.y;

        int x, y;
        x = (int)Math.ceil((int)selectedTileX / 15);
        y = (int)Math.ceil((int)selectedTileY / 15);
        selectedTile = gameState.getMap().getTerrains()[x][y];
        System.out.println("Selected Tile: " + "x:" + selectedTile.getX() + " y:" + selectedTile.getY());

        if (selectedTile.getCharacter() != null && selectedCharacter == null){
            selectedCharacter = selectedTile.getCharacter();
        }
        if (selectedCharacter != null){
            if (!selectedCharacter.setCurrentTerrain(selectedTile)){
                selectedCharacter = null;
            } else {
                selectedTile.setCharacter(selectedCharacter);
            }
        }


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
