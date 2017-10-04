package com.game.classes;

import java.io.Serializable;

public class Terrain implements Serializable {
    private TerrainProperties property;
    private int bonus;
    private Character character;
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

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
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
