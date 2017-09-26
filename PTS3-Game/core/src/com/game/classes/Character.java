package com.game.classes;

public class Character {
    private String name;
    private int maxHealthPoints;
    private int currentHealthPoints;
    private int attackPoints;
    private int defensePoints;
    private int movementPoints;
    private boolean isDead;
    private Terrain currentTerrain;
    private int[] position;
    private Player player;

    public Character(String name, int maxHealthPoints, int attackPoints, int defensePoints, int movementPoints) {
        this.name = name;
        this.maxHealthPoints = maxHealthPoints;
        this.attackPoints = attackPoints;
        this.defensePoints = defensePoints;
        this.movementPoints = movementPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxHealthPoints() {
        return maxHealthPoints;
    }

    public void setMaxHealthPoints(int maxHealthPoints) {
        this.maxHealthPoints = maxHealthPoints;
    }

    public int getCurrentHealthPoints() {
        return currentHealthPoints;
    }

    public void setCurrentHealthPoints(int currentHealthPoints) {
        this.currentHealthPoints = currentHealthPoints;
    }

    public int getAttackPoints() {
        return attackPoints;
    }

    public void setAttackPoints(int attackPoints) {
        this.attackPoints = attackPoints;
    }

    public int getDefensePoints() {
        return defensePoints;
    }

    public void setDefensePoints(int defensePoints) {
        this.defensePoints = defensePoints;
    }

    public int getMovementPoints() {
        return movementPoints;
    }

    public void setMovementPoints(int movementPoints) {
        this.movementPoints = movementPoints;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public Terrain getCurrentTerrain() {
        return currentTerrain;
    }

    public void setCurrentTerrain(Terrain currentTerrain) {
        this.currentTerrain = currentTerrain;
    }

    public int[] getPosition() {
        return position;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
