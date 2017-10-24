package com.game.classes;

import com.game.classes.Chat;
import com.game.classes.Game;
import com.game.classes.Map;
import com.game.classes.Player;
import com.game.classes.network.Client.Client;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {

    //fields
    Game instance;
    Client client;


    @Before
    public void setUp() throws Exception
    {
        client = new Client("gameTest");
        instance = new Game(client);
    }

    @After
    public void tearDown() throws Exception
    {

    }

    @Test
    public void getPlayers() throws Exception
    {
        assertNotEquals(null, instance);
    }

    @Test
    public void addPlayer() throws Exception
    {
        Player testPlayer = new Player("test");
        instance.addPlayer(testPlayer);
        assertEquals(1, instance.getPlayers().size());
    }

    @Test
    public void removePlayer() throws Exception
    {
        Player testPlayer = new Player("test");
        instance.addPlayer(testPlayer);
        instance.removePlayer(testPlayer);
        assertEquals(0, instance.getPlayers().size());
    }

    @Test
    public void getMap() throws Exception
    {
        Map expected = new Map(10,10, 0, 0, null);
        instance.setMap(expected);
        Map result = instance.getMap();
        assertEquals(expected, result);
    }

    @Test
    public void setMap() throws Exception
    {
        assertEquals(null, instance.getMap());
        Map toAdd = new Map(10,10, 0, 0, null);
        instance.setMap(toAdd);
        assertNotNull(instance.getMap());
    }

    @Test
    public void setChat() throws Exception
    {
        Chat chat = new Chat(client);
        assertEquals(null,instance.getChat());
        instance.setChat(chat);
        assertNotNull(instance.getChat());
    }

    @Test
    public void getChat() throws Exception
    {
        Chat chat = new Chat(client);
        assertEquals(null, instance.getChat());
        instance.setChat(chat);
        assertNotNull(instance.getChat());
    }

    @Test
    public void getInGame() throws Exception
    {
        assertEquals(false, instance.getInGame());
        instance.setInGame(true);
        assertEquals(true, instance.getInGame());
    }

    @Test
    public void setInGame() throws Exception
    {
        assertEquals(false, instance.getInGame());
        instance.setInGame(true);
        assertEquals(true, instance.getInGame());
    }

}