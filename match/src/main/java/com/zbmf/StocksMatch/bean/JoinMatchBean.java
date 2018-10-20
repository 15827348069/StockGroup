package com.zbmf.StocksMatch.bean;

import java.io.Serializable;

/**
 * Created by pq
 * on 2018/3/19.
 */

public class JoinMatchBean extends Erro implements Serializable {
    private String status;
    private Result result;

    public boolean getStatus() {
        return status.equals("ok");
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result implements Serializable{
        private int user_id;
        private int match_id;
        private String nickname;
        private String start_at;
        private String end_at;
        private String avatar;
        private String truename;
        private String mobile;
        private double total;
        private float init;
        private double position;
        private double unfrozen;
        private double frozen;
        private double deal;
        private float stocks_value;
        private double total_yield;
        private double day_yield;
        private double week_yield;
        private double month_yield;
        private int day_rank;
        private int week_rank;
        private int month_rank;
        private int total_rank;
        private int holds;
        private int records;//等于1，总收益率小鱼0 可用
        private int players;
        private int canusecard;
        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getMatch_id() {
            return match_id;
        }

        public void setMatch_id(int match_id) {
            this.match_id = match_id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
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

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getTruename() {
            return truename;
        }

        public void setTruename(String truename) {
            this.truename = truename;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public float getInit() {
            return init;
        }

        public void setInit(float init) {
            this.init = init;
        }

        public double getPosition() {
            return position;
        }

        public void setPosition(double position) {
            this.position = position;
        }

        public double getUnfrozen() {
            return unfrozen;
        }

        public void setUnfrozen(double unfrozen) {
            this.unfrozen = unfrozen;
        }

        public double getFrozen() {
            return frozen;
        }

        public void setFrozen(double frozen) {
            this.frozen = frozen;
        }

        public double getDeal() {
            return deal;
        }

        public void setDeal(double deal) {
            this.deal = deal;
        }

        public float getStocks_value() {
            return stocks_value;
        }

        public void setStocks_value(float stocks_value) {
            this.stocks_value = stocks_value;
        }

        public double getTotal_yield() {
            return total_yield;
        }

        public void setTotal_yield(double total_yield) {
            this.total_yield = total_yield;
        }

        public double getWeek_yield() {
            return week_yield;
        }

        public void setWeek_yield(double week_yield) {
            this.week_yield = week_yield;
        }

        public double getMonth_yield() {
            return month_yield;
        }

        public void setMonth_yield(double month_yield) {
            this.month_yield = month_yield;
        }

        public int getDay_rank() {
            return day_rank;
        }

        public void setDay_rank(int day_rank) {
            this.day_rank = day_rank;
        }

        public int getWeek_rank() {
            return week_rank;
        }

        public void setWeek_rank(int week_rank) {
            this.week_rank = week_rank;
        }

        public int getMonth_rank() {
            return month_rank;
        }

        public void setMonth_rank(int month_rank) {
            this.month_rank = month_rank;
        }

        public int getTotal_rank() {
            return total_rank;
        }

        public void setTotal_rank(int total_rank) {
            this.total_rank = total_rank;
        }

        public int getHolds() {
            return holds;
        }

        public void setHolds(int holds) {
            this.holds = holds;
        }

        public int getRecords() {
            return records;
        }

        public void setRecords(int records) {
            this.records = records;
        }

        public int getPlayers() {
            return players;
        }

        public void setPlayers(int players) {
            this.players = players;
        }

        public double getDay_yield() {
            return day_yield;
        }

        public void setDay_yield(double day_yield) {
            this.day_yield = day_yield;
        }

        public int getCanusecard() {
            return canusecard;
        }

        public void setCanusecard(int canusecard) {
            this.canusecard = canusecard;
        }
    }
}
