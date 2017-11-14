package com.game.classes.network;

import com.game.classes.Player;

import java.util.ArrayList;

public interface GameEvents {
    void onGetPlayers(ArrayList<Player> players);
    void onStartGame();
    void onEndGame();
    void onJoinGame();
    void onUpdateCharacter(int x, int y, String charName, String playerName);
}
