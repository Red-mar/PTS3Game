package network.Client;

public class ClientEventHandler implements IClientEvents {

    @Override
    public void onConnect(String serverName) {
        System.out.println("Connected with " + serverName);
    }

    @Override
    public void onDisconnect() {
        System.out.println("Disconnected.");
    }

    @Override
    public void onMessaged(String message) {
        System.out.println(message);
    }
}
