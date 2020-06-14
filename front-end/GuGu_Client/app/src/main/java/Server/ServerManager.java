package Server;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServerManager extends Thread {
    //    192.168.43.58
//    10.85.15.88
//    private Gson gson=new Gson();
    private static final String IP = "10.0.2.2";
    private Socket socket;
    private String username;
    private int iconID;
    private String message = null;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private ReceiveChatMsg receiveChatMsg;
    private static final ServerManager serverManager = new ServerManager();

    public static ServerManager getServerManager() {
        return serverManager;
    }

    private ServerManager() {
        receiveChatMsg = new ReceiveChatMsg();
    }

    public void run() {
        try {
            socket = new Socket(IP, 27777);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

            String msg = null;
            String receiveMsgType = null;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println("The Client Receive line : " + line);
                if (!line.equals("-1")) {
                    if (receiveMsgType == null) {
                        receiveMsgType = line;
                    } else {
                        msg = line;
                    }
                } else {
                    dealMsg(receiveMsgType, msg);
                    receiveMsgType = null;
                    msg = null;
                    line = null;
                }
            }


        } catch (IOException e) {
            System.out.println("IP Adress Error ! ! !");
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
                bufferedReader.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void dealMsg(String receiveMsgType, String msg) {
        if (receiveMsgType.equals("ACKLOGIN")) {
            this.message = "SUCCESS";
        } else if (receiveMsgType.equals("REGACK")) {
            this.message = msg;
        } else {
            this.message = null;
        }
        return;
    }

    public void sendMessage(Context context, String msg, String msgType) {
        try {
            while (socket == null) ;
            if (bufferedWriter != null) {
                System.out.println("Send Message : [ " + msgType + " ] : " + msg);
                bufferedWriter.write(msgType + "\n");
                bufferedWriter.flush();
                bufferedWriter.write(msg + "\n");
                bufferedWriter.flush();
                bufferedWriter.write("-1\n");
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMessage() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getIconID() {
        return iconID;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }
}

