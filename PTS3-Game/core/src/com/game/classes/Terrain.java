package com.game.classes;

public class Terrain {
    private TerrainVisual visual;
    private TerrainProperties property;
    private int bonus;

    public Terrain(TerrainVisual visual, TerrainProperties property) {
        this.visual = visual;
        this.property = property;
    }

    public TerrainVisual getVisual() {
        return visual;
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
