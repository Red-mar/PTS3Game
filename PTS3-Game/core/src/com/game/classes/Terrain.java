package com.game.classes;

public class Terrain {
    private TerrainProperties property;
    private int bonus;
    private int x;
    private int y;

    /**
     * a tile on a map
     * @param property
     */
    public Terrain(TerrainProperties property, int x, int y) {
        this.property = property;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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
