package com.game.classes.unitTest;

import com.game.classes.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MapTest {
    Map map;

    @Before
    public void setUp() throws Exception
    {
        map = new Map(10, 20);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getSizeX() throws Exception
    {
        assertEquals(10, map.getSizeX());
    }

    @Test
    public void setSizeX() throws Exception
    {
        map.setSizeX(15);
        assertEquals(15, map.getSizeX());
    }

    @Test
    public void getSizeY() throws Exception
    {
        assertEquals(20, map.getSizeY());
    }

    @Test
    public void setSizeY() throws Exception
    {
        map.setSizeY(25);
        assertEquals(25, map.getSizeY());
    }

    @Test
    public void getTerrain() throws Exception {
    }

    @Test
    public void setTerrain() throws Exception {
    }

    public static class ChatTest {
        @Before
        public void setUp() throws Exception {
        }

        @After
        public void tearDown() throws Exception {
        }

        @Test
        public void getGame() throws Exception {
        }

        @Test
        public void setGame() throws Exception {
        }

        @Test
        public void getChatlog() throws Exception {
        }

        @Test
        public void setChatlog() throws Exception {
        }

        @Test
        public void sendMessageAll() throws Exception {
        }

        @Test
        public void sendMessageWhisper() throws Exception {
        }

    }
}