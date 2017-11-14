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

import java.util.ArrayList;
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

        if (temp == null){
            return;
        }
        players.remove(temp);
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
        }
        String textureFile;
        Character character;
        Texture texture;
        Sprite sprite;
        int[] ints = new Random().ints(0,60).distinct().limit(15).toArray();
        for (int i = 0; i < 5; i ++){
            int x = ints[i] % 20 + 10;
            int y = enemy == 1 ? ints[i] / 20 : ints[i] / 20 + 37;
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

    public void loadMap(String fileName){ //TODO zet deel in map class
        TiledMap tiledMap = new TmxMapLoader().load(fileName);

        TiledMapTileLayer tileLayer = (TiledMapTileLayer)tiledMap.getLayers().get(0);
        int tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        int tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);

        // Add objects
        MapLayer mapLayer = tiledMap.getLayers().get("Impassable Terrain");
        MapObjects mapObjects = mapLayer.getObjects();
        ArrayList<RectangleMapObject> mapObjectList = new ArrayList<RectangleMapObject>();
        for (int i = 0; i < mapObjects.getCount(); i++){
            RectangleMapObject rmo = (RectangleMapObject) mapObjects.get(i);
            rmo.getRectangle().setX((rmo.getRectangle().x ) / tileWidth );
            rmo.getRectangle().setY((rmo.getRectangle().y ) / tileHeight);
            mapObjectList.add(rmo);
        }

        map = new Map(tileLayer.getWidth(), tileLayer.getHeight(), tileHeight, tileWidth, mapObjectList);
        map.setTiledMap(tiledMap);

        if (getClient().isConnected() != null){
            getClient().sendGameMap(map);
        }
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
        pathing.findPath(oldTile, tile);
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
}
