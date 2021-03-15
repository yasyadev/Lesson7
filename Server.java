import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server extends Thread{
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    private Socket socket;
    private static List<MyClient>clients = Collections.synchronizedList(new ArrayList<>());
    private MyClient client;

    public Server(Socket socket) {
        this.socket = socket;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        try {
            whileChating();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }
    }
    private void whileChating() throws IOException {
        String message = "SERVER msg: you are connecting " + socket.getInetAddress() + " : " + socket.getPort();
        printWriter.println(message + "\r\n");
        do {
            message = bufferedReader.readLine();
            printWriter.println("Сообщение получил");
            printWriter.println(message);
            System.out.println(message);
        }while (!message.equals("EXIT"));
    }
    private void closeConnection(){
        System.out.println("Закрыите соединения");
        printWriter.println("Пользователь отключился \r\n");
        try {
            printWriter.close();
            bufferedReader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
