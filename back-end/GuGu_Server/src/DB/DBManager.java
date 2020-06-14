package DB;

import View.MainWindow;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {

    private static final DBManager dbManager = new DBManager();
    private static Connection connection = null;
    private static final String url = "jdbc:mysql://175.24.74.92:3306/Gugu_Test1?"
            + "useUnicode=true&useJDBCCompliantTimezoneShift=true&"
            + "useLegacyDatetimeCode=false&serverTimezone=UTC";

//    private static final String url = "jdbc:mysql://localhost/zzchats?"
//            + "useUnicode=true&useJDBCCompliantTimezoneShift=true&"
//            + "useLegacyDatetimeCode=false&serverTimezone=UTC";



    private Statement statement;

    public static DBManager getDBManager() {
        return dbManager;
    }

    public Connection getConnection() {
        return connection;
    }

    private DBManager() { }

    public void addDBDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            MainWindow.getMainWindow().setShowMsg("load mysql driver success");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            MainWindow.getMainWindow().setShowMsg("load mysql driver failed");
            e.printStackTrace();
        }
    }


    public void connectDB() {
        try {
            connection = DriverManager.getConnection(url, "root", "123456");
            MainWindow.getMainWindow().setShowMsg("connect gugu mysql success");

        } catch (SQLException e) {
            MainWindow.getMainWindow().setShowMsg("connect gugu mysql failed");
            e.printStackTrace();
        }
    }
}
