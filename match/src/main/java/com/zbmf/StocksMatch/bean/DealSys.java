package com.zbmf.StocksMatch.bean;

import java.io.Serializable;

/**
 * Created by xuhao
 * on 2017/10/25.
 */

public class DealSys extends BaseBean implements Serializable {
    private int user_id;
    private int action;
    private String avatar;
    private int type;
    private String posted_at;
    private String nickname;
    private String user_img;
    private String date;
    private String stock_name;
    private String symbol;
    private String count_comments;
    private double price;
    private double gain_yield;
    private int volumn_total;
    private String name;
//    private long created_at;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public long getCreated_at() {
//        return created_at;
//    }
//
//    public void setCreated_at(long created_at) {
//        this.created_at = created_at;
//    }

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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
//
    public String getAction() {
        String str="买入";
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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public DealSys(int user_id, int action, String nickname, String user_img, String date, String stock_name, String sumbol, String count_comments, double price, double gain_yield, int volumn_total) {
        this.user_id = user_id;
//        this.action = action;
        this.nickname = nickname;
        this.user_img = user_img;
        this.date = date;
        this.stock_name = stock_name;
        this.symbol = sumbol;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPosted_at() {
        return posted_at;
    }

    public void setPosted_at(String posted_at) {
        this.posted_at = posted_at;
    }
}
