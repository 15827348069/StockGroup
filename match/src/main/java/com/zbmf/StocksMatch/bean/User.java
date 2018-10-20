package com.zbmf.StocksMatch.bean;

import java.io.Serializable;

/**
 * Created by xuhao
 * on 2017/11/23.
 */
public class User extends BaseBean implements Serializable{
    private int user_id;
    private String username;
    private String nickname;
    private String truename;
    private String avatar;
    private String role;
    private String phone;
    private double mpay;
    private String idcard;

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", truename='" + truename + '\'' +
                ", avatar='" + avatar + '\'' +
                ", role='" + role + '\'' +
                ", phone='" + phone + '\'' +
                ", idcard='" + idcard + '\'' +
                '}';
    }

    public double getMpay() {
        return mpay;
    }

    public void setMpay(double mpay) {
        this.mpay = mpay;
    }

    public int getUser_id() {
        return this.user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getNickname() {
        return this.nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getTruename() {
        return this.truename;
    }
    public void setTruename(String truename) {
        this.truename = truename;
    }
    public String getAvatar() {
        return this.avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getRole() {
        return this.role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getIdcard() {
        return this.idcard;
    }
    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }
}
