package com.zbmf.StocksMatch.beans;

import java.util.List;

public class User extends General{
    private String phone;
    private String nickname;
    private String fans;
    private String group_id;
    private String enable_fans;
    private String auth_token;
    private String sms;
    private String avatar;
    private List<String> advanceds;
    private String username;
    private String mpay;
    private String gid;
    private String role;
    private String user_id;
    private boolean account;

    private String count_fens;
    private List<MatchBean> matches;
    private String is_focus;

    public String getIs_focus() {
        return is_focus;
    }

    public void setIs_focus(String is_focus) {
        this.is_focus = is_focus;
    }

    public List<MatchBean> getMatches() {
        return matches;
    }

    public void setMatches(List<MatchBean> matches) {
        this.matches = matches;
    }

    public String getCount_fens() {
        return count_fens;
    }

    public void setCount_fens(String count_fens) {
        this.count_fens = count_fens;
    }

    private List<User> list;

    public boolean isAccount() {
        return account;
    }

    public void setAccount(boolean account) {
        this.account = account;
    }

    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFans() {
        return fans;
    }

    public void setFans(String fans) {
        this.fans = fans;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getEnable_fans() {
        return enable_fans;
    }

    public void setEnable_fans(String enable_fans) {
        this.enable_fans = enable_fans;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<String> getAdvanceds() {
        return advanceds;
    }

    public void setAdvanceds(List<String> advanceds) {
        this.advanceds = advanceds;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMpay() {
        return mpay;
    }

    public void setMpay(String mpay) {
        this.mpay = mpay;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
