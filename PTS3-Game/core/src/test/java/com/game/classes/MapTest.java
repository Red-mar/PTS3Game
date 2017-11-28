package com.game.classes;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.game.classes.Map;
import com.game.classes.pathing.aStarPathing;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MapTest {
    ArrayList<RectangleMapObject> objects;
    Map map;
    TiledMap tiledMap;

    @Before
    public void setUp() throws Exception
    {
        //tiledMap = new TmxMapLoader().load("map_2.tmx");
        objects = new ArrayList<RectangleMapObject>();
        RectangleMapObject mapObject = new RectangleMapObject();
        objects.add(mapObject);

        map = new Map(40,40,15,15, objects);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getSizeX() throws Exception
    {
        assertEquals(map.getSizeX(), 40);
    }

    @Test
    public void getSizeY() throws Exception
    {

        assertEquals(map.getSizeY() , 40);
    }

    @Test
    public void getTerrains() throws Exception {
        assertEquals(map.getTerrains()[0][0].getX(), 0);
    }

    @Test
    public void getTileHeight() throws Exception {
        assertEquals(map.getTileHeight(), 15);
    }

    @Test
    public void getTileWidth() throws Exception {
        assertEquals(map.getTileWidth(), 15);
    }

    @Test
    public void setTiledMap() throws Exception {
        //map.setTiledMap(tiledMap);
        //assertEquals(map.getTiledMap(), tiledMap);
    }

    @Test
    public void getNeighbours() throws Exception {
        List<Terrain> neighbours = map.getNeighbours(map.getTerrains()[1][1]);
        assertEquals(neighbours.get(0).getX(), map.getTerrains()[0][0].getX());
    }

    @Test
    public void clearTerrain() throws Exception {
        map.clearTerrain();
        assertEquals(map.getTerrains()[0][0].getCharacter(), null);
    }

    @Test
    public void TiledMap() throws Exception {
        TiledMap tm = new TiledMap();
        map.setTiledMap(tm);
        assertEquals(tm, map.getTiledMap());
    }
}