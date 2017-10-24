package com.game.classes;

import com.game.classes.network.Client.Client;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class ChatTest {

    Client client;
    Game game;
    Chat chat;

    @Before
    public void setUp() throws Exception
    {
        client = new Client("gameTest");
        game = new Game(client);
        chat = new Chat(client);
        ArrayList<String> chatlog = new ArrayList<String>();
        chatlog.add("Test");
        chat.setChatlog(chatlog);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getGame() throws Exception
    {
        assertNotEquals(null, game);
    }

    @Test
    public void setGame() throws Exception
    {
        chat.setGame(game);
        assertNotNull(chat.getGame());
    }

    @Test
    public void getChatlog() throws Exception
    {
        ArrayList<String> instance = chat.getChatlog();
        assertNotNull(instance);
    }

    @Test
    public void setChatlog() throws Exception
    {
        ArrayList<String> instance = new ArrayList<String>();
        instance.add("Test");
        chat.setChatlog(instance);
        assertNotNull(chat.getChatlog());
    }

}