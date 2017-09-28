package network.Client;

import com.game.classes.Player;

import java.net.SocketAddress;
import java.util.ArrayList;

public interface IClientEvents {
    void onConnect(String serverName);

    void onDisconnect();

    void onMessaged(String message);

    void onGetPlayers(ArrayList<Player> players);
}
