package Util;

import java.util.ArrayList;
import java.util.List;

public class ChatMsg {

    private boolean myInfo;
    private int iconID;
    // 发送者
    private String username;
    private String content;
    // 接收者
    private String chatObj;


    private int isGroup;

    public int getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(int isGroup) {
        this.isGroup = isGroup;
    }

    public static List<ChatMsg> chatMsgList = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIconID() {
        return iconID;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }

    public boolean isMyInfo() {
        return myInfo;
    }

    public void setMyInfo(boolean myInfo) {
        this.myInfo = myInfo;
    }

    public String getChatObj() {
        return chatObj;
    }

    public void setChatObj(String chatObj) {
        this.chatObj = chatObj;
    }
}

