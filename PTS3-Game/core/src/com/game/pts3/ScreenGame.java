package com.game.pts3;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.game.classes.Character;
import com.game.classes.Player;
import com.game.classes.Terrain;
import network.Client.GameEvents;

import java.util.ArrayList;

public class ScreenGame implements Screen, InputProcessor, GameEvents {

    Stage stage;
    private Skin skin;
    private Game game;
    private AssetManager manager;
    private Preferences prefs;
    //private Player clientPlayer;
    private TiledMap tiledMap;
    private MapObjects mapObjects;
    private com.game.classes.Game gameState;
    private OrthographicCamera camera;
    private TiledMapRenderer renderer;

    private Chat chat;

    private SpriteBatch batch;
    private Sprite sprite;

    private Sound damageSound;
    private Sound errorSound;
    private Sound alarmSound;
    private float volume;

    private ShapeRenderer shapeRenderer;
    private float selectedTileX = 0;
    private float selectedTileY = 0;
    private Terrain selectedTile;
    private Character selectedCharacter;

    private boolean showMovementOptions = false;

    private float cameraZoomMin = 0.400f;
    private float cameraZoomMax = 0.125f;
    private Vector3 cameraBoundsMax;
    private Vector3 cameraBoundsMin;

    //Debug options
    private boolean showCharacter = false;

    public ScreenGame(Game game, TiledMap map, final com.game.classes.Game gameState, Chat chat, AssetManager assetManager){
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        stage = new Stage();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);
        camera.zoom = cameraZoomMin;
        camera.update();

        this.manager = assetManager;
        this.prefs = Gdx.app.getPreferences("PTS3GamePreferences");
        this.volume = prefs.getFloat("volume");
        this.gameState = gameState;
        gameState.addGameListener(this);
        this.chat = chat;
        //this.clientPlayer = clientPlayer;
        this.game = game;

        tiledMap = map;
        selectedTile = gameState.getMap().getTerrains()[0][0];
        renderer = new OrthogonalTiledMapRenderer(tiledMap);

        skin = manager.get("data/uiskin.json", Skin.class);
        damageSound = manager.get("sound/Damage.wav", Sound.class);
        errorSound = manager.get("sound/Error.wav", Sound.class);
        alarmSound = manager.get("sound/Alarm.wav", Sound.class);

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        /**
         * UI
         */
        Label lblGame = new Label("Game start", skin);
        lblGame.setPosition(10,10);
        lblGame.setSize(100,100);

