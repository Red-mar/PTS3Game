package com.game.classes.network.Server;

import com.game.classes.Game;
import com.game.classes.network.Client.Client;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class ServerTest {
    private Server server;
    private Client client;
    private Game serverGame;
    private Game clientGame;
/*
    @Before
    public void setUp() throws Exception {
        String port = "";

        server = new Server(4321, port);
        client = new Client(port);

        serverGame = new Game(server);
        clientGame = new Game(client);

        server.start();
        client.start();



        clientGame.establishConnection("red");
    }

    @After
    public void tearDown() throws Exception {
        if (server != null) {
            server.stop();
            server = null;
            try {
                Thread.sleep(5000);
            }
            catch (InterruptedException e) {
                // do nothing.
            }
        }
    }

    @Test
    public void start() throws Exception {

        assertEquals(true, client.isConnected());
    }

    @Test
    public void stop() throws Exception {

    }

    @Test
    public void sendMessageAll() throws Exception {
    }

    @Test
    public void sendMessageWhisper() throws Exception {
        //server.sendMessageWhisper("test1", "test2", "message");
        assertEquals(true,true);
    }

    @Test
    public void sendGameMessagePlayers() throws Exception {
        //server.sendGameMessagePlayers();
    }

    @Test
    public void sendGameStart() throws Exception {
        //server.sendGameStart();
        assertEquals(true,true);
    }

    @Test
    public void sendGameEnd() throws Exception {
        //server.sendGameEnd();
        assertEquals(true,true);
    }

    @Test
    public void sendCharacter() throws Exception {
        //server.sendCharacter(0,0, "testChar", "testPlayer");
        assertEquals(true,true);
    }*/

}