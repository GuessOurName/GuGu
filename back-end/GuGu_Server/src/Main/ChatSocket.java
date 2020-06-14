package Main;

import DB.DBManager;
import Util.*;
import View.MainWindow;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import Util.LoginMsg;
import com.google.gson.reflect.TypeToken;


public class ChatSocket extends Thread {

    private String userId = "12";
    private Socket socket;
    private String message = null;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Connection connection = DBManager.getDBManager().getConnection();
    private SocketMsg socketMsg;
    private Gson gson = new Gson();
    private LoginMsg loginMsg;
    private List<UserItemMsg> userItemMsgList = new ArrayList<>();


    public ChatSocket() {
    }

    public ChatSocket(Socket s) {
        this.socket = s;
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF-8"));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            String line = null;
            String receiveMsgType = null;
            String msg = null;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println("The Server Receive line : " + line);
                if (!line.equals("-1")) {
                    if (receiveMsgType == null) {
                        receiveMsgType = line;
                    } else {
                        msg = line;
                    }
                } else {
                    delMessage(receiveMsgType, msg);
                    receiveMsgType = null;
                    msg = null;
                }
            }
        } catch (IOException e) {
            System.out.println("Error");
            e.printStackTrace();
        } finally {
            try {
                MainWindow.getMainWindow().setShowMsg(this.userId + " login out !");
                MainWindow.getMainWindow().removeOfflineUsers(this.userId);
                ChatManager.getChatManager().remove(socketMsg);
                bufferedWriter.close();
                bufferedReader.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void delMessage(String receiveMsgType, String msg) {
        if (receiveMsgType != null) {
            switch (receiveMsgType) {
                case "LOGIN": {
                    dealLogin(msg);
                    break;
                }
                case "REGISTER": {
                    dealRegister(msg);
                    break;
                }
                case "DRESSUP": {
                    dealDressUp(msg);
                    break;
                }
                case "GETDRESSUP": {
                    dealGetDressUp(msg);
                    break;
                }
                case "PROFILE": {
                    dealProfile(msg);
                    break;
                }
                case "GETPROFILE": {
                    dealGetProfile(msg);
                    break;
                }
                case "GETFRIENDLIST": {
                    dealGetFriendList(msg);
                    break;
                }
                case "GETGROUPLIST": {
                    System.out.println("Deal with grouplist!");
                    dealGetGroupList(msg);
                    System.out.println("Deal with grouplist done!");
                    break;
                }
                case "GETFRIENDPROFILE": {
                    dealGetFriendProfile(msg);
                    break;
                }
                case "STATE": {
                    dealState(msg);
                    break;
                }
                case "CHATMSG": {
                    dealChatMsg(msg);
                    break;
                }
                case "USERLIST": {
                    dealUserList(msg);
                    break;
                }
                case "ADDFRIEND": {
                    dealAddFriend(msg);
                    break;
                }
                case "GROUPMEMBERLIST": {
                    dealGroupMemberList(msg);
                    break;
                }
                case "ADDGROUP": {
                    dealAddGroup(msg);
                    break;
                }
                case "GETALLGROUPLIST": {
                    dealGetAllGroupList(msg);
                    break;
                }
                default:
                    dealError();
                    break;
            }
        }
    }

    public void sendMsg(String msg) {
        try {
            while (socket == null) ;
            if (bufferedWriter != null) {
                System.out.println("send :" + msg);
                bufferedWriter.write(msg + "\n");
                bufferedWriter.flush();
                bufferedWriter.write("-1" + "\n");
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMsg() {
        return message;
    }


    private void dealError() {
    }

    private void dealGetAllGroupList(String msg) {
        String out = null;
        String sql = "SELECT groupName FROM Groups;";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                out += "[ACKGETALLGROUPLIST]:[" + resultSet.getString(1) + "] ";
            }
            sendMsg(out);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dealAddGroup(String msg) {
    }

    private void dealGroupMemberList(String msg) {
    }

    private void dealAddFriend(String msg) {
    }

    private void dealUserList(String msg) {
    }

    private void dealChatMsg(String msg) {
        String chatObj = null;
        String content = null;
        String avatarID = null;
        String msgType = null;
        String p = "\\[CHATMSG\\]:\\[(.*), (.*), (.*), (.*)\\]";
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(msg);
        if (matcher.find()) {
            chatObj = matcher.group(1);
            content = matcher.group(2);
            avatarID = matcher.group(3);
            msgType = matcher.group(4);
        } else {
            return;
        }
        String out = null;
        String sqlGroup = "SELECT * FROM Groups WHERE groupName = '" + chatObj + "';";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlGroup);
            // gruop chat
            if (resultSet.next()) {
                // find all group members to send msg
                String sql = "SELECT groupMemberName FROM GROUPINFO WHERE groupName = '" + chatObj + "';";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    // if user is online , then send.
                    for (SocketMsg SocketMsg : ChatManager.getChatManager().socketList) {
                        if (SocketMsg.getUsername().equals(resultSet.getString(1)) && !SocketMsg.getUsername().equals(userId)) {
                            out = "[GETCHATMSG]:[" + userId + ", " + content + ", " + avatarID + ", Text, " + chatObj + "]";
                            SocketMsg.getChatSocket().sendMsg(out);
                        }
                    }
                }
                // private chat
            } else {
                for (SocketMsg socketManager : ChatManager.getChatManager().socketList) {
                    if (socketManager.getUsername().equals(chatObj)) {
                        out = "[GETCHATMSG]:[" + userId + ", " + content + ", " + avatarID + ", Text,  ]";
                        socketManager.getChatSocket().sendMsg(out);
                    }
                }
            }
            out = "[ACKCHATMSG]:[1]";
            sendMsg(out);
        } catch (SQLException e) {
            out = "[ACKCHATMSG]:[0]";
            sendMsg(out);
            e.printStackTrace();
        }
    }

    private void dealState(String msg) {
    }

    private void dealGetFriendProfile(String msg) {
        String friendName = null;
        String p = "\\[GETFRIENDPROFILE\\]:\\[(.*)\\]";
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(msg);
        if (matcher.find()) {
            friendName = matcher.group(1);
        } else {
            return;
        }
        String out = null;
        String sql = "SELECT avatar, sign, background, state FROM UserInfo WHERE username = '" + friendName + "';";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                out = "[ACKGETFRIENDPROFILE]:[" + resultSet.getString(1) + ", " + resultSet.getString(2) + ", "
                        + "" + resultSet.getString(3) + ", " + resultSet.getString(4) + "]";
                sendMsg(out);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dealGetGroupList(String msg) {
//        String sql = "SELECT groupName FROM GroupInfo WHERE groupMemberName = '" + userId + "';";
        String sqlGroupList = "SELECT GroupId FROM GroupInfo WHERE UserId = " + userId + ";\n";
        List<UserItemMsg> userItemMsgList = new ArrayList<UserItemMsg>();
        try {
            // 一个st只能对应一个rs
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlGroupList);
            while (resultSet.next()) {
                UserItemMsg userItemMsg = new UserItemMsg();
                userItemMsg.setItemType(1);
                int groupId = resultSet.getInt("GroupId");
                userItemMsg.setGroupId(String.valueOf(groupId));
                // 查询群名称和创建者
                String sqlGroups = "SELECT GroupName,CreaterId FROM Groups WHERE GroupId =" + groupId + ";\n";
                Statement statement1 = connection.createStatement();
                ResultSet resultSet1 = statement1.executeQuery(sqlGroups);
                if (resultSet1.next()) {
                    String groupName = resultSet1.getString("GroupName");
                    int createrId = resultSet1.getInt("CreaterId");
                    userItemMsg.setCreaterId(String.valueOf(createrId));
                    userItemMsg.setGroupName(groupName);
                }
                // 查询群成员
//                List<String> userList = new ArrayList<String>();
//                String sqlGroupUser = "SELECT UserId FROM GroupInfo WHERE GroupId =" + groupId + ";\n";
//                Statement statement2 = connection.createStatement();
//                ResultSet resultSet2 = statement2.executeQuery(sqlGroupUser);
//                while (resultSet2.next()) {
//                    int groupUserId = resultSet2.getInt("UserId");
//                    userList.add(String.valueOf(groupUserId));
//                }
//                userItemMsg.setGroupUserList(userList);
                userItemMsgList.add(userItemMsg);
            }
            resultSet.close();
            String asd = gson.toJson(userItemMsgList);
            System.out.println(asd);
//            GroupList g = gson.fromJson(msg,GroupList.class);
//            List<UserItemMsg> gg = gson.fromJson(asd, new TypeToken<List<UserItemMsg>>() {
//            }.getType());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dealGetFriendList(String msg) {
        String out = null;
        String sql = "SELECT friendsName FROM Friends WHERE username = " + userId + ";";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                out += "[ACKGETFRIENDLIST]:[" + resultSet.getString(1) + "] ";
            }
            sendMsg(out);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dealGetProfile(String msg) {
        String out = null;
        String sql = "SELECT sign FROM UserInfo WHERE username = '" + userId + "';";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                out = "[ACKGETPROFILE]:[" + resultSet.getString(1) + "]";
                sendMsg(out);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dealProfile(String msg) {
    }

    private void dealGetDressUp(String msg) {
        String out = null;
        String sql = "SELECT avatar, background FROM UserInfo WHERE username = '" + userId + "';";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                out = "[ACKGETDRESSUP]:[" + resultSet.getString(1) + ", " + resultSet.getString(2) + "]";
                sendMsg(out);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dealDressUp(String msg) {
        String acatarID = null;
        String backgroundID = null;
        String p = "\\[DRESSUP\\]:\\[(.*), (.*)\\]";
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(msg);
        if (matcher.find()) {
            acatarID = matcher.group(1);
            backgroundID = matcher.group(2);
        }
        System.out.println(acatarID + "   " + backgroundID);
        String sql = "UPDATE UserInfo SET avatar =  " + acatarID + ", background = " + backgroundID + " WHERE username = '" + userId + "'";
        try {
            Statement statement = connection.createStatement();
            if (statement.executeUpdate(sql) > 0) {
                sendMsg("[ACKDRESSUP]:[1]");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sendMsg("[ACKDRESSUP]:[0]");
    }

    private void dealRegister(String msg) {
        // 接收名称，密码，返回一个登录ID
        RegisterMsg regMsg = gson.fromJson(msg, RegisterMsg.class);
        System.out.println("Deal Register Message : " + regMsg.toString());
        String iuserName = regMsg.getUserName();
        String iPassword = regMsg.getUserPwd();
        System.out.println(iuserName);
        System.out.println(iPassword);
        String sqlregMsg = "INSERT INTO User(Pwd) VALUE('" + iPassword + "');";
        String sqluserId = "SELECT UserId FROM User ORDER BY UserId DESC LIMIT 1;";

        try {
            Statement statement = connection.createStatement();
            int resultReg = statement.executeUpdate(sqlregMsg);
            // 注册成功
            if (resultReg > 0) {
                System.out.println(iuserName + "register success!");
                ResultSet resultSet = statement.executeQuery(sqluserId);
                // 更新User表
                if (resultSet.next()) {
                    int userId = resultSet.getInt("UserId");
                    // 发送注册成功以及UserId
                    sendMsg("REGACK\n" + String.valueOf(userId));
                    // 更新UserInfo表
                    String sqluserInfo = "INSERT INTO UserInfo(UserId,UserName) VALUE('" + userId + "','" + iuserName + "');";
                    int resultUserInfo = statement.executeUpdate(sqluserInfo);
                }
            } else {
                System.out.println("Register error!");
                sendMsg("REGFAIL");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dealLogin(String msg) {
        // 处理登录账号和密码
        loginMsg = gson.fromJson(msg, LoginMsg.class);
        System.out.println("Deal Login Message : " + loginMsg.toString());
        String iuserId = loginMsg.getUsername();
        String iPassword = loginMsg.getPassword();

        System.out.println(iuserId);
        System.out.println(iPassword);
        // 根据Id查询密码
        String sqlPassword = "SELECT Pwd FROM User WHERE UserId = " + iuserId + ";";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlPassword);
            // 密码匹配
            if (resultSet.next() && iPassword.equals(resultSet.getString("Pwd"))) {
                sendMsg("ACKLOGIN");
                this.userId = iuserId;
                MainWindow.getMainWindow().setShowMsg(this.userId + " login in!");
                MainWindow.getMainWindow().addOnlineUsers(this.userId);
                socketMsg = new SocketMsg(this, this.userId);
                ChatManager.getChatManager().add(socketMsg);
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
