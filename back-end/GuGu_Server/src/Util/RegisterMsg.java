package Util;

public class RegisterMsg {
    private int userId;
    private String userName;
    private String userPwd;

    public RegisterMsg(String userName, String userPwd) {
        this.userName = userName;
        this.userPwd = userPwd;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getUserPwd() {
        return this.userPwd;
    }
}
