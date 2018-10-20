package com.zbmf.StockGroup.beans;

import java.io.Serializable;

/**
 * Created by xuhao on 2017/10/25.
 */

public class DealSys implements Serializable{
    private String user_id;
    private int action;
    private String nickname;
    private String user_img;
    private String avatar;
    private String posted_at;
    private String date;
    private String stock_name;
    private String sumbol;
    private String count_comments;
    private double price;
    private double gain_yield;
    private int volumn_total;

    public int getVolumn_total() {
        return volumn_total;
    }

    public void setVolumn_total(int volumn_total) {
        this.volumn_total = volumn_total;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getGain_yield() {
        return gain_yield;
    }

    public void setGain_yield(double gain_yield) {
        this.gain_yield = gain_yield;
    }

    public String getCount_comments() {
        return count_comments;
    }

    public void setCount_comments(String count_comments) {
        this.count_comments = count_comments;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAction() {
        String str="";
        switch (action){
            case 0:
            case 2:
                str="  买入  ";
                break;
            case 3:
                str="  卖出  ";
                break;
        }
        return str;
    }

    public DealSys(String stock_name, String sumbol){
        this.stock_name = stock_name;
        this.sumbol = sumbol;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    public String getSumbol() {
        return sumbol;
    }

    public void setSumbol(String sumbol) {
        this.sumbol = sumbol;
    }

    public DealSys(String user_id, int action, String nickname, String user_img, String date,
                   String stock_name, String sumbol /*,String count_comments, double price, double gain_yield, int volumn_total*/) {
        this.user_id = user_id;
        this.action = action;
        this.nickname = nickname;
        this.user_img = user_img;
        this.date = date;
        this.stock_name = stock_name;
        this.sumbol = sumbol;
//        this.count_comments = count_comments;
//        this.price = price;
//        this.gain_yield = gain_yield;
//        this.volumn_total = volumn_total;
    }
    public DealSys(int action,String date, String stock_name, String sumbol){
        this.action = action;
        this.date = date;
        this.stock_name = stock_name;
        this.sumbol = sumbol;
    }
    public DealSys(String user_id, int action, String nickname, String user_img, String date,
                   String stock_name, String sumbol ,String count_comments, double price, double gain_yield, int volumn_total) {
        this.user_id = user_id;
        this.action = action;
        this.nickname = nickname;
        this.user_img = user_img;
        this.date = date;
        this.stock_name = stock_name;
        this.sumbol = sumbol;
        this.count_comments = count_comments;
        this.price = price;
        this.gain_yield = gain_yield;
        this.volumn_total = volumn_total;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPosted_at() {
        return posted_at;
    }

    public void setPosted_at(String posted_at) {
        this.posted_at = posted_at;
    }
}
