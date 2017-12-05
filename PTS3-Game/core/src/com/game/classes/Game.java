package com.game.classes;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.game.classes.network.Client.Client;
import com.game.classes.pathing.aStarPathing;
import com.game.classes.network.GameEvents;
import com.game.classes.network.Server.Server;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Game
{
    private ArrayList<Player> players;
    private Map map;
    private Chat chat;
    private Boolean inGame;
    private Client client;
    private Server server;
    private Player clientPlayer;

    private aStarPathing pathing;

    private ArrayList<RectangleMapObject> spawnBlueList;
    private ArrayList<RectangleMapObject> spawnRedList;

    /**
     * The game.
     * Handles everything that happens in the game.
     * @param client Requires a client.
     */
    public Game(Client client)
    {
        this.client = client;
        players = new ArrayList<Player>();
        inGame = false;
        pathing = new aStarPathing(getMap());
    }


    public Game(Server server){
        this.server = server;
        //this.server.start();
        players = new ArrayList<Player>();
        inGame = false;
        pathing = new aStarPathing(getMap());
    }

    /**
     * Gets all the players currently in the game.
     * @return Returns an array list of players.
     */
    public ArrayList<Player> getPlayers()
    {
        return this.players;
    }

    public void setPlayers(ArrayList<Player> players){
        this.players = players;
    }

    /**
     * Add a player to the game.
     * @param Player The player to add to the game.
     */
    public void addPlayer(Player Player)
    {
        players.add(Player);
    }

    /**
     * Remove a player from the game.
     * @param player The player to remove from the game.
     */
    public void removePlayer(Player player)
    {
        Player temp = player;
        for (Player current:players)
        {
            if(current.equals(player))
            {
                temp = current;
            }
        }
        players.remove(temp);
    }

    /**
     * If a player needs to be removed without having a player object
     * @param name of the player to remove
     */
    public void removePlayer(String name){
        Player temp = null;
        for (Player current:players)
        {
            if(current.getName().equals(name))
            {
                temp = current;
            }
        }

        if (temp != null){
            players.remove(temp);
        }

    }

    /**
     * Gets the current map that the game is using.
     * @return A map object.
     */
    public Map getMap()
    {
        return this.map;
    }

    /**
     * Sets the map that the game should use.
     * (cannot be used while the game is running.)
     * @param map The map the game should use.
     */
    public void setMap(Map map)
    {
        this.map = map;
    }

    /**
     * Adds a chat to the game.
     * @param chat The chat that should be added to the game.
     */
    public void setChat(Chat chat)
    {
        this.chat = chat;
    }

    /**
     * Get the chat that is added to the game.
     * @return The chat that is added to the game.
     */
    public Chat getChat()
    {
        return this.chat;
    }

    /**
     * Checks whether the game is running.
     * @return True if the game is running, false if not.
     */
    public boolean getInGame()
    {
        return inGame;
    }

    /**
     * Sets the status of the game.
     * @param inGame Set true if the game is running, false if not.
     */
    public void setInGame(boolean inGame)
    {
        this.inGame = inGame;
    }

    /**
     * Get the connection with the server.
     * @return Returns a client object.
     */
    public synchronized Client getClient() {
        return client;
    }

    /**
     * get the Hosting player of the game (client player)
     * @return Returns a Player Object Host
     */
    public Player getClientPlayer() {
        return clientPlayer;
    }

    /**
     *
     * @param clientPlayer
     */
    public void setClientPlayer(Player clientPlayer) {
        this.clientPlayer = clientPlayer;
    }

    public aStarPathing getPathing() {
        return pathing;
    }

    public boolean establishConnection(String name){
        try {
            if (client.isConnected() == null) {
                client.start();
                while (true) {
                    Thread.sleep(10);
                    if (client.isConnected() != null){
                        if (client.isConnected()){
                            break;
                        }
                        client.setConnected(null);
                        return false;
                    }
                } //TODO betere oplossing
                getClient().sendMessageSetName(name);
                getClient().sendMessageGetPlayers();
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Generates characters for the clientplayer
     * @param name
     * @param manager
     */
    public void generateCharacters(String name, AssetManager manager){
        updateClientPlayer();
        int enemy = 1;
        for (int i = 0; i < players.size(); i++){
            if (players.get(i).getName() == clientPlayer.getName()){
                enemy = i + 1;
            }
            if (enemy >= 3) return;
        }
        String textureFile;
        Character character;
        Texture texture;
        Sprite sprite;
        for (int i = 0; i < 5; i ++){
            int x = 0;
            int y = 0;
            //Player Blue
            if (enemy == 1){
                x = (int) spawnBlueList.get(i).getRectangle().x;
                y = (int) spawnBlueList.get(i).getRectangle().y;
            }
            //Player Red
            else {
                x = (int) spawnRedList.get(i).getRectangle().x;
                y = (int) spawnRedList.get(i).getRectangle().y;
            }
            System.out.println(x + " "+ y);
            Terrain terrain = getMap().getTerrains()[x][y];

            switch (i){
                case 1:
                    textureFile = "Sprites/bowman-" + enemy + ".png";
                    texture = manager.get(textureFile, Texture.class);
                    sprite = new Sprite(texture);
                    character = new Character("Bowman", 8, 4, 0, 6, 3, sprite, terrain,textureFile,clientPlayer);
                    break;
                case 2:
                    textureFile = "Sprites/heavy-" + enemy + ".png";
                    texture = manager.get(textureFile, Texture.class);
                    sprite = new Sprite(texture);
                    character = new Character("Heavy Dude", 10, 3, 2, 4, 1, sprite, terrain,textureFile,clientPlayer);
                    break;
                case 3:
                    textureFile = "Sprites/horseman-" + enemy + ".png";
                    texture = manager.get(textureFile, Texture.class);
                    sprite = new Sprite(texture);
                    character = new Character("Man with donkey", 8, 4, 0, 10, 1, sprite, terrain,textureFile,clientPlayer);
                    break;
                case 4:
                    textureFile = "Sprites/wizard-" + enemy + ".png";
                    texture = manager.get(textureFile, Texture.class);
                    sprite = new Sprite(texture);
                    character = new Character("Merrrlijn", 7, 5, 0, 5, 2, sprite, terrain,textureFile,clientPlayer);
                    break;
                default:
                    textureFile = "Sprites/swordsman-" + enemy + ".png";
                    texture = manager.get(textureFile, Texture.class);
                    sprite = new Sprite(texture);
                    character = new Character("Zwaardvechter", 10, 4, 1, 6, 1, sprite, terrain,textureFile,clientPlayer);
                    break;
            }

            clientPlayer.addCharacter(character);
        }
        if (client.isConnected() == null) return;
        getClient().sendGameMessagePlayer(clientPlayer);
    }

    public boolean loadMap(String fileName){ //TODO zet deel in map class
        TiledMap tiledMap = null;
        try {
            tiledMap = new TmxMapLoader().load(fileName);
        } catch (Exception e){
            return false;
        }

        TiledMapTileLayer tileLayer = (TiledMapTileLayer)tiledMap.getLayers().get(0);
        int tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        int tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);

        // Add Impassable Terrain
        MapLayer mapLayer = tiledMap.getLayers().get("Impassable Terrain");
        if (mapLayer != null){
            MapObjects mapObjects = mapLayer.getObjects();
            ArrayList<RectangleMapObject> mapObjectList = new ArrayList<RectangleMapObject>();
            for (int i = 0; i < mapObjects.getCount(); i++){
                RectangleMapObject rmo = (RectangleMapObject) mapObjects.get(i);
                fixRectangleObject(rmo, tileWidth, tileHeight);
                mapObjectList.add(rmo);
            }
            map = new Map(tileLayer.getWidth(), tileLayer.getHeight(), tileHeight, tileWidth, mapObjectList);
        } else {
            map = new Map(tileLayer.getWidth(), tileLayer.getHeight(), tileHeight, tileWidth, null);
        }

        //Add Spawn Positions
        MapLayer spawnBlue = tiledMap.getLayers().get("SpawnBlue");
        if (spawnBlue != null){
            spawnBlueList = getSpawnFromTiledMap(spawnBlue, tileWidth, tileHeight);
            Collections.shuffle(spawnBlueList);
        }

        MapLayer spawnRed = tiledMap.getLayers().get("SpawnRed");
        if (spawnRed != null){
            spawnRedList = getSpawnFromTiledMap(spawnRed, tileWidth, tileHeight);
            Collections.shuffle(spawnRedList);
        }


        map.setTiledMap(tiledMap);

        if (getClient().isConnected() != null){
            getClient().sendGameMap(map);
        }
        return true;
    }

    private ArrayList<RectangleMapObject> getSpawnFromTiledMap(MapLayer mapLayer, int tileWidth, int tileHeight){
        MapObjects spawnPos = mapLayer.getObjects();
        ArrayList<RectangleMapObject> spawnPosList = new ArrayList<RectangleMapObject>();
        for (int i = 0; i < spawnPos.getCount(); i ++){
            RectangleMapObject rmo = (RectangleMapObject) spawnPos.get(i);
            fixRectangleObject(rmo, tileWidth, tileHeight);
            spawnPosList.add(rmo);
        }
        return spawnPosList;
    }

    private void fixRectangleObject(RectangleMapObject rmo, int tileWidth, int tileHeight){
        rmo.getRectangle().setX((rmo.getRectangle().x ) / tileWidth );
        rmo.getRectangle().setY((rmo.getRectangle().y ) / tileHeight);
    }

    /**
     * Sends a message to the server to send everyone new players
     */
    public void updatePlayers(){
        updateClientPlayer();
        getClient().sendGameMessagePlayer(clientPlayer);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getClient().sendMessageGetPlayers();
    }

    /**
     * Sends an end turn message to the server
     */
    public void endTurn(){
        updateClientPlayer();

        for (Player player : getPlayers()){
            getClient().sendGameMessagePlayer(player);

            try {
                Thread.sleep(100); // Server crashes if it gets too many messages...
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        getClient().sendGameEndTurn();
    }

    public void endTurnLocal() {
        clientPlayer.setHasTurn(true);
        for (Character character : clientPlayer.getCharacters()) {
            character.setCurrentMovementPoints(character.getMovementPoints());
            character.setHasAttacked(false);
        }
    }

    public boolean characterAttack(Character attacker, Character defender){
        if (defender == null) return false;
        if (!attacker.hasAttacked() && attacker.canAttack(defender.getCurrentTerrain())){
            defender.takeDamage(attacker.getAttackPoints());
            attacker.setHasAttacked(true);
            if (defender.isDead()){
                defender.getCurrentTerrain().setCharacter(null);
            }
            return true;
        }
        return false;
    }

    public boolean moveCharacter(Character character, Terrain tile, Terrain oldTile){
        if (tile.getY() == oldTile.getY() && tile.getX() == oldTile.getX()){
            return false;
        }
        else {
            pathing.findPath(oldTile, tile);
        }
        if (pathing.getPath().size() > character.getMovementPoints()){
            return false;
        }

        if (character.setCurrentTerrain(tile, pathing.getPath().size())){
            map.getTerrains()
                    [oldTile.getX()]
                    [oldTile.getY()].setCharacter(null);
            tile.setCharacter(character);

            if (client.isConnected() != null){
                client.sendCharacterMove(
                        tile.getX(),
                        tile.getY(),
                        character.getName(),
                        character.getPlayer().getName());
            }
            return true;
        }
        return false;
    }

    public void forceMoveCharacter(int x, int y, String charName, String playerName){
        for (Player player : players) {
            if (player.getName().equals(playerName)){
                for (Character character : player.getCharacters()) {
                    if (character.getName().equals(charName)){
                        character.forceSetCurrentTerrain(map.getTerrains()[x][y]);
                    }
                }
            }
        }
    }

    public void addGameListener(GameEvents listener){
        client.addGameListener(listener);
    }

    public Player checkTurn(ArrayList<Player> players){
        for (Player player:players) {
            if (clientPlayer.getName().equals(player.getName())){
                clientPlayer = player;
            }
            if (player.hasTurn()){
                return player;
            }
        }
        return null;
    }

    public int getTotalCharacters(){
        int count = 0;
        for (Player player : players) {
            for (Character character : player.getCharacters()) {
                if (!character.isDead()){
                    count++;
                }
            }
        }
        return count;
    }

    private void updateClientPlayer(){
        for (Player player:getPlayers()) {
            if (clientPlayer.getName().equals(player.getName())){
                clientPlayer = player;
            }
        }
    }

    public ArrayList<String> getMapFiles() {
        File folder = new File("map");
        File[] listOfFiles = folder.listFiles();

        ArrayList<String> filenames = new ArrayList<String>();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && listOfFiles[i].getName().contains(".tmx")) {
                filenames.add(listOfFiles[i].getName());
            }
        }
        return filenames;
    }
}
