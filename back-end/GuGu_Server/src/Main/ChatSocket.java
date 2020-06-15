package Main;

import DB.DBManager;
import Util.*;
import View.MainWindow;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import Util.LoginMsg;
import Util.UserItemMsg;


public class ChatSocket extends Thread {

    private String userId;
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
                case "LOGIN": { //登录
                    dealLogin(msg);
                    break;
                }
                case "REGISTER": {  //注册
                    dealRegister(msg);
                    break;
                }
                case "GETUSERITEM": {   //联系人、群组
                    getUserItem(msg);
                    break;
                }
                case "CHATMSG": {   // 转发聊天信息
                    dealChatMsg(msg);
                    break;
                }
                case "ADDFRIEND": { // 添加好友
                    dealAddFriend(msg);
                    break;
                }
                case "ADDGROUP": {  // 添加群组
                    dealAddGroup(msg);
                    break;
                }
                case "ADDMOMENTMSG": { // 发送朋友圈
                    dealAddMoment(msg);
                }
                case "GETMOMENTMSG": {  // 获取朋友圈
                    dealGetMoment();
                }
                default:
//                    dealError();    // 无法识别
                    break;
            }
        }
    }

    public void sendMsg(String msgType, String msg) {
        try {
            while (socket == null) ;
            if (bufferedWriter != null) {
                System.out.println("sending..." + msg);
                bufferedWriter.write(msgType + "\n");
                bufferedWriter.flush();
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
        String error = "Server cant resolve messageType!";
        sendMsg("ERROR", error);
    }


    private void dealAddGroup(String msg) {
        String sqlSearchGroupById = "SELECT * FROM Groups WHERE GroupId=" + msg + ";\n";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlSearchGroupById);
            if (resultSet.next()) {
                String sqlIsGroup = "SELECT * FROM GroupInfo WHERE UserId=" + userId + " and GroupId=" + msg + ";\n";
                Statement statement1 = connection.createStatement();
                ResultSet resultSet1 = statement1.executeQuery(sqlIsGroup);
                if (!resultSet1.next()) {
                    String sqlAddGroup = "INSERT INTO GroupInfo VALUE(" + msg + "," + userId + ");\n";
                    Statement statement2 = connection.createStatement();
                    int result1 = statement2.executeUpdate(sqlAddGroup);
                    sendMsg("ACKADDGROUP", "1");
                } else {
                    sendMsg("ADDGROUPFAIL", "0");
                }
            } else {
                sendMsg("ADDGROUPFAIL", "0");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dealAddMoment(String msg) {
        MomentMsg momentMsg = gson.fromJson(msg, MomentMsg.class);
        String from = momentMsg.getUserId();
        int likes = momentMsg.getGood_num();
        String content = momentMsg.getMoment();
        Date curTime = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = simpleDateFormat.format(curTime);
        String sqlMoment = "INSERT INTO Moments(UserId,Likes,Msg,MomentTime) VALUE(" + from + "," + likes + ",'" + content + "','" + time + "');\n";
        try {
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(sqlMoment);
            if (result > 0) {
                sendMsg("ACKADDMOMENT", "1");
            } else {
                sendMsg("ADDMOMENTFAIL", "0");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void dealGetMoment() {
        List<MomentMsg> momentMsgList = new ArrayList<>();
        String sqlgetMoment = "SELECT distinct  Friends.UserId,Likes,Msg,MomentTime FROM Friends,Moments WHERE (Friends.UserId=" + userId + " and Friends.FriendId=Moments.UserId and Moments.UserId = Friends.FriendId) or (Moments.UserId = "+userId+" and Moments.UserId = Friends.UserId);\n";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlgetMoment);
            while (resultSet.next()) {
                MomentMsg momentMsg = new MomentMsg();
                momentMsg.setGood(resultSet.getInt("Likes"));
                momentMsg.setMoment(resultSet.getString("Msg"));
                momentMsg.setUserId(String.valueOf(resultSet.getInt("UserId")));
                momentMsg.setTime(resultSet.getString("MomentTime"));
                momentMsgList.add(momentMsg);
            }
            String moments = gson.toJson(momentMsgList);
            sendMsg("ACKGETMOMENT", moments);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dealAddFriend(String msg) {
        // 根据id查找
        String sqlSearchFriendById = "SELECT * FROM UserInfo WHERE UserId =" + msg + ";\n";
        // 根据UserName查找
        String sqlSearchFriendByName = "SELECT * FROM UserInfo WHERE UserName ='" + msg + "';\n";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlSearchFriendById);
            if (resultSet.next()) {
                String sqlIsFriend = "SELECT * FROM Friends WHERE UserId=" + userId + " and FriendId=" + msg + ";\n";
                Statement statement1 = connection.createStatement();
                ResultSet resultSet1 = statement1.executeQuery(sqlIsFriend);
                if (!resultSet1.next()) {
                    String sqlAddFriend1 = "INSERT INTO Friends VALUE(" + userId + "," + msg + ");\n";
                    String sqlAddFriend2 = "INSERT INTO Friends VALUE(" + msg + "," + userId + ");\n";
                    Statement statement2 = connection.createStatement();
                    int result1 = statement2.executeUpdate(sqlAddFriend1);
                    int result2 = statement2.executeUpdate(sqlAddFriend2);
                    sendMsg("ACKADDFRIEND", "1");
                } else {
                    sendMsg("ADDFRIENDFAIL", "0");
                }
            } else {
                sendMsg("ADDFRIENDFAIL", "0");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void dealChatMsg(String msg) {
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ChatMsg chatMsg = gson.fromJson(msg, ChatMsg.class);
        String from = chatMsg.getUsername();
        String target = chatMsg.getChatObj();
        String content = chatMsg.getContent();
        Date curTime = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        chatMsg.setMyInfo(false);
        String transmit = gson.toJson(chatMsg);

        if (chatMsg.getIsGroup() == 1) {
            String sqlMsgInsert = "INSERT INTO GroupChatTxt VALUE(" + Integer.valueOf(target) + "," + Integer.valueOf(from) + ",'" + content + "','" + simpleDateFormat.format(curTime) + "');\n";
            try {
                Statement statement = connection.createStatement();
                int result = statement.executeUpdate(sqlMsgInsert);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String sqlGroup = "SELECT * FROM GroupInfo WHERE GroupId = " + target + ";\n";
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlGroup);
                while (resultSet.next()) {
                    // 寻找在线用户发送
                    for (SocketMsg SocketMsg : ChatManager.getChatManager().socketList) {
                        if (SocketMsg.getUsername().equals(resultSet.getString("UserId")) && !SocketMsg.getUsername().equals(userId)) {
                            SocketMsg.getChatSocket().sendMsg("CHATMSG", transmit);
                        }
                    }
                    sendMsg("ACKGROUPMSG", "1");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // 插入记录
            String sqlMsgInsert = "INSERT INTO ChatTxt VALUE(" + Integer.valueOf(target) + "," + Integer.valueOf(from) + ",'" + content + "','" + simpleDateFormat.format(curTime) + "');\n";
            try {
                Statement statement = connection.createStatement();
                for (SocketMsg SocketMsg : ChatManager.getChatManager().socketList) {
                    if (SocketMsg.getUsername().equals(target) && !SocketMsg.getUsername().equals(userId)) {
                        SocketMsg.getChatSocket().sendMsg("CHATMSG", transmit);
                        break;
                    }
                }
                int result = statement.executeUpdate(sqlMsgInsert);
                if (result > 0) {
                    sendMsg("ACKCHATMSG", "1");
                } else {
                    sendMsg("CHATMSGFAIL", "-1");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
    }


    public void getUserItem(String msg) {
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
//            String asd = gson.toJson(userItemMsgList);
//            System.out.println(asd);
//            GroupList g = gson.fromJson(msg,GroupList.class);
//            List<UserItemMsg> gg = gson.fromJson(asd, new TypeToken<List<UserItemMsg>>() {
//            }.getType());
            String sqlFriends = "SELECT FriendId FROM Friends WHERE UserId=" + userId + ";\n";
            resultSet = statement.executeQuery(sqlFriends);
            while (resultSet.next()) {
                UserItemMsg userItemMsg = new UserItemMsg();
                userItemMsg.setItemType(2);
                int friendId = resultSet.getInt("FriendId");
                userItemMsg.setUserId(String.valueOf(friendId));
                String sqlFriendInfo = "SELECT UserName,AvatarPath,Sign FROM UserInfo WHERE UserId=" + friendId + ";\n";
                Statement statement1 = connection.createStatement();
                ResultSet resultSet1 = statement1.executeQuery(sqlFriendInfo);
                if (resultSet1.next()) {
                    String friendName = resultSet1.getString("UserName");
                    String friendAvatarPath = resultSet1.getString("AvatarPath");
                    String friendSign = resultSet1.getString("Sign");
                    userItemMsg.setUserName(friendName);
                    userItemMsg.setAvatarPath(friendAvatarPath);
                    userItemMsg.setSign(friendSign);
                }
                userItemMsgList.add(userItemMsg);
            }
            msg = gson.toJson(userItemMsgList);
            System.out.println("Send : " + msg);
            sendMsg("USERITEM", msg);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                    sendMsg("REGACK", String.valueOf(userId));
                    // 更新UserInfo表
                    String sqluserInfo = "INSERT INTO UserInfo(UserId,UserName) VALUE('" + userId + "','" + iuserName + "');";
                    int resultUserInfo = statement.executeUpdate(sqluserInfo);
                }
            } else {
                System.out.println("Register error!");
                sendMsg("REGFAIL", "0");
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

        // 根据Id查询密码
        String sqlPassword = "SELECT Pwd FROM User WHERE UserId = " + iuserId + ";";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlPassword);
            // 密码匹配
            if (resultSet.next() && iPassword.equals(resultSet.getString("Pwd"))) {
                sendMsg("ACKLOGIN", "1");
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
