package com.zbmf.StockGTec.beans;

import java.io.Serializable;

/**
 * Created by xuhao on 2016/12/16.
 */

public class LiveMessage   implements  Serializable{
    private String message_name;
    private long message_time;
    private String message_countent;
    private String img_url;
    private String group_id;
    private String message_or_img;

    private boolean show_item_anim;
    private String message_type;
    private int live_message_type;//直播消息类型

    private String box_name;//宝盒名字
    private String box_id;//宝盒ID
    private String box_user_name;//宝盒创建者名字
    private String buy_box_username;//订阅者名字
    private int action;//宝盒动作

    private GiftBean giftbean;

    private String thumb;//缩略图地址
    private String url;//原图地址

    private String msg_id;

    private String red_id;
    private String red_message;
    private int importent;

    private String blog_id;
    private String user_id;
    private String blog_name;


    public String getBlog_id() {
        return blog_id;
    }

    public void setBlog_id(String blog_id) {
        this.blog_id = blog_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBlog_name() {
        return blog_name;
    }

    public void setBlog_name(String blog_name) {
        this.blog_name = blog_name;
    }

    public int getImportent() {
        return importent;
    }

    public void setImportent(int importent) {
        this.importent = importent;
    }

    public String getRed_id() {
        return red_id;
    }

    public void setRed_id(String red_id) {
        this.red_id = red_id;
    }

    public String getRed_message() {
        return red_message;
    }

    public void setRed_message(String red_message) {
        this.red_message = red_message;
    }

    public String getMessage_or_img() {
        return message_or_img;
    }

    public void setMessage_or_img(String message_or_img) {
        this.message_or_img = message_or_img;
    }

    public String getMsg_id() {
        return msg_id;
    }
    public String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public GiftBean getGiftbean() {
        return giftbean;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setGiftbean(GiftBean giftbean) {
        this.giftbean = giftbean;
    }

    public String getBox_name() {
        return box_name;
    }

    public void setBox_name(String box_name) {
        this.box_name = box_name;
    }

    public String getBuy_box_username() {
        return buy_box_username;
    }

    public void setBuy_box_username(String buy_box_username) {
        this.buy_box_username = buy_box_username;
    }

    public String getBox_user_name() {
        return box_user_name;
    }

    public void setBox_user_name(String box_user_name) {
        this.box_user_name = box_user_name;
    }

    public String getBox_id() {
        return box_id;
    }

    public void setBox_id(String box_id) {
        this.box_id = box_id;
    }

    public int getLive_message_type() {
        return live_message_type;
    }

    public void setLive_message_type(int live_message_type) {
        this.live_message_type = live_message_type;
    }

    public boolean isShow_item_anim() {
        return show_item_anim;
    }

    public void setShow_item_anim(boolean show_item_anim) {
        this.show_item_anim = show_item_anim;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getMessage_name() {
        return message_name;
    }

    public void setMessage_name(String message_name) {
        this.message_name = message_name;
    }

    public String getMessage_countent() {
        return message_countent;
    }

    public void setMessage_countent(String message_countent) {
        this.message_countent = message_countent;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public long getMessage_time() {
        return message_time;
    }

    public void setMessage_time(long message_time) {
        this.message_time = message_time;
    }
}
