package network.Client;

import java.net.SocketAddress;

public interface IClientEvents {
    void onConnect(String serverName);

    void onDisconnect();

    void onMessaged(String message);
}
