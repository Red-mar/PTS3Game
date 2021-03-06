package com.game.classes.pathing;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.game.classes.Map;
import com.game.classes.Terrain;
import com.game.classes.TerrainProperties;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.css.Rect;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class aStarPathingTest {
    ArrayList<RectangleMapObject> objects;
    Map map;
    aStarPathing pathing;

    @Before
    public void setUp() throws Exception {
        objects = new ArrayList<RectangleMapObject>();
        RectangleMapObject mapObject = new RectangleMapObject();
        objects.add(mapObject);

        map = new Map(40,40,15,15, objects);

        pathing = new aStarPathing(map);

    }

    @Test
    public void setMap() throws Exception {
        map = new Map(40,40,10,10, objects);
        pathing.setMap(map);
    }

    @Test
    public void getPath() throws Exception {
        Terrain terrain = map.getTerrains()[5][5];
        Terrain terrain2 = map.getTerrains()[1][1];
        pathing.findPath(terrain2, terrain);
        List<Terrain> path = pathing.getPath();

        assertEquals(path.get(1).getX(), 2);
        assertEquals(path.get(0).getX(), 1);
    }
}