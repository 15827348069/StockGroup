package com.zbmf.groupro.beans;

/**
 * Created by xuhao on 2016/12/27.
 */

public class LiveTypeMessage {
    private String message_type;
    private String message;
    private int box_leaver;
    public int getBox_leaver() {
        return box_leaver;
    }

    public void setBox_leaver(int box_leaver) {
        this.box_leaver = box_leaver;
    }
    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
