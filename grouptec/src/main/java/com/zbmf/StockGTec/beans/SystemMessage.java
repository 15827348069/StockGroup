package com.zbmf.StockGTec.beans;

/**
 * Created by xuhao on 2017/3/6.
 */

public class SystemMessage {
    private String avatar;
    private String message_type;
    private String date;
    private String countent;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getCountent() {
        return countent;
    }

    public void setCountent(String countent) {
        this.countent = countent;
    }
}
