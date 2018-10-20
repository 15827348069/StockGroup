package com.zbmf.StocksMatch.bean;

/**
 * Created by xuhao\
 * on 2017/11/9.
 */

public class TraderYield {
    private int deal_days;
    private int deal_total;
    private String deal_success;
    private String total_yield;
    private String win_index;
    private String index_yield;
    private String total_money;
    private  int hold_num;
    private String position;

    public int getDeal_days() {
        return deal_days;
    }

    public void setDeal_days(int deal_days) {
        this.deal_days = deal_days;
    }

    public int getDeal_total() {
        return deal_total;
    }

    public void setDeal_total(int deal_total) {
        this.deal_total = deal_total;
    }

    public String getDeal_success() {
        return deal_success;
    }

    public void setDeal_success(String deal_success) {
        this.deal_success = deal_success;
    }

    public String getTotal_yield() {
        return total_yield;
    }

    public void setTotal_yield(String total_yield) {
        this.total_yield = total_yield;
    }

    public String getWin_index() {
        return win_index;
    }

    public void setWin_index(String win_index) {
        this.win_index = win_index;
    }

    public String getTotal_money() {
        return total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money;
    }

    public int getHold_num() {
        return hold_num;
    }

    public void setHold_num(int hold_num) {
        this.hold_num = hold_num;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getIndex_yield() {
        return index_yield;
    }

    public void setIndex_yield(String index_yield) {
        this.index_yield = index_yield;
    }

    public TraderYield(int deal_days, int deal_total, String deal_success, String total_yield,
                       String win_index, String index_yield, String total_money, int hold_num, String position) {
        this.deal_days = deal_days;
        this.deal_total = deal_total;
        this.deal_success = deal_success;
        this.total_yield = total_yield;
        this.win_index = win_index;
        this.index_yield = index_yield;
        this.total_money = total_money;
        this.hold_num = hold_num;
        this.position = position;
    }
}
