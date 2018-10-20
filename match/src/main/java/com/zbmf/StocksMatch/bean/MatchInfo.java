package com.zbmf.StocksMatch.bean;

import java.io.Serializable;

/**
 * 具体比赛信息
 * Created by kubo on 2015/12/30.
 */
public class MatchInfo extends Erro implements Serializable{
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

    public static class Result implements Serializable{
        private int user_id;
        private int match_id;
        private double unfrozen;

        private int id;
        private String nickname;
        private String avatar;
        private String role;
        private String truename;
        private String match_username;
        private String mobile;
        private String updated_at;
        private double total;
        private String init;
        private double position;
        private double moneyunfrozen;//可用资产
        private double frozen;//冻结
        private String deal;//交易
        private int players;

        private double stocks_value;
        private double total_yield;
        private int canusecard;

        private double paynum;
        private double yield;
        private double day_yield;
        private double week_yield;
        private double month_yield;

        private int day_rank;//排名
        private int week_rank;
        private String month_rank;
        private int total_rank;
        private String holds;
        private String records;//获奖记录
        private String trusts;//委托
        private String focus;
        private String half;
        private String avg_week_yield;
        private String avg_month_yield;
        private int count_players;
        private double week_velocity;
        private String new_announcement;

        public String getNew_announcement() {
            return new_announcement;
        }

        public double getPaynum() {
            return paynum;
        }

        public void setPaynum(double paynum) {
            this.paynum = paynum;
        }

        public void setNew_announcement(String new_announcement) {
            this.new_announcement = new_announcement;
        }

        public double getWeek_velocity() {
            return week_velocity;
        }

        public void setWeek_velocity(double week_velocity) {
            this.week_velocity = week_velocity;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getTruename() {
            return truename;
        }

        public void setTruename(String truename) {
            this.truename = truename;
        }

        public String getMatch_username() {
            return match_username;
        }

        public void setMatch_username(String match_username) {
            this.match_username = match_username;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public String getInit() {
            return init;
        }

        public void setInit(String init) {
            this.init = init;
        }

        public double getPosition() {
            return position;
        }

        public void setPosition(double position) {
            this.position = position;
        }

        public double getMoneyunfrozen() {
            return moneyunfrozen;
        }

        public void setMoneyunfrozen(double moneyunfrozen) {
            this.moneyunfrozen = moneyunfrozen;
        }

        public double getFrozen() {
            return frozen;
        }

        public void setFrozen(double frozen) {
            this.frozen = frozen;
        }

        public String getDeal() {
            return deal;
        }

        public void setDeal(String deal) {
            this.deal = deal;
        }

        public double getStocks_value() {
            return stocks_value;
        }

        public void setStocks_value(double stocks_value) {
            this.stocks_value = stocks_value;
        }

        public double getYield() {
            return yield;
        }

        public void setYield(double yield) {
            this.yield = yield;
        }

        public double getDay_yield() {
            return day_yield;
        }

        public void setDay_yield(double day_yield) {
            this.day_yield = day_yield;
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

        public String getMonth_rank() {
            return month_rank;
        }

        public void setMonth_rank(String month_rank) {
            this.month_rank = month_rank;
        }

        public int getTotal_rank() {
            return total_rank;
        }

        public void setTotal_rank(int total_rank) {
            this.total_rank = total_rank;
        }

        public String getHolds() {
            return holds;
        }

        public void setHolds(String holds) {
            this.holds = holds;
        }

        public String getRecords() {
            return records;
        }

        public void setRecords(String records) {
            this.records = records;
        }

        public String getTrusts() {
            return trusts;
        }

        public void setTrusts(String trusts) {
            this.trusts = trusts;
        }

        public String getFocus() {
            return focus;
        }

        public void setFocus(String focus) {
            this.focus = focus;
        }

        public String getHalf() {
            return half;
        }

        public void setHalf(String half) {
            this.half = half;
        }

        public String getAvg_week_yield() {
            return avg_week_yield;
        }

        public void setAvg_week_yield(String avg_week_yield) {
            this.avg_week_yield = avg_week_yield;
        }

        public String getAvg_month_yield() {
            return avg_month_yield;
        }

        public void setAvg_month_yield(String avg_month_yield) {
            this.avg_month_yield = avg_month_yield;
        }

        public int getCount_players() {
            return count_players;
        }

        public void setCount_players(int count_players) {
            this.count_players = count_players;
        }

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

        public double getUnfrozen() {
            return unfrozen;
        }

        public void setUnfrozen(double unfrozen) {
            this.unfrozen = unfrozen;
        }

        public int getPlayers() {
            return players;
        }

        public void setPlayers(int players) {
            this.players = players;
        }

        public double getTotal_yield() {
            return total_yield;
        }

        public void setTotal_yield(double total_yield) {
            this.total_yield = total_yield;
        }

        public int getCanusecard() {
            return canusecard;
        }

        public void setCanusecard(int canusecard) {
            this.canusecard = canusecard;
        }
    }

}
