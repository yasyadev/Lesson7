import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerRun {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5656, 25)) {
            while (true){
                Socket socket = serverSocket.accept();
                new Server(socket).start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
