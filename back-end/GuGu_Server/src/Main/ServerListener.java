package Main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener extends Thread{

    private ServerSocket serverSocket;

    @Override
    public void run() {
        try {
            // 队列长度27
            serverSocket = new ServerSocket(27777, 27);
            while (true) {
                // 队首取出一个请求
                Socket socket = serverSocket.accept();
                ChatSocket chatSocket = new ChatSocket(socket);
                chatSocket.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
