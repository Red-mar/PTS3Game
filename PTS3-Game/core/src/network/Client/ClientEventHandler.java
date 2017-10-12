package network.Client;

import com.game.classes.Player;

import java.util.ArrayList;

public class ClientEventHandler implements ChatEvents {

    @Override
    public void onConnect(String serverName) {
        System.out.println("Connected with " + serverName);
    }

    @Override
    public void onDisconnect(String reason) {
        System.out.println("Not connected.");
    }

    @Override
    public void onMessaged(String message) {
    }
}
