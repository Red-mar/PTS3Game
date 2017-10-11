package com.game.classes;

import com.badlogic.gdx.maps.objects.RectangleMapObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/libgdx/libgdx/wiki/Tile-maps
 * :thinking:
 */
public class Map implements Serializable {
    private int sizeX;
    private int sizeY;
    private int tileHeight;
    private int tileWidth;
    private Terrain[][] terrains;

    /**
     * A map
     * @param sizeX
     * @param sizeY
     */
    public Map(int sizeX, int sizeY, int tileHeight, int tileWidth, ArrayList<RectangleMapObject> mapObjects) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;

        terrains = new Terrain[sizeX][sizeY];
        for (int i = 0; i < sizeX; i++){
            for (int j = 0; j < sizeY; j++){
                terrains[i][j] = new Terrain(TerrainProperties.Normal, i, j);
            }
        }

        for (int i = 0; i < mapObjects.size(); i++){
            for (int h = 0; h < sizeX; h++){
                for (int j = 0; j < sizeY; j++){
                    if (terrains[h][j].getX() == mapObjects.get(i).getRectangle().x && terrains[h][j].getY() == mapObjects.get(i).getRectangle().y)
                        terrains[h][j].setProperty(TerrainProperties.Impassable);
                }
            }
        }
    }
    ///ToDo constructor which loads the tmx


    public Terrain[][] getTerrains() {
        return terrains;
    }

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

    public List<Terrain> getNeighbours(Terrain terrain){
        ArrayList<Terrain> neighbours = new ArrayList<Terrain>();

        for (int x = -1; x <= 1; x++){
            for (int y = -1; y <= 1; y++){
                if (x==0 && y==0) continue;

                int checkX = terrain.getX() + x;
                int checkY = terrain.getY() + y;

                if (checkX >= 0 && checkX < sizeX && checkY >= 0 && checkY < sizeY){
                    neighbours.add(terrains[checkX][checkY]);
                }
            }
        }

        return neighbours;
    }

    /**
     * Clears the terrain of any characters.
     */
    public void clearTerrain(){
        for (Terrain[] terrains : getTerrains()) {
            for (Terrain terrain : terrains) {
                terrain.setCharacter(null); //Clear terrain of fake characters
            }
        }
    }

    /*public Terrain getTerrain() {
        return terrain;
    }*/

}
