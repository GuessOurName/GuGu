package Util;

import java.util.ArrayList;
import java.util.List;

public class MomentMsg {
    private int iconID;
    private String userId;
    private String moment;
    private int good;
    private int good_num;
    private String time;

    public int getGood_num() {
        return good_num;
    }

    public void setGood_num(int good_num) {
        this.good_num = good_num;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIconID() {
        return iconID;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMoment() {
        return moment;
    }

    public void setMoment(String moment) {
        this.moment = moment;
    }

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }
}
