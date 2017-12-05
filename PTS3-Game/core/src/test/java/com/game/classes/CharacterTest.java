package com.game.classes;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.game.classes.Character;
import com.game.classes.Player;
import com.game.classes.Terrain;
import com.game.classes.TerrainProperties;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterTest {
    Character c = new Character("", 10, 20, 30, 40, 1, new Sprite(), new Terrain(TerrainProperties.Normal, 1, 1), "test", new Player(""));

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
        assertEquals(35, c.getCurrentHealthPoints());
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
        Terrain t = new Terrain(TerrainProperties.Normal, 38, 38);
        assertEquals(c.setCurrentTerrain(t, 0), false);
        t = new Terrain(TerrainProperties.Normal, 1, 1);
        t.setCharacter(null);
        assertEquals(c.setCurrentTerrain(t, 0), true);
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

    @Test
    public void getCurrentHealthPoints() throws Exception {
        assertEquals(c.getCurrentHealthPoints(), 10);
    }

    @Test
    public void isDead() throws Exception {
        assertEquals(c.isDead(), false);
    }

    @Test
    public void getPlayer() throws Exception {
        assertEquals(c.getPlayer().getName(), "");
    }

    @Test
    public void hasAttacked() throws Exception {
        c.setHasAttacked(true);
        assertEquals(c.hasAttacked(), true);
    }

    @Test
    public void forceSetCurrentTerrain() throws Exception {
        Terrain newTerrain = new Terrain(TerrainProperties.Normal, 1, 1);
        c.forceSetCurrentTerrain(newTerrain);

        assertEquals(c.getCurrentTerrain().getX(), 1);
    }

    @Test
    public void takeDamage() throws Exception {
        c.takeDamage(5);
        assertEquals(c.getCurrentHealthPoints(), 35);
        c.takeDamage(100);
        assertEquals(c.isDead(), true);
    }

    @Test
    public void canMove() throws Exception {
        assertEquals(c.canMove(new Terrain(TerrainProperties.Normal, 38, 38)), false);
        c.setCurrentMovementPoints(40);
        Terrain t = new Terrain(TerrainProperties.Impassable,1 , 1);
        t.setCharacter(c);
        assertEquals(c.canMove(t), false);

        t.setCharacter(null);
        assertEquals(c.canMove(t), false);

        t = new Terrain(TerrainProperties.Normal, 1, 1);
        assertEquals(c.canMove(t), true);
    }

    @Test
    public void canAttack() throws Exception {
        Terrain t = new Terrain(TerrainProperties.Normal,1 , 1);
        assertEquals(c.canAttack(t), false);

        Terrain enemyterrain = new Terrain(TerrainProperties.Normal,1 , 1);
        Character enemy = new Character("", 10, 20, 30, 40, 1, new Sprite(), new Terrain(TerrainProperties.Normal, 1, 1), "test", new Player(""));
        enemyterrain.setCharacter(enemy);
        assertEquals(c.canAttack(enemyterrain), true);

        c.setHasAttacked(true);
        assertEquals(c.canAttack(enemyterrain), false);
    }

    @Test
    public void currentMovementPoints() throws Exception {
        int movementpoints = 40;
        c.setCurrentMovementPoints(movementpoints);
        assertEquals(c.getCurrentMovementPoints(), movementpoints);
    }

    @Test
    public void sprite() throws Exception {
        Sprite sprite = new Sprite();
        c.setSprite(sprite);
        assertEquals(c.getSprite(), sprite);
        assertEquals(c.getSpriteTexture(), "test");
    }

    @Test
    public void attackRange() throws Exception {
        assertEquals(c.getAttackRange(), 1);
    }
}