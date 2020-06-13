package Main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener extends Thread{

    private ServerSocket serverSocket;

    @Override
    public void run() {
        try {
            // 监听port端口，请求队列长度为27
            serverSocket = new ServerSocket(27777, 27);
            while (true) {
                // 从队首获取一个socket
                Socket socket = serverSocket.accept();
                ChatSocket chatSocket = new ChatSocket(socket);
                chatSocket.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
