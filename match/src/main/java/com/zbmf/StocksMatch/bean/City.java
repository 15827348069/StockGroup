package com.zbmf.StocksMatch.bean;

/**
 * Created by xuhao
 * on 2017/11/27.
 */

public class City extends BaseBean {
    private String cityName;
    private int rank;
    private double yield;
    private String one_avatar;
    private String two_avatar;
    private String three_avatar;
    private String matchId;

    public City(String cityName,String matchId) {
        this.cityName = cityName;
        this.matchId=matchId;
    }

    public City(String cityName, int rank, double yield, String one_avatar, String two_avatar, String three_avatar) {
        this.cityName = cityName;
        this.rank = rank;
        this.yield = yield;
        this.one_avatar = one_avatar;
        this.two_avatar = two_avatar;
        this.three_avatar = three_avatar;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public double getYield() {
        return yield;
    }

    public void setYield(double yield) {
        this.yield = yield;
    }

    public String getOne_avatar() {
        return one_avatar;
    }

    public void setOne_avatar(String one_avatar) {
        this.one_avatar = one_avatar;
    }

    public String getTwo_avatar() {
        return two_avatar;
    }

    public void setTwo_avatar(String two_avatar) {
        this.two_avatar = two_avatar;
    }

    public String getThree_avatar() {
        return three_avatar;
    }

    public void setThree_avatar(String three_avatar) {
        this.three_avatar = three_avatar;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }
}
