package com.game.classes;

import com.badlogic.gdx.graphics.g2d.Sprite;

import java.io.Serializable;

public class Character implements Serializable {
    private String name;
    private int maxHealthPoints;
    private int currentHealthPoints;
    private int attackPoints;
    private int defensePoints;
    private int movementPoints;
    private int currentMovementPoints;
    private int attackRange;
    private boolean isDead;
    private Terrain currentTerrain;
    private int[] position;
    private Player player;
    private transient Sprite sprite;
    private String spriteTexture;
    private boolean hasAttacked = false;

    /**
     * A character that belongs to a player.
     * @param name The name of the character
     * @param maxHealthPoints The maximum amount of damage a character can take.
     * @param attackPoints The amount of damage the character can deal.
     * @param defensePoints The amount of damage that gets reduced per received attack.
     * @param movementPoints The amount of tiles a character can move.
     */
    public Character(String name, int maxHealthPoints, int attackPoints, int defensePoints, int movementPoints, int attackRange, Sprite sprite, Terrain currentTerrain, String spriteTexture, Player player) {
        this.name = name;
        this.maxHealthPoints = maxHealthPoints;
        this.currentHealthPoints = maxHealthPoints;
        this.attackPoints = attackPoints;
        this.defensePoints = defensePoints;
        this.movementPoints = movementPoints;
        this.attackRange = attackRange;
        this.currentMovementPoints = movementPoints;
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
    public boolean setCurrentTerrain(Terrain terrain, int amountMoved) {
        if (!canMove(terrain)){
            return false;
        }
        currentMovementPoints -= amountMoved;
        this.currentTerrain = terrain;
        //TODO: reset pathfinder here
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

    /**
     * get
     * @return Sprite
     */
    public Sprite getSprite() {
        return sprite;
    }

    /**
     *
     * @param sprite
     */
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    /**
     *
     * @return
     */
    public String getSpriteTexture() {
        return spriteTexture;
    }

    /**
     * Set if a character has (true) or has not (false) attacked this turn
     * @param hasAttacked requires a boolean
     */
    public void setHasAttacked(boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    /**
     * Check if the character has attacked
     * @return boolean
     */
    public boolean hasAttacked() {
        return hasAttacked;
    }

    /**
     * Set the amount of movement a character can make each turn
     * @param currentMovementPoints amount of movementpoints a character has
     */
    public void setCurrentMovementPoints(int currentMovementPoints) {
        this.currentMovementPoints = currentMovementPoints;
    }

    public int getAttackRange()
    {
        return attackRange;
    }

    public int getCurrentMovementPoints()
    {
        return currentMovementPoints;
    }


    public void forceSetCurrentTerrain(Terrain terrain){
        this.currentTerrain = terrain;
    }

    public void takeDamage(int attackPoints) {
        int currentHealth = currentHealthPoints - (attackPoints - defensePoints);
        this.currentHealthPoints = currentHealth;
        if (this.currentHealthPoints <= 0){
            isDead = true;
        }
    }

    /**
     * check if a character can still move during this turn
     * @param terrain check if terrain isn't occupied or not accesible
     * @return boolean result if character can move
     */
    public boolean canMove(Terrain terrain){
        int totalMovement = calculateTotalMovement(terrain);

        if (totalMovement > currentMovementPoints){
            return false;
        } else if (terrain.getCharacter() != null){
            return false;
        } else if (terrain.getProperty() == TerrainProperties.Impassable){
            return false;
        }// else if (terrain.getProperty() != TerrainProperties.Impassable && !Pathfinder.canFindPath(terrain, this)){
        //   return false;
        //}
        return true;
    }

    /**
     * check if character can attack this turn
     * @param terrain where you attack is occupied or is you
     * @return result if character can attack or not
     */
    public boolean canAttack(Terrain terrain){
        int totalMovement = calculateTotalMovement(terrain);

        if (totalMovement > attackRange){
            return false;
        }
        if (terrain.getCharacter() == null && terrain.getCharacter() != this || terrain.getCharacter() == this){
            return false;
        }
        if (hasAttacked){
            return false;
        }
        return true;
    }

    /**
     * calculate the movement
     * @param terrain
     * @return
     */
    private int calculateTotalMovement(Terrain terrain){
        int xMove = Math.abs(terrain.getX() - currentTerrain.getX());
        int yMove = Math.abs(terrain.getY() - currentTerrain.getY());
        return xMove + yMove;
    }
}
