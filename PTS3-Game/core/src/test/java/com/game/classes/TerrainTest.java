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

}