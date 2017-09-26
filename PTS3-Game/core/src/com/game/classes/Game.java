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
        for (Player current:players)
        {
            if(current.equals(player))
            {
                players.remove(current);
            }
        }
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

    public boolean getInGame()
    {
        return getInGame();
    }

    public void setInGame(boolean inGame)
    {
        this.inGame = inGame;
    }

    public Client getClient() {
        return client;
    }
}
