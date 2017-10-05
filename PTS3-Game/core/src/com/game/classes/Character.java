package com.game.classes;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;

public class Character implements Serializable {
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
    private transient Sprite sprite;
    private String spriteTexture;

    /**
     * A character that belongs to a player.
     * @param name The name of the character
     * @param maxHealthPoints The maximum amount of damage a character can take.
     * @param attackPoints The amount of damage the character can deal.
     * @param defensePoints The amount of damage that gets reduced per received attack.
     * @param movementPoints The amount of tiles a character can move.
     */
    public Character(String name, int maxHealthPoints, int attackPoints, int defensePoints, int movementPoints, Sprite sprite, Terrain currentTerrain, String spriteTexture, Player player) {
        this.name = name;
        this.maxHealthPoints = maxHealthPoints;
        this.attackPoints = attackPoints;
        this.defensePoints = defensePoints;
        this.movementPoints = movementPoints;
        this.sprite = sprite;
        this.spriteTexture = spriteTexture;
        this.currentTerrain = currentTerrain;
        currentTerrain.setCharacter(this);
        this.player = player;
    }

    /**
     * Gets the name of the character
     * @return The name of the character as string.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the character
     * @param name Requires a name as a string.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the maximum amount of health points.
     * @return Return the maximum amount of health points as int.
     */
    public int getMaxHealthPoints() {
        return maxHealthPoints;
    }

    /**
     * Set the maximum amount of health points.
     * @param maxHealthPoints Requires an int.
     */
    public void setMaxHealthPoints(int maxHealthPoints) {
        this.maxHealthPoints = maxHealthPoints;
    }

    /**
     * Get the current amount of health points.
     * @return Return the current amount of health points as int.
     */
    public int getCurrentHealthPoints() {
        return currentHealthPoints;
    }

    /**
     * Set the current amount of health points.
     * @param currentHealthPoints Requires the current amount of health points as int.
     */
    public void setCurrentHealthPoints(int currentHealthPoints) {
        this.currentHealthPoints = currentHealthPoints;
    }

    /**
     * Get the attack points of a character.
     * @return Returns the current attack points of a character as int.
     */
    public int getAttackPoints() {
        return attackPoints;
    }

    /**
     * Set the attack points of a character.
     * @param attackPoints Requires an int.
     */
    public void setAttackPoints(int attackPoints) {
        this.attackPoints = attackPoints;
    }

    /**
     * Get the defense points of a character.
     * @return Return an int.
     */
    public int getDefensePoints() {
        return defensePoints;
    }

    /**
     * Set the defense points of a character.
     * @param defensePoints Requires an int.
     */
    public void setDefensePoints(int defensePoints) {
        this.defensePoints = defensePoints;
    }

    /**
     * Get the defense points of a character.
     * @return Returns an int.
     */
    public int getMovementPoints() {
        return movementPoints;
    }

    /**
     * Set the movement points of a character.
     * @param movementPoints Requires an int.
     */
    public void setMovementPoints(int movementPoints) {
        this.movementPoints = movementPoints;
    }

    /**
     * Checks if the character has died.
     * @return Returns false if alive, true if not.
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     * Set if a character is alive or not.
     * @param dead Requires a boolean.
     */
    public void setDead(boolean dead) {
        isDead = dead;
    }

    /**
     * Get the current terrain the character is standing on.
     * @return Returns a Terrain object.
     */
    public Terrain getCurrentTerrain() {
        return currentTerrain;
    }

    /**
     * Set the current terrain a character is standing on.
     * @param terrain Requires a Terrain object.
     */
    public boolean setCurrentTerrain(Terrain terrain) {
        if (!canMove(terrain)){
            return false;
        }
        this.currentTerrain = terrain;
        return true;
    }

    public boolean canMove(Terrain terrain){
        int xMove = Math.abs(terrain.getX() - currentTerrain.getX());
        int yMove = Math.abs(terrain.getY() - currentTerrain.getY());
        int totalMovement = xMove + yMove;
        //TODO gebruik len

        if (totalMovement > movementPoints){
            return false;
        }
        return true;
    }

    public boolean canAttack(Terrain terrain){
        int xMove = Math.abs(terrain.getX() - currentTerrain.getX());
        int yMove = Math.abs(terrain.getY() - currentTerrain.getY());
        int totalMovement = xMove + yMove;
        //TODO gebruik len

        if (totalMovement > movementPoints + 1 && terrain.getCharacter() == null){
            return false;
        }
        return true;
    }

    /**
     * Get the current position of the character on the map.
     * @return Returns an int array
     * TODO int array?
     */
    public int[] getPosition() {
        return position;
    }

    /**
     * Set the current position of a character.
     * @param position Requires the an int array.
     */
    public void setPosition(int[] position) {
        this.position = position;
    }

    /**
     * Get the player that can control this character.
     * @return Returns a player object.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Set the player that can control this character
     * @param player Requires a player object.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public String getSpriteTexture() {
        return spriteTexture;
    }
}
