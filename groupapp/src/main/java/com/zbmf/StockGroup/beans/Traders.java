package com.zbmf.StockGroup.beans;

import java.io.Serializable;

/**
 * Created by xuhao on 2017/11/9.
 */

public class Traders implements Serializable{
    private int rank;
    private String user_id;
    private String nickname;
    private String avatar;
    private double total_yield;
    private double deal_success;
    private String deal_total;
    private String joint_at;
    private double price_month;
    private String profile;
    private String expired_at;

    private boolean isMore;

    public Traders(boolean isMore) {
        this.isMore = isMore;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public String getExpired_at() {
        return expired_at;
    }

    public void setExpired_at(String expired_at) {
        this.expired_at = expired_at;
    }

    public String getJoint_at() {
        return joint_at;
    }

    public void setJoint_at(String joint_at) {
        this.joint_at = joint_at;
    }

    public double getPrice_month() {
        return price_month;
    }

    public void setPrice_month(double price_month) {
        this.price_month = price_month;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
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

    public double getTotal_yield() {
        return total_yield;
    }

    public void setTotal_yield(double total_yield) {
        this.total_yield = total_yield;
    }

    public double getDeal_success() {
        return deal_success;
    }

    public void setDeal_success(double deal_success) {
        this.deal_success = deal_success;
    }

    public String getDeal_total() {
        return deal_total;
    }

    public void setDeal_total(String deal_total) {
        this.deal_total = deal_total;
    }

    public Traders(int rank, String user_id, String nickname, String avatar, double total_yield, double deal_success, String deal_total, String joint_at, double price_month, String profile, String expired_at) {
        this.rank = rank;
        this.user_id = user_id;
        this.nickname = nickname;
        this.avatar = avatar;
        this.total_yield = total_yield;
        this.deal_success = deal_success;
        this.deal_total = deal_total;
        this.joint_at = joint_at;
        this.price_month = price_month;
        this.profile = profile;
        this.expired_at = expired_at;
    }
}
