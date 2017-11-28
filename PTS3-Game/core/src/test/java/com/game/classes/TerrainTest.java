package com.game.classes;

import com.game.classes.Terrain;
import com.game.classes.TerrainProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TerrainTest {
    Terrain terrain;
    TerrainProperties terrainProperties;

    @Before
    public void setUp() throws Exception {
        terrainProperties = TerrainProperties.Normal;

        terrain = new Terrain(terrainProperties, 0 , 0);
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void getProperty() throws Exception {
        assertEquals(terrainProperties, terrain.getProperty());
    }

    @Test
    public void getBonus() throws Exception {
        terrain.setBonus(1);

        assertEquals(1,terrain.getBonus());
    }

    @Test
    public void setBonus() throws Exception {
        terrain.setBonus(1);

        assertEquals(1, terrain.getBonus());
    }

    @Test
    public void gCost() throws Exception {
        int cost = 3;
        terrain.setgCost(cost);
        assertEquals(terrain.getgCost(), cost);
    }

    @Test
    public void hCost() throws Exception {
        int cost = 3;
        terrain.sethCost(cost);
        assertEquals(terrain.gethCost(), cost);
    }

    @Test
    public void fCost() throws Exception {
        int cost = 3;
        terrain.sethCost(cost);
        terrain.setgCost(cost);
        assertEquals(terrain.getfCost(), cost + cost);
    }

    @Test
    public void parent() throws Exception {
        Terrain parent = new Terrain(terrainProperties, 5 , 5);
        terrain.setParent(parent);
        assertEquals(parent, terrain.getParent());
    }

    @Test
    public void getHeuristic() throws Exception {
        int x = 3;
        int y = 2;
        int testHeuristic = Math.abs(terrain.getX()) + Math.abs(terrain.getY());
        assertEquals(testHeuristic, terrain.getHeuristic(terrain));
    }
}