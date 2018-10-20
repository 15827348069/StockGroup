package com.zbmf.StockGroup.beans;

/**
 * Created by xuhao on 2017/11/8.
 */

public class Dictum {
    private String dictum_id;
    private String nickname;
    private String avatar;
    private String tags;
    private int online_status;
    private int dictum_num;
    private long showtime;
    private String user_advice;
    private String zbmf_advice;
    private long created_at;
    private int dictum_total;
    private String user_id;

    public String getDictum_id() {
        return dictum_id;
    }

    public void setDictum_id(String dictum_id) {
        this.dictum_id = dictum_id;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getOnline_status() {
        return online_status;
    }

    public void setOnline_status(int online_status) {
        this.online_status = online_status;
    }

    public int getDictum_num() {
        return dictum_num;
    }

    public void setDictum_num(int dictum_num) {
        this.dictum_num = dictum_num;
    }

    public long getShowtime() {
        return showtime;
    }

    public void setShowtime(long showtime) {
        this.showtime = showtime;
    }

    public String getUser_advice() {
        return user_advice;
    }

    public void setUser_advice(String user_advice) {
        this.user_advice = user_advice;
    }

    public String getZbmf_advice() {
        return zbmf_advice;
    }

    public void setZbmf_advice(String zbmf_advice) {
        this.zbmf_advice = zbmf_advice;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public int getDictum_total() {
        return dictum_total;
    }

    public void setDictum_total(int dictum_total) {
        this.dictum_total = dictum_total;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Dictum(String dictum_id, String nickname, String avatar, String tags, int online_status, int dictum_num, long showtime, String user_advice, String zbmf_advice, long created_at, int dictum_total, String user_id) {
        this.dictum_id = dictum_id;
        this.nickname = nickname;
        this.avatar = avatar;
        this.tags = tags;
        this.online_status = online_status;
        this.dictum_num = dictum_num;
        this.showtime = showtime;
        this.user_advice = user_advice;
        this.zbmf_advice = zbmf_advice;
        this.created_at = created_at;
        this.dictum_total = dictum_total;
        this.user_id = user_id;
    }
}