        TextButton btnEndTurn = new TextButton("End Turn", skin);
        btnEndTurn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                endTurn();
            }
        });
        btnEndTurn.setPosition(10,10);
        btnEndTurn.setSize(250,20);

        stage.addActor(lblGame);
        stage.addActor(btnEndTurn);
        stage.addActor(chat.getScrollPane());
        stage.addActor(chat.getTextField());
        stage.addActor(chat.getBtnSendMessage());
        stage.getRoot().addCaptureListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if (!(event.getTarget() instanceof TextField)) stage.setKeyboardFocus(null);
                return false;
            }});

        updatePlayers();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);

        /**
         * Set Camera Start
         */
        camera.position.set(gameState.getMap().getSizeX() * gameState.getMap().getTileWidth() / 2,
                gameState.getMap().getSizeY() * gameState.getMap().getTileHeight() / 2,
                0);

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor( 0, 0, 0, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        camera.update();

        /**
         * Camera Bounds
         */
        cameraBoundsMin = new Vector3(100 * camera.zoom, 100 * camera.zoom, 0);
        cameraBoundsMax = new Vector3(gameState.getMap().getSizeX() * gameState.getMap().getTileWidth() - 100 * camera.zoom,
                gameState.getMap().getSizeY() * gameState.getMap().getTileHeight() - 100 * camera.zoom,
                0);

        /**
         * Tilemap
         */
        renderer.setView(camera);
        renderer.render();

        /**
         * Grid
         */
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

        if (showMovementOptions){
            shapeRenderer.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            for (int i = 0; i < gameState.getMap().getSizeX(); i++){
                for (int j = 0; j < gameState.getMap().getSizeY(); j++){
                    if (!selectedCharacter.canMove(gameState.getMap().getTerrains()[i][j])){
                        shapeRenderer.setColor(1,0,0,0.5f);
                        shapeRenderer.rect(gameState.getMap().getTileWidth() * i, gameState.getMap().getTileHeight() * j, gameState.getMap().getTileWidth(), gameState.getMap().getTileHeight());
                    }
                    if (selectedCharacter.canAttack(gameState.getMap().getTerrains()[i][j])){
                        shapeRenderer.setColor(0,0,1,0.5f);
                        shapeRenderer.rect(gameState.getMap().getTileWidth() * i, gameState.getMap().getTileHeight() * j, gameState.getMap().getTileWidth(), gameState.getMap().getTileHeight());
                    }

                }
            }
        }

        if (showCharacter){ //Debug
            shapeRenderer.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0,1,0,0.5f);

            for (int i = 0; i < gameState.getMap().getSizeX(); i++){
                for (int j = 0; j < gameState.getMap().getSizeY(); j++){
                    if (gameState.getMap().getTerrains()[i][j].getCharacter() != null){
                        shapeRenderer.rect(gameState.getMap().getTileWidth() * i, gameState.getMap().getTileHeight() * j, gameState.getMap().getTileWidth(), gameState.getMap().getTileHeight());
                    }
                }
            }
        }

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
                if (character.isDead()) continue;
                if (character.getSprite()!=null){
                    character.getSprite().draw(batch);
                    character.getSprite().setPosition(
                            character.getCurrentTerrain().getX() * gameState.getMap().getTileWidth(),
                            character.getCurrentTerrain().getY() * gameState.getMap().getTileHeight()
                    );
                } else {
                    updatePlayers(); // Sometimes needed to prevent invisible characters ...
                }
            }
        }
        batch.end();

        /**
         * UI
         */
        stage.act();
        stage.draw();

        moveCamera();
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

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.NUM_1)
            tiledMap.getLayers().get(0).setVisible(!tiledMap.getLayers().get(0).isVisible());
        if(keycode == Input.Keys.NUM_2)
            tiledMap.getLayers().get(1).setVisible(!tiledMap.getLayers().get(1).isVisible());
        return false;
    }

    public void moveCamera(){
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
            if (camera.position.x - 5 >= cameraBoundsMin.x){camera.translate(-5, 0);}
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
            if (camera.position.x + 5 <= cameraBoundsMax.x){camera.translate(5, 0);}
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W))
            if (camera.position.y + 5 <= cameraBoundsMax.y) {camera.translate(0, 5);}
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S))
            if (camera.position.y - 5 >= cameraBoundsMin.y) {camera.translate(0, -5);}
        if (Gdx.input.isKeyPressed(Input.Keys.PAGE_UP) && camera.zoom + 0.005f <= cameraZoomMin){
            camera.zoom += 0.005f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.PAGE_DOWN) && camera.zoom - 0.005f >= cameraZoomMax){
            camera.zoom += -0.005f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)){
            camera.rotate(2);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)){
            camera.rotate(-2);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)){
            if (showCharacter){
                showCharacter = false;
            }else
                showCharacter = true;
        }
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
        if (x < 0 || y < 0 || x > gameState.getMap().getSizeX() || y > gameState.getMap().getSizeY()){
            return false;
        }
        try {
            selectedTile = gameState.getMap().getTerrains()[x][y];
        }
        catch (ArrayIndexOutOfBoundsException ex){
            selectedTile = gameState.getMap().getTerrains()[x - 1][y];
        }
        System.out.println("Selected Tile: " + "x:" + selectedTile.getX() + " y:" + selectedTile.getY());

        if (!gameState.getClientPlayer().hasTurn()) {
            System.out.println("It is not your turn at the moment.");
            errorSound.play(volume);
            return false;
        }

        if (selectedCharacter != null){ //Do something with currently selected character
            Terrain oldTerrain = selectedCharacter.getCurrentTerrain();
            if (!gameState.moveCharacter(selectedCharacter, selectedTile, oldTerrain)){
                if (gameState.characterAttack(selectedCharacter, selectedTile.getCharacter())){
                    damageSound.play(volume);
                    chat.getTextArea().appendText("Attacked character " + selectedTile.getCharacter().getName() +
                            " for " + (selectedCharacter.getAttackPoints() - selectedTile.getCharacter().getDefensePoints()) +
                            " damage. HP " + selectedTile.getCharacter().getCurrentHealthPoints() + "/" + selectedTile.getCharacter().getMaxHealthPoints() + "\n");
                }
            } else if (selectedCharacter.getPlayer() == gameState.getClientPlayer()){
                gameState.getMap().getTerrains()[oldTerrain.getX()][oldTerrain.getY()].setCharacter(null);
                selectedTile.setCharacter(selectedCharacter);
            }

            selectedCharacter = null;
            showMovementOptions = false;
            return false;
        }

        if (selectedTile.getCharacter() != null
                && selectedCharacter == null
                && selectedTile.getCharacter().getPlayer() == gameState.getClientPlayer()){ // Select a character if nothing is selected.
            selectedCharacter = selectedTile.getCharacter();
            showMovementOptions = true;
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
        if (amount > 0){
            if (camera.zoom + 0.05f <= cameraZoomMin)
            camera.zoom += 0.05f;
        } else {
            if (camera.zoom - 0.05f >= cameraZoomMax)
            camera.zoom -= 0.05f;
        }
        return false;
    }

    @Override
    public void onGetPlayers(final ArrayList<Player> players) {
        gameState.getMap().clearTerrain();

        for (Player player:players) {
            for (Character character:player.getCharacters()) {
                if (character.isDead()) continue;
                Sprite sprite = new Sprite(manager.get(character.getSpriteTexture(), Texture.class));

                sprite.setPosition(character.getCurrentTerrain().getX()*15, character.getCurrentTerrain().getY()*15);
                gameState.getMap().getTerrains()[character.getCurrentTerrain().getX()][character.getCurrentTerrain().getY()].setCharacter(character);
                character.setSprite(sprite);
            }
        }
        gameState.setPlayers(players);

        /** Checks who has the turn **/
        for (Player player:gameState.getPlayers()) {
            if (gameState.getClientPlayer().getName().equals(player.getName())){
                gameState.setClientPlayer(player);
            }
            if (player.hasTurn()){
                chat.textArea.appendText("It's " + player.getName() + "'s turn!\n");
                if (player == gameState.getClientPlayer()){
                    alarmSound.play(volume);
                }
            }
        }
    }

    @Override
    public void onStartGame() {
        chat.getTextArea().appendText("Someone tried to start the game?\n");
        errorSound.play();
    }

    @Override
    public void onEndGame() {
        new Thread(new Runnable() { //Need to start the game on the open gl thread. so yeah..
            @Override
            public void run() {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.dispose();
                        stage.clear();
                        Gdx.app.exit();
                    }
                });
            }
        }).start();
    }

    private void updatePlayers(){
        gameState.updatePlayers();
    }

    private synchronized void endTurn(){
        showMovementOptions = false;
        gameState.endTurn();
    }
}
