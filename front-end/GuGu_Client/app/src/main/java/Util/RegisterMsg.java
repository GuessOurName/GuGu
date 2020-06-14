package Util;

public class RegisterMsg {
    private String userName;
    private String userPwd;

    public RegisterMsg(String userName, String userPwd) {
        this.userName = userName;
        this.userPwd = userPwd;
    }


    public String getUserName() {
        return this.userName;
    }

    public String getUserPwd() {
        return this.userPwd;
    }
}
