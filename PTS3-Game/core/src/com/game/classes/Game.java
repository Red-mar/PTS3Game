package com.game.classes;

import network.Client.Client;

import java.util.ArrayList;

public class Game
{
    private ArrayList<Player> players;
    private Map map;
    private Chat chat;
    private Boolean inGame;
    private Client client;

    /**
     * The game.
     * Handles everything that happens in the game.
     * @param client Requires a client.
     */
    public Game(Client client)
    {
        this.client = client;
        this.client.start();
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
    public Client getClient() {
        return client;
    }
}
