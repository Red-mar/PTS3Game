package com.game.classes.unitTest;

import com.game.classes.Terrain;
import com.game.classes.TerrainProperties;
import com.game.classes.TerrainVisual;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TerrainTest {
    Terrain terrain;
    TerrainVisual terrainVisual;
    TerrainProperties terrainProperties;

    @Before
    public void setUp() throws Exception {
        terrainVisual = TerrainVisual.Grass;
        terrainProperties = TerrainProperties.Normal;

        terrain = new Terrain(terrainVisual, terrainProperties);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getVisual() throws Exception {
        assertEquals(terrainVisual, terrain.getVisual());
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

}