package network.Client;

import com.game.classes.Player;

import java.util.ArrayList;

public class ClientEventHandler implements IClientEvents {

    @Override
    public void onConnect(String serverName) {
        System.out.println("Connected with " + serverName);
    }

    @Override
    public void onDisconnect() {
        System.out.println("Not connected.");
    }

    @Override
    public void onMessaged(String message) {
    }

    @Override
    public void onGetPlayers(ArrayList<Player> players) {
    }
}
