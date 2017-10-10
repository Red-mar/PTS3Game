package com.game.classes;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import network.Client.Client;
import network.Client.GameEvents;
import network.Server.Server;

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
    }

    public Game(Server server){
        this.server = server;
        //this.server.start();
        players = new ArrayList<Player>();
        inGame = false;
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

    public Player getClientPlayer() {
        return clientPlayer;
    }

    public void setClientPlayer(Player clientPlayer) {
        this.clientPlayer = clientPlayer;
    }

    public void establishConnection(String name, com.game.pts3.Chat chat){
        try {


            if (!getClient().isConnected()) {
                getClient().start();
                while (!getClient().isConnected()) {
                } //TODO betere oplossing
                getClient().addListener(chat);
                getClient().sendMessageSetName(name);
                Thread.sleep(100);
                getClient().sendMessageGetPlayers();
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }
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
        for (int i = 0; i < 15; i ++){
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
        getClient().sendGameMessagePlayer(clientPlayer);
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

    private void updateClientPlayer(){
        for (Player player:getPlayers()) {
            if (clientPlayer.getName().equals(player.getName())){
                clientPlayer = player;
            }
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
        if (character.setCurrentTerrain(tile)){
            map.getTerrains()[oldTile.getX()][oldTile.getY()].setCharacter(null);
            tile.setCharacter(character);

            return true;
        }
        return false;
    }

    public void addGameListener(GameEvents listener){
        client.addGameListener(listener);
    }
}
