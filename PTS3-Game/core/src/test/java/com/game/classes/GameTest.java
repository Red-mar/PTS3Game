package com.game.classes;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.game.classes.Chat;
import com.game.classes.Game;
import com.game.classes.Map;
import com.game.classes.Player;
import com.game.classes.network.Client.Client;
import com.game.classes.network.Server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameTest {
    //fields
    Game instance;
    Game game;
    Client client;
    Server server;


    @Before
    public void setUp() throws Exception
    {
        //server = new Server(82, "ip");
        client = new Client("gameTest");
        instance = new Game(client);
        game = new Game(server);
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
    public void setPlayers() throws Exception {
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(new Player("test1"));
        players.add(new Player("test2"));
        instance.setPlayers(players);
        assertArrayEquals(players.toArray(), instance.getPlayers().toArray());
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

        instance.addPlayer(testPlayer);
        instance.removePlayer("fail");
        assertEquals(1, instance.getPlayers().size());

        instance.removePlayer("test");
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
    public void getClient() throws Exception {
        assertEquals(client, instance.getClient());
    }


    @Test
    public void setInGame() throws Exception
    {
        assertEquals(false, instance.getInGame());
        instance.setInGame(true);
        assertEquals(true, instance.getInGame());
    }

    @Test
    public void establishConnection() throws Exception
    {
        instance.establishConnection("test");
    }

    @Test
    public void generateCharacters() throws Exception
    {
        /*
        Map toAdd = new Map(40,40, 10, 10, null);
        instance.setMap(toAdd);
        AssetManager manager = new AssetManager();
        manager.load("Sprites/bowman-1.png", Texture.class);
        manager.load("Sprites/bowman-2.png", Texture.class);
        manager.load("Sprites/heavy-1.png", Texture.class);
        manager.load("Sprites/heavy-2.png", Texture.class);
        manager.load("Sprites/horseman-1.png", Texture.class);
        manager.load("Sprites/horseman-2.png", Texture.class);
        manager.load("Sprites/swordsman-1.png", Texture.class);
        manager.load("Sprites/swordsman-2.png", Texture.class);
        manager.load("Sprites/wizard-1.png", Texture.class);
        manager.load("Sprites/wizard-2.png", Texture.class);
        instance.generateCharacters("test", manager);
        assertEquals(5, instance.getClientPlayer().getCharacters().size());
    */}


    @Test
    public void loadMap() throws Exception{/*
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));
        instance.loadMap("core/assets/map_2.tmx");
    */}

    @Test
    public void moveCharacter() throws Exception
    {

    }

    @Test
    public void forceMoveCharacter() throws Exception
    {
        instance.addPlayer(new Player("test"));
    }

    @Test
    public void addGameListner() throws Exception{

    }

    @Test
    public void checkTurn() throws Exception{
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(new Player("1"));
        players.add(new Player("2"));
        instance.setClientPlayer(players.get(0));
        instance.endTurnLocal();

        assertEquals(players.get(0), instance.checkTurn(players));
    }

    @Test
    public void getTotalCharacters() throws Exception{
        instance.addPlayer(new Player("test"));
        assertEquals(0, instance.getTotalCharacters());
    }

    @Test
    public void getMapFiles() throws Exception {
        ArrayList<String> filenames = new ArrayList<String>();
    }

    @Test
    public void clientPlayer() throws Exception {
        Player p = new Player("test");
        instance.setClientPlayer(p);
        assertEquals(instance.getClientPlayer(), p);
    }


}