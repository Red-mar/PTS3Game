package network.Client;

import com.game.classes.Player;

import java.util.ArrayList;

public interface ChatEvents {
    void onConnect(String serverName);

    void onDisconnect();

    void onMessaged(String message);
}
