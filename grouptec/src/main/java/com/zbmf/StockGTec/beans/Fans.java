package com.zbmf.StockGTec.beans;

import java.util.List;

/**
 * Created by iMac on 2017/3/3.
 */

public class Fans {

    /**
     user_id	number	用户ID
     nickname	string	用户昵称
     avatar	string	用户头像
     fans_level	number	铁粉等级
     join_at	string	加入时间
     expire_at	string	到期时间
     */
    private String user_id;
    private String nickname;
    private String avatar;
    private int fans_level;
    private String join_at;
    private String expire_at;
    public int pages;
    private List<Fans> list;

    public void setList(List<Fans> list) {
        this.list = list;
    }

    public List<Fans> getList() {
        return list;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getFans_level() {
        return fans_level;
    }

    public void setFans_level(int fans_level) {
        this.fans_level = fans_level;
    }

    public String getJoin_at() {
        return join_at;
    }

    public void setJoin_at(String join_at) {
        this.join_at = join_at;
    }

    public String getExpire_at() {
        return expire_at;
    }

    public void setExpire_at(String expire_at) {
        this.expire_at = expire_at;
    }
}
