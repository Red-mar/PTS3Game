package com.game.pts3;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.game.classes.Character;
import com.game.classes.Pathfinder;
import com.game.classes.Player;
import com.game.classes.Terrain;
import com.game.classes.network.Client.Client;
import com.game.classes.network.GameEvents;

import java.util.ArrayList;

public class ScreenGame implements Screen, InputProcessor, GameEvents {

    Stage stage;
    private Skin skin;
    private Game game;
    private AssetManager manager;
    private Preferences prefs;
    private com.game.classes.Game gameState;
    private OrthographicCamera camera;
    private TiledMapRenderer renderer;

    private Chat chat;
    private boolean inChat;
    private Label lblFPS;
    private Label lblPlayers;
    private Label lblCharacter;

    //Char values

    private Window charWindow;

    private Label lblDP;
    private Label lblAP;
    private Label lblHP;
    private Label lblCharName;
    private Label lblRange;
    private Label lblMovement;
    private Sprite statSprite;
    private SpriteBatch statSb;

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
    private Pathfinder pathfinder;

    private boolean showMovementOptions = false;

    private float cameraZoomMin = 0.700f;
    private float cameraZoomMax = 0.075f;
    private Vector3 cameraBoundsMax;
    private Vector3 cameraBoundsMin;

    //Debug options
    private boolean showCharacter = false;
    private boolean showPathing = false;
    private boolean debugInfo = true;

    private ScrollPane scrollPane;

