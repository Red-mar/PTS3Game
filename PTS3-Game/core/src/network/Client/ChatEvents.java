package network.Client;

import com.game.classes.Player;

import java.util.ArrayList;

public interface ChatEvents {
    void onConnect(String serverName);

    void onDisconnect(String reason);

    void onMessaged(String message);
}
