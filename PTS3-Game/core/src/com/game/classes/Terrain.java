package com.game.classes;

public class Terrain {
    private TerrainProperties property;
    private int bonus;
    private int[] position;

    /**
     * a tile on a map
     * @param property
     */
    public Terrain(TerrainProperties property, int x, int y) {
        this.property = property;
        position = new int[2];
        position[0] = x;
        position[1] = y;
    }


    public TerrainProperties getProperty() {
        return property;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }
}
