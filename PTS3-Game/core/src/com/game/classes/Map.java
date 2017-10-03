package com.game.classes;

import java.util.ArrayList;

/**
 * https://github.com/libgdx/libgdx/wiki/Tile-maps
 * :thinking:
 */
public class Map {
    private int sizeX;
    private int sizeY;
    private int tileHeight;
    private int tileWidth;
    private ArrayList<Terrain> terrain;

    /**
     * A map
     * @param sizeX
     * @param sizeY
     */
    public Map(int sizeX, int sizeY, int tileHeight, int tileWidth) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
        terrain = new ArrayList<Terrain>();
        for (int i = 0; i < sizeX; i++){
            for (int j = 0; i < sizeY; i++){

                terrain.add(new Terrain(TerrainProperties.Normal, i, j));
            }
        }
    }
    ///ToDo constructor which loads the tmx


    public int getSizeX() {
        return sizeX;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    public int getTileHeight() { return tileHeight; }

    public int getTileWidth() { return tileWidth; }

    /*public Terrain getTerrain() {
        return terrain;
    }*/

}
