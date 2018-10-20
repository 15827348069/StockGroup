package com.zbmf.StockGroup.beans;

/**
 * Created by pq
 * on 2018/7/9.
 */

public class DzUser {
    private int id;
    private String nickname;
    private String avatar;
    private String created_at;

    public DzUser(int id, String nickname, String avatar, String created_at) {
        this.id = id;
        this.nickname = nickname;
        this.avatar = avatar;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
