package com.game.classes;

import java.io.Serializable;

public class Terrain implements Serializable {
    private TerrainProperties property;
    private int bonus;
    private Character character;
    private int x;
    private int y;

    /**
     * for pathfinding
     */
    private int gCost;
    private int hCost;
    private transient Terrain parent;

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

    public TerrainProperties getProperty() { return property; }

    public void setProperty(TerrainProperties property) { this.property = property; }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public int getgCost() {
        return gCost;
    }

    public void setgCost(int gCost) {
        this.gCost = gCost;
    }

    public int gethCost() {
        return hCost;
    }

    public void sethCost(int hCost) {
        this.hCost = hCost;
    }

    public int getfCost(){
        return gCost + hCost;
    }

    public Terrain getParent() {
        return parent;
    }

    public void setParent(Terrain parent) {
        this.parent = parent;
    }

    public int getHeuristic(Terrain terrain){
        return Math.abs(terrain.getX() - x) + Math.abs(terrain.getY() - y);
    }
}
