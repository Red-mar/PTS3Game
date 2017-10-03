package com.game.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    private String name;
    private boolean isSpectator;
    private boolean isReady;
    private ArrayList<Character> characters;
    private boolean isLocalPlayer;

    /**
     * Creates a new player
     * @param name The name of the player
     */
    public Player(String name) {
        this.name = name;
        this.characters = new ArrayList<Character>();
    }

    /**
     * Gets the name of the player
     * @return Returns a string.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Specifies if the player is a spectator.
     * @return True if the player is a spectator, false if not.
     */
    public boolean isSpectator() {
        return isSpectator;
    }

    /**
     * Sets whether the player is a spectator.
     * @param spectator Requires a boolean
     */
    public void setSpectator(boolean spectator) {
        isSpectator = spectator;
    }

    /**
     * Checks if the player is ready.
     * @return True if the player is ready, false if not.
     */
    public boolean isReady() {
        return isReady;
    }

    /**
     * Sets the ready state of the player.
     * @param ready Requires a boolean.
     */
    public void setReady(boolean ready) {
        isReady = ready;
    }

    /**
     * Gets the list of characters that belong to this player.
     * @return Returns a list of characters.
     */
    public ArrayList<Character> getCharacters() {
        return characters;
    }

    /**
     * Sets a whole list of characters.
     * @param characters Requires a list of characters.
     */
    public void setCharacters(ArrayList<Character> characters) {
        this.characters = characters;
    }

    public void addCharacter(Character character){
        characters.add(character);
    }

    /**
     * Checks if the player is a local player.
     * @return True if the player is a local player, false if not.
     */
    public boolean isLocalPlayer() {
        return isLocalPlayer;
    }

    /**
     * Sets whether the player is a local player.
     * @param localPlayer Requires a boolean.
     */
    public void setLocalPlayer(boolean localPlayer) {
        isLocalPlayer = localPlayer;
    }

    @Override
    public String toString() {
        if (isSpectator){
            return getName() + "\t|\tSpectator.";
        }

        if (isReady){
            return getName() + "\t|\tReady!";
        }
        return getName() + "\t|\tNot Ready.";
    }
}
