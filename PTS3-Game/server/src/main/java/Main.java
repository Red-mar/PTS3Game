import com.game.classes.network.Server.Server;

public class Main {

    public static void main(String[] args){
        int port = Integer.parseInt(args[0]);
        String ip = args[1];

        try {
            Server mainServer = new Server(port, ip);
            mainServer.start();

        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
