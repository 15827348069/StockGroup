package com.zbmf.StockGroup.beans;

import java.io.Serializable;

/**
 * Created by xuhao on 2017/2/20.
 */

public class BlogBean implements Serializable{
    private String img;
    private String avatar;
    private String title;
    private String name;
    private String date;
    private String look_number;
    private String pinglun;
    private String app_link;
    private String wap_link;
    private String blog_id;
    private boolean is_myself;
    private boolean is_collects;
    private boolean is_delete;

    public boolean is_delete() {
        return is_delete;
    }

    public void setIs_delete(boolean is_delete) {
        this.is_delete = is_delete;
    }

    public boolean is_collects() {
        return is_collects;
    }

    public void setIs_collects(boolean is_collects) {
        this.is_collects = is_collects;
    }

    public String getBlog_id() {
        return blog_id;
    }

    public void setBlog_id(String blog_id) {
        this.blog_id = blog_id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLook_number() {
        return look_number;
    }

    public void setLook_number(String look_number) {
        this.look_number = look_number;
    }

    public String getPinglun() {
        return pinglun;
    }

    public void setPinglun(String pinglun) {
        this.pinglun = pinglun;
    }

    public boolean is_myself() {
        return is_myself;
    }

    public void setIs_myself(boolean is_myself) {
        this.is_myself = is_myself;
    }

    public String getWap_link() {
        return wap_link;
    }

    public void setWap_link(String wap_link) {
        this.wap_link = wap_link;
    }

    public String getApp_link() {
        return app_link;
    }

    public void setApp_link(String app_link) {
        this.app_link = app_link;
    }
}
