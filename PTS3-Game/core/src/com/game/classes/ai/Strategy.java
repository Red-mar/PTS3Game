package com.game.classes.ai;

import com.badlogic.gdx.utils.BinaryHeap;
import com.game.classes.Character;
import com.game.classes.Game;
import com.game.classes.Player;

import java.io.Serializable;
import java.util.Random;

public class Strategy implements Serializable {
    Game gameState;
    Player aiPlayer;
    Random rnd;

    public Strategy(Player player) {
        aiPlayer = player;
        rnd = new Random();

    }

    public void setGameState(Game gameState) {
        this.gameState = gameState;
    }

    public void playTurn() {
        for (Player player : gameState.getPlayers()) {
            if (player.getName().equals(aiPlayer.getName())){
                aiPlayer = player;
                continue;
            }
        }

        for (Character aiCharacter : aiPlayer.getCharacters()) {
            for (Player player : gameState.getPlayers()) {
                if (player != aiPlayer){
                    for (Character character : player.getCharacters()) {
                        gameState.getPathing().findPath(
                                aiCharacter.getCurrentTerrain(),
                                character.getCurrentTerrain());
                        character.forceSetCurrentTerrain(gameState.getPathing().getPath().get(character.getMovementPoints()));
                        return;
                    }
                }
            }
        }
    }
}
