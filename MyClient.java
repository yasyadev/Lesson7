import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MyClient extends JFrame {

    private JTextField userInputText;
    private JTextArea chatWindow;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    private Socket socket;

    public MyClient(){
        super("Client");
        userInputText = new JTextField();
        userInputText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage(e.getActionCommand());
                userInputText.setText("");
            }
        });
        add(userInputText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        chatWindow.setEditable(false);
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        setSize(300, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        startClient();
    }
    private void startClient() {
        try {
            connectServer();
            whileChating();
        }catch (EOFException e){
            showMessage("Клиент оборвал соединение");
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            closeConnection();
        }
    }
    private void connectServer() throws IOException {
        showMessage("Connecting...");
        socket = new Socket();
        socket.connect(new InetSocketAddress("127.0.0.1", 5656), 2000);
        printWriter = new PrintWriter(socket.getOutputStream(), true);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        showMessage("Connection ready");
    }
    private void whileChating() throws IOException {
        String message;
        do {
            message = bufferedReader.readLine();
            showMessage("\n" + message);
        }while (!message.equals("EXIT"));
    }
    private void closeConnection(){
        showMessage("\nClose connection...");
        try {
            printWriter.close();
            bufferedReader.close();
            socket.close();
        } catch (IOException e) {
            e.getMessage();
        }
    }
    public void sendMessage(String message){
        printWriter.println(message);
    }
    private void showMessage(String msg){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                chatWindow.append(msg);
            }
        });
    }
}
