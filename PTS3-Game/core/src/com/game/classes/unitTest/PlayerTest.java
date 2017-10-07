package com.game.classes.unitTest;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.game.classes.Character;
import com.game.classes.Player;
import com.game.classes.Terrain;
import com.game.classes.TerrainProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PlayerTest {
    Player p;
    @Before
    public void setUp() throws Exception {
        p = new Player("Aapje");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getName() throws Exception {
        assertEquals("Aapje", p.getName());
    }

    @Test
    public void isSpectator() throws Exception {
        assertEquals(false, p.isSpectator());
    }

    @Test
    public void setSpectator() throws Exception {
        p.setSpectator(true);
        assertEquals(true, p.isSpectator());
    }

    @Test
    public void isReady() throws Exception {
        assertEquals(false, p.isReady());
    }

    @Test
    public void setReady() throws Exception {
        p.setReady(true);
        assertEquals(true, p.isReady());
    }

    @Test
    public void getCharacters() throws Exception {
        assertNotEquals(null,p.getCharacters());
    }

    @Test
    public void setCharacters() throws Exception {
        Character c = new Character("Hans", 10,10,10,10, 1, new Sprite(), new Terrain(TerrainProperties.Normal, 1, 1), "",new Player(""));
        ArrayList<Character> list = new ArrayList<Character>();
        list.add(c);
        p.setCharacters(list);
        assertEquals("Hans", p.getCharacters().get(0).getName());
    }

    @Test
    public void isLocalPlayer() throws Exception {
        assertEquals(false, p.isLocalPlayer());
    }

    @Test
    public void setLocalPlayer() throws Exception {
        p.setLocalPlayer(true);
        assertEquals(true, p.isLocalPlayer());
    }

}