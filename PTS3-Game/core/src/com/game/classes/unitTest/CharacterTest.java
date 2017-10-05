package com.game.classes.unitTest;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.game.classes.Character;
import com.game.classes.Player;
import com.game.classes.Terrain;
import com.game.classes.TerrainProperties;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterTest {
    Character c = new Character("", 10, 20, 30, 40, new Sprite(), new Terrain(TerrainProperties.Normal, 1, 1), "", new Player(""));

    @Test
    public void getName() throws Exception
    {
        assertEquals("", c.getName());
    }

    @Test
    public void setName() throws Exception
    {
        c.setName("test2");
        assertEquals("test2", c.getName());
    }

    @Test
    public void getMaxHealthPoints() throws Exception
    {
        assertEquals(10, c.getMaxHealthPoints());
    }

    @Test
    public void setMaxHealthPoints() throws Exception
    {
        c.setMaxHealthPoints(15);
        assertEquals(15, c.getMaxHealthPoints());
    }

    @Test
    public void setCurrentHealthPoints() throws Exception
    {
        c.takeDamage(5);
        assertEquals(5, c.getCurrentHealthPoints());
    }

    @Test
    public void getAttackPoints() throws Exception
    {
        assertEquals(20, c.getAttackPoints());
    }

    @Test
    public void setAttackPoints() throws Exception
    {
        c.setAttackPoints(25);
        assertEquals(25, c.getAttackPoints());
    }

    @Test
    public void getDefensePoints() throws Exception
    {
        assertEquals(30, c.getDefensePoints());
    }

    @Test
    public void setDefensePoints() throws Exception
    {
        c.setDefensePoints(35);
        assertEquals(35, c.getDefensePoints());
    }

    @Test
    public void getMovementPoints() throws Exception
    {
        assertEquals(40, c.getMovementPoints());
    }

    @Test
    public void setMovementPoints() throws Exception
    {
        c.setMovementPoints(45);
        assertEquals(45, c.getMovementPoints());
    }

    @Test
    public void setDead() throws Exception
    {
        c.setDead(true);
        assertEquals(true, c.isDead());
    }

    @Test
    public void getCurrentTerrain() throws Exception
    {

    }

    @Test
    public void setCurrentTerrain() throws Exception
    {

    }

    @Test
    public void setPosition() throws Exception
    {
        int[] i = {2,2};
        c.setPosition(i);
        assertEquals(i, c.getPosition());
    }

    @Test
    public void setPlayer() throws Exception
    {
        Player p = new Player("testPlayer");
        c.setPlayer(p);
        assertEquals(p, c.getPlayer());
    }

}