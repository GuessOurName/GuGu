package Util;

public class LoginMsg {
    private String username;
    private String password;

    public LoginMsg(){}
    public LoginMsg(String username,String password,String msgtype){
        this.username=username;
        this.password=password;
    }

    public void setUsername(String username){
        this.username=username;
    }

    public void setPassword(String password){
        this.password=password;
    }


    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }



    @Override
    public String toString() {
        return "User{" +
                "name='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

