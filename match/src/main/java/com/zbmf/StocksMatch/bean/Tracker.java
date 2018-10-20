package com.zbmf.StocksMatch.bean;

/**
 * Created by xuhao
 * on 2017/12/19.
 */

public class Tracker {
    private int user_id;
    private int is_track;
    private String expired_at;
    private double mpays;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getIs_track() {
        return is_track;
    }

    public void setIs_track(int is_track) {
        this.is_track = is_track;
    }

    public String getExpired_at() {
        return expired_at;
    }

    public void setExpired_at(String expired_at) {
        this.expired_at = expired_at;
    }

    public double getMpays() {
        return mpays;
    }

    public void setMpays(double mpays) {
        this.mpays = mpays;
    }
}
