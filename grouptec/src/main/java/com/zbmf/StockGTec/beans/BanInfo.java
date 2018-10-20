package com.zbmf.StockGTec.beans;

import java.util.List;

/**
 * Created by iMac on 2017/1/16.
 */

public class BanInfo {

    /**
     * user_id : 1791
     * nickname : sms
     * time : 2017-01-16 10:39:21
     */

    private String user_id;
    private String nickname;
    private String time;
    private List<BanInfo> infos;

    public void setInfos(List<BanInfo> infos) {
        this.infos = infos;
    }

    public List<BanInfo> getInfos() {
        return infos;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
