package com.game.classes;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private boolean isSpectator;
    private boolean isReady;
    private List<Character> characters;
    private boolean isLocalPlayer;

    public Player(String name) {
        this.name = name;
        this.characters = new ArrayList<Character>();
    }

    public String getName() {
        return name;
    }

    public boolean isSpectator() {
        return isSpectator;
    }

    public void setSpectator(boolean spectator) {
        isSpectator = spectator;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }

    public boolean isLocalPlayer() {
        return isLocalPlayer;
    }

    public void setLocalPlayer(boolean localPlayer) {
        isLocalPlayer = localPlayer;
    }
}
