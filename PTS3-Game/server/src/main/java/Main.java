import network.Server.Server;

public class Main {

    public static void main(String[] args){
        int port = Integer.parseInt(args[0]);

        try {
            Server mainServer = new Server(port);
            mainServer.start();

        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
