package com.zbmf.StockGroup.beans;

import java.io.Serializable;

/**
 * Created by pq
 * on 2018/6/6.
 */

public class Rank implements Serializable {
    private int week_rank;
    private String symbol;
    private String stock_name;
    private double week_yield;
    private double week_score;
    private int user_id;
    private String nickname;
    private String avatar;
    private String reason;

    private String start_at;
    private String end_at;
    private int round;
    private double high_yield;

    public Rank(int week_rank, String symbol, String stock_name, double week_yield,
                double week_score, int user_id, String nickname, String avatar, String reason) {
        this.week_rank = week_rank;
        this.symbol = symbol;
        this.stock_name = stock_name;
        this.week_yield = week_yield;
        this.week_score = week_score;
        this.user_id = user_id;
        this.nickname = nickname;
        this.avatar = avatar;
        this.reason = reason;
    }

    public Rank(int week_rank, String symbol, String stock_name, double week_yield,
                double week_score, String reason, String start_at, String end_at, int round,double high_yield) {
        this.week_rank = week_rank;
        this.symbol = symbol;
        this.stock_name = stock_name;
        this.week_yield = week_yield;
        this.week_score = week_score;
        this.reason = reason;
        this.start_at = start_at;
        this.end_at = end_at;
        this.round = round;
        this.high_yield=high_yield;
    }

    public Rank(int week_rank, String symbol, String stock_name, double week_yield,
                double week_score, int user_id, String nickname, String avatar, String reason,
                double high_yield
                /*String start_at,String end_at,int round*/) {
        this.week_rank = week_rank;
        this.symbol = symbol;
        this.stock_name = stock_name;
        this.week_yield = week_yield;
        this.week_score = week_score;
        this.user_id = user_id;
        this.nickname = nickname;
        this.avatar = avatar;
        this.reason = reason;
        this.high_yield = high_yield;
    /*    this.start_at=start_at;
        this.end_at=end_at;
        this.round=round;*/
    }
    public Rank(int week_rank, String symbol, String stock_name, double week_yield,
                double week_score, int user_id, String nickname, String avatar, String reason,
                double high_yield
                ,String start_at,String end_at,int round) {
        this.week_rank = week_rank;
        this.symbol = symbol;
        this.stock_name = stock_name;
        this.week_yield = week_yield;
        this.week_score = week_score;
        this.user_id = user_id;
        this.nickname = nickname;
        this.avatar = avatar;
        this.reason = reason;
        this.high_yield = high_yield;
        this.start_at=start_at;
        this.end_at=end_at;
        this.round=round;
    }

    public int getWeek_rank() {
        return week_rank;
    }

    public void setWeek_rank(int week_rank) {
        this.week_rank = week_rank;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    public double getWeek_yield() {
        return week_yield;
    }

    public void setWeek_yield(double week_yield) {
        this.week_yield = week_yield;
    }

    public double getWeek_score() {
        return week_score;
    }

    public void setWeek_score(double week_score) {
        this.week_score = week_score;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStart_at() {
        return start_at;
    }

    public void setStart_at(String start_at) {
        this.start_at = start_at;
    }

    public String getEnd_at() {
        return end_at;
    }

    public void setEnd_at(String end_at) {
        this.end_at = end_at;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public double getHigh_yield() {
        return high_yield;
    }

    public void setHigh_yield(double high_yield) {
        this.high_yield = high_yield;
    }
}
