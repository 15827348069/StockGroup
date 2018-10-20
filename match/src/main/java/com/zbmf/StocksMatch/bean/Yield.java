package com.zbmf.StocksMatch.bean;

/**
 * Created by lulu on 2016/1/13.
 */
public class Yield extends BaseBean{
    private int page;
    private int perpage;
    private int pages;
    private int total;


    private String user;
    private String nickname;
    private String avatar;
    private String role;
    private Double yield;
    private Double total_yield;
    private Double day_yield;
    private Double week_yield;
    private Double month_yield;
    private String is_track;
    private int order;
    private DealSys deals;

    public DealSys getDealSys() {
        return deals;
    }

    public void setDealSys(DealSys dealSys) {
        this.deals = dealSys;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPerpage() {
        return perpage;
    }

    public void setPerpage(int perpage) {
        this.perpage = perpage;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public Double getYield() {
        return yield;
    }

    public void setYield(Double yield) {
        this.yield = yield;
    }

    public Double getTotal_yield() {
        return total_yield;
    }

    public void setTotal_yield(Double total_yield) {
        this.total_yield = total_yield;
    }

    public Double getDay_yield() {
        return day_yield;
    }

    public void setDay_yield(Double day_yield) {
        this.day_yield = day_yield;
    }

    public Double getWeek_yield() {
        return week_yield;
    }

    public void setWeek_yield(Double week_yield) {
        this.week_yield = week_yield;
    }

    public Double getMonth_yield() {
        return month_yield;
    }

    public void setMonth_yield(Double month_yield) {
        this.month_yield = month_yield;
    }

    public String getIs_track() {
        return is_track;
    }

    public void setIs_track(String is_track) {
        this.is_track = is_track;
    }
}
