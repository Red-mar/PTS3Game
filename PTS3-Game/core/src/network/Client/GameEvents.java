package network.Client;

import com.game.classes.Player;

import java.util.ArrayList;

public interface GameEvents {
    void onGetPlayers(ArrayList<Player> players);
}