    public ScreenGame(Game game, com.game.classes.Game gameState, Chat chat, AssetManager assetManager){
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
        gameState.getPathing().setMap(gameState.getMap());
        this.chat = chat;
        inChat = false;
        this.game = game;

        selectedTile = gameState.getMap().getTerrains()[0][0];
        renderer = new OrthogonalTiledMapRenderer(gameState.getMap().getTiledMap());

        skin = manager.get("data/uiskin.json", Skin.class);
        damageSound = manager.get("sound/Damage.wav", Sound.class);
        errorSound = manager.get("sound/Error.wav", Sound.class);
        alarmSound = manager.get("sound/Alarm.wav", Sound.class);

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        pathfinder = new Pathfinder();
        pathfinder.setMap(gameState.getMap());
        /**
         * UI
         */

        TextButton btnEndTurn = new TextButton("End Turn", skin);
        btnEndTurn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                endTurn();
            }
        });
        btnEndTurn.setPosition(10,10);
        btnEndTurn.setSize(250,20);


        if (debugInfo){
            lblFPS = new Label("Fps", skin);
            lblFPS.setPosition(10,height - 20);
            lblFPS.setSize(100,20);

            lblPlayers = new Label("Characters:", skin);
            lblPlayers.setPosition(10,height - 40);
            lblPlayers.setSize(100,20);

            lblCharacter = new Label("Players", skin);
            lblCharacter.setPosition(10,height - 60);
            lblCharacter.setSize(100,20);

            scrollPane = chat.getScrollPane();

            stage.addActor(lblFPS);
            stage.addActor(lblPlayers);
            stage.addActor(lblCharacter);

            //Char stats display

            charWindow = new Window("Stats", skin);
            charWindow.setColor(Color.GRAY);
            charWindow.setPosition(width - 180, height - 80);
            charWindow.setSize(211, 180);

            //attack points label
            lblAP = new Label("ap", skin);

            //defense points label
            lblDP = new Label("dp", skin);

            //hitpoints label
            lblHP = new Label("hp", skin);

            //charname label
            lblCharName = new Label("charName", skin);

            //movement label
            lblMovement = new Label("movement", skin);

            //range label
            lblRange = new Label("range", skin);

            statSb = new SpriteBatch();
            statSprite = new Sprite();

            charWindow.add(lblCharName);
            charWindow.row();

            charWindow.add(lblHP);
            charWindow.row();

            charWindow.add(lblAP);
            charWindow.row();

            charWindow.add(lblDP);
            charWindow.row();

            charWindow.add(lblRange);
            charWindow.row();

            charWindow.add(lblMovement);
            charWindow.row();



            stage.addActor(charWindow);

        }

        stage.addActor(btnEndTurn);
        stage.addActor(chat.getScrollPane());
        stage.addActor(chat.getTextField());
        stage.addActor(chat.getBtnSendMessage());
        stage.getRoot().addCaptureListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if (!(event.getTarget() instanceof TextField)) {
                    stage.setKeyboardFocus(null);
                    inChat = false;
                }
                else{
                    inChat = true;
                }
                return false;
            }});

        stage.getRoot().addCaptureListener(new InputListener() {
             public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                 if (!(event.getTarget() instanceof ScrollPane)) {
                     stage.setScrollFocus(null);
                     inChat = false;
                 }
                 else{
                     inChat = true;
                 }
                 return false;
             }
        });

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
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor( 0, 0, 0, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        camera.update();

        if (debugInfo){
            lblFPS.setText("FPS:" + Gdx.graphics.getFramesPerSecond());
            lblPlayers.setText("Amount players: " + gameState.getPlayers().size() +
                    " Clientplayer: " + gameState.getClientPlayer().getName());
            lblCharacter.setText("Amount characters: " + gameState.getTotalCharacters());
            if(selectedCharacter != null)
            {
                charWindow.setVisible(true);

                String charName = "Unit: " + selectedCharacter.getName();
                lblCharName.setText(charName);

                String AP = Integer.toString(selectedCharacter.getAttackPoints());
                lblAP.setText("Attack points: " + AP);

                String DP = Integer.toString(selectedCharacter.getDefensePoints());
                lblDP.setText("Defense points: " + DP);

                String HP = "HP: " + Integer.toString(selectedCharacter.getCurrentHealthPoints()) + "/" + Integer.toString(selectedCharacter.getMaxHealthPoints());
                lblHP.setText(HP);

                String range = "Range: " + Integer.toString(selectedCharacter.getAttackRange());
                lblRange.setText(range);

                String movement = "Movement: " + Integer.toString(selectedCharacter.getCurrentMovementPoints()) + "/" + Integer.toString(selectedCharacter.getMovementPoints());
                lblMovement.setText(movement);


            }
            else
            {
                charWindow.setVisible(false);
            }
        }

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
        shapeRenderer.rect((float)Math.ceil((int)selectedTileX / gameState.getMap().getTileHeight()) * gameState.getMap().getTileWidth(), (float)Math.ceil((int)selectedTileY / gameState.getMap().getTileHeight()) * gameState.getMap().getTileWidth(), gameState.getMap().getTileWidth(), gameState.getMap().getTileHeight());

        if (showMovementOptions){
            shapeRenderer.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            for (int i = 0; i < gameState.getMap().getSizeX(); i++){
                for (int j = 0; j < gameState.getMap().getSizeY(); j++){
                    Terrain terrain = gameState.getMap().getTerrains()[i][j];
                    if (!selectedCharacter.canMove(terrain)){
                        shapeRenderer.setColor(1,0,0,0.5f);
                        shapeRenderer.rect(gameState.getMap().getTileWidth() * i, gameState.getMap().getTileHeight() * j, gameState.getMap().getTileWidth(), gameState.getMap().getTileHeight());
                    } else {
                        gameState.getPathing().findPath(selectedCharacter.getCurrentTerrain(), terrain);
                        if (gameState.getPathing().getPath().size() > selectedCharacter.getMovementPoints()){
                            shapeRenderer.setColor(1,0,0,0.5f);
                            shapeRenderer.rect(gameState.getMap().getTileWidth() * i, gameState.getMap().getTileHeight() * j, gameState.getMap().getTileWidth(), gameState.getMap().getTileHeight());
                        }
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

        if (showPathing){
            shapeRenderer.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0.5f,0.5f,0.5f, 0.5f);

            for (int i = 0; i < gameState.getMap().getSizeX(); i++){
                for (int j = 0; j < gameState.getMap().getSizeY(); j++){
                    if (gameState.getPathing().getPath().size() == 0) break;
                    if (gameState.getPathing().getPath().contains(gameState.getMap().getTerrains()[i][j])){
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

        if(selectedCharacter != null)
        {
            statSprite = selectedCharacter.getSprite();
            statSb.begin();
            statSb.draw(statSprite,charWindow.getX(),charWindow.getY() - charWindow.getHeight(), statSprite.getHeight() *10, statSprite.getWidth()*10);
            statSb.end();
        }
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
            gameState.getMap().getTiledMap().getLayers().get(0).setVisible(!gameState.getMap().getTiledMap().getLayers().get(0).isVisible());
        if(keycode == Input.Keys.NUM_2)
            gameState.getMap().getTiledMap().getLayers().get(1).setVisible(!gameState.getMap().getTiledMap().getLayers().get(1).isVisible());
        if (keycode == Input.Keys.NUM_4){
            showPathing = !showPathing;
        }
        if (keycode == Input.Keys.P || keycode == Input.Keys.ESCAPE){
            Screen pauseScreen = new ScreenEnd(game, gameState, manager,game.getScreen());
            game.setScreen(pauseScreen);
            pauseScreen.show();

        }

        return false;
    }

    public void moveCamera(){
        if(inChat)
            return;
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
            camera.rotate(1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)){
            camera.rotate(-1);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)){
            showCharacter = !showCharacter;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            changeFullscreen();
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
        x = (int)Math.ceil((int)selectedTileX / gameState.getMap().getTileWidth());
        y = (int)Math.ceil((int)selectedTileY / gameState.getMap().getTileHeight());
        if (x < 0 || y < 0 || x > gameState.getMap().getSizeX() || y > gameState.getMap().getSizeY()){
            return false;
        }
        try {
            selectedTile = gameState.getMap().getTerrains()[x][y];
        }
        catch (ArrayIndexOutOfBoundsException ex){ }
        gameState.getPathing().findPath(selectedTile, gameState.getMap().getTerrains()[1][1]);

        if (!gameState.getClientPlayer().hasTurn()) {
            System.out.println("It is not your turn at the moment.");
            errorSound.play(volume);
            return false;
        }
        Character tempCharacter = selectedTile.getCharacter();

        if (selectedCharacter != null){ //Do something with currently selected character
            Terrain oldTerrain = selectedCharacter.getCurrentTerrain();
            if (!gameState.moveCharacter(selectedCharacter, selectedTile, oldTerrain)){
                if (gameState.characterAttack(selectedCharacter, selectedTile.getCharacter())){
                    damageSound.play(volume);
                    chat.getTextArea().appendText("Attacked character " + tempCharacter.getName() +
                            " for " + (selectedCharacter.getAttackPoints() - tempCharacter.getDefensePoints()) +
                            " damage. HP " + tempCharacter.getCurrentHealthPoints() + "/" + tempCharacter.getMaxHealthPoints() + "\n");
                    chat.setScrollbar();
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
            manager.get("sound/wololo.wav", Sound.class).play(volume);
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
        if(inChat)
            return false;
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

                sprite.setPosition(character.getCurrentTerrain().getX()*gameState.getMap().getTileHeight(), character.getCurrentTerrain().getY()*gameState.getMap().getTileWidth());
                gameState.getMap().getTerrains()[character.getCurrentTerrain().getX()][character.getCurrentTerrain().getY()].setCharacter(character);
                character.setSprite(sprite);
            }
        }
        gameState.setPlayers(players);

        /** Checks who has the turn **/
        Player turnPlayer = gameState.checkTurn(players);
        if (turnPlayer != null){
            chat.textArea.appendText("It's " + turnPlayer.getName() + "'s turn!\n");
            chat.setScrollbar();
            if (turnPlayer == gameState.getClientPlayer()){
                alarmSound.play(volume);
            }
        }
    }

    @Override
    public void onStartGame() {
        chat.getTextArea().appendText("Someone tried to start the game?\n");
        chat.setScrollbar();
        errorSound.play();
    }

    @Override
    public void onEndGame() {
        gameState.getClient().stop();

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                chat.getTextArea().appendText("Ending the game...\n");
                chat.setScrollbar();
                manager.get("bgm/battlebase1.mp3", Music.class).stop();
                Timer timer = new Timer();
                Task task = new Task() {
                    @Override
                    public void run() {
                        game.setScreen(new ScreenLobby(game, gameState.getClientPlayer().getName(),
                                new com.game.classes.Game(new Client(gameState.getClient().getServerIP()))
                                , manager));
                        manager.get("bgm/bgmbase1.mp3", Music.class).play();
                        stage.clear();
                    }
                };
                timer.scheduleTask(task, 2f);
            }
        });
    }

    @Override
    public void onJoinGame(){
        chat.getTextArea().appendText("Someone joined the game!\n");
        chat.setScrollbar();
        alarmSound.play();
    }

    @Override
    public void onUpdateCharacter(int x, int y, String charName, String playerName) {
        gameState.forceMoveCharacter(x,y,charName,playerName);
    }

    private void updatePlayers(){
        if (gameState.getClient().isConnected() == null) {
            ArrayList<Player> clientplayer = new ArrayList<Player>();
            clientplayer.add(gameState.getClientPlayer());
            onGetPlayers(clientplayer);
            return;
        }
        gameState.updatePlayers();
    }

    private synchronized void endTurn(){
        if (!gameState.getClientPlayer().hasTurn()){
            chat.getTextArea().appendText("Je bent niet aan de beurt.\n");
            chat.setScrollbar();
            return;
        }
        showMovementOptions = false;
        if (gameState.getClient().isConnected() == null){
            gameState.endTurnLocal();
            alarmSound.play(volume);
            return;
        }
        gameState.endTurn();
    }

    private void changeFullscreen(){ //TODO zet ergens anders neer
        if (Gdx.graphics.isFullscreen()){
            Gdx.graphics.setWindowedMode(1280,720);
        } else {
            Graphics.Monitor currMonitor = Gdx.graphics.getMonitor();
            Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode(currMonitor);
            if (!Gdx.graphics.setFullscreenMode(displayMode)){
                System.out.println("Could not enter fullscreen mode.");
            }
        }
    }

}
