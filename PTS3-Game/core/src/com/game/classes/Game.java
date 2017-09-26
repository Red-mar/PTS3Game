package com.game.classes;

import com.example.network.Client.Client;

import java.util.ArrayList;

public class Game
{
    //fields
    private ArrayList<Player> players;
    private Map map;
    private Chat chat;
    private Boolean inGame;
    private Client client;

    //constructor
    public Game(Client client)
    {
        this.client = client;
        this.client.start();
        players = new ArrayList<Player>();
        inGame = false;
    }

    //methods
    public ArrayList<Player> getPlayers()
    {
        return this.players;
    }

    public void addPlayer(Player Player)
    {
        players.add(Player);
    }

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

    public Map getMap()
    {
        return this.map;
    }

    public void setMap(Map map)
    {
        this.map = map;
    }

    public void setChat(Chat chat)
    {
        this.chat = chat;
    }

    public Chat getChat()
    {
        return this.chat;
    }

    public boolean getInGame()
    {
        return inGame;
    }

    public void setInGame(boolean inGame)
    {
        this.inGame = inGame;
    }

    public Client getClient() {
        return client;
    }
}
