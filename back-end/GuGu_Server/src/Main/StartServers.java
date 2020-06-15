package Main;

import DB.DBManager;
import View.MainWindow;

import java.awt.*;
import java.sql.SQLException;

public class StartServers {
    public static void main(String[] args) {
        // awt的事件处理线程
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainWindow frame = MainWindow.getMainWindow();
                    DBManager dbManager = DBManager.getDBManager();

                    ServerListener listener = new ServerListener();

                    frame.setOnStartServersListener(new MainWindow.OnStartServersListener() {
                        // 停止
                        @Override
                        public void stop() {
                            try {
                                dbManager.getConnection().close();
                                MainWindow.getMainWindow().setShowMsg("DB connection is closed");
                            } catch (SQLException e) {
                                MainWindow.getMainWindow().setShowMsg("DB connection close failed");
                                e.printStackTrace();
                            }
                        }

                        // 开始
                        @Override
                        public void start() {
                            dbManager.addDBDriver();
                            dbManager.connectDB();
                            if (!listener.isAlive()) {
                                listener.start();
                            }
//                            ChatSocket chatSocket = new ChatSocket();
//                            chatSocket.getUserItem("");
                        }
                    });
                    frame.setVisible(true);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
