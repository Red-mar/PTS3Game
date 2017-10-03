package com.game.classes;

public class Terrain {
    private TerrainProperties property;
    private int bonus;

    /**
     * a tile on a map
     * @param property
     */
    public Terrain(TerrainProperties property) {
        this.property = property;
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
