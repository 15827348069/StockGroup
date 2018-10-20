package com.zbmf.StockGroup.beans;

/**
 * Created by xuhao on 2017/11/9.
 */

public class TraderYield {
    private int deal_days;
    private int deal_total;
    private double deal_success;
    private double total_yideld;
    private double win_index;
    private double index_yield;
    private double total_money;
    private  int hold_num;
    private double position;

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

    public double getDeal_success() {
        return deal_success;
    }

    public void setDeal_success(double deal_success) {
        this.deal_success = deal_success;
    }

    public double getTotal_yideld() {
        return total_yideld;
    }

    public void setTotal_yideld(double total_yideld) {
        this.total_yideld = total_yideld;
    }

    public double getWin_index() {
        return win_index;
    }

    public void setWin_index(double win_index) {
        this.win_index = win_index;
    }

    public double getTotal_money() {
        return total_money;
    }

    public void setTotal_money(double total_money) {
        this.total_money = total_money;
    }

    public int getHold_num() {
        return hold_num;
    }

    public void setHold_num(int hold_num) {
        this.hold_num = hold_num;
    }

    public double getPosition() {
        return position;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public double getIndex_yield() {
        return index_yield;
    }

    public void setIndex_yield(double index_yield) {
        this.index_yield = index_yield;
    }

    public TraderYield(int deal_days, int deal_total, double deal_success, double total_yideld, double win_index, double index_yield, double total_money, int hold_num, double position) {
        this.deal_days = deal_days;
        this.deal_total = deal_total;
        this.deal_success = deal_success;
        this.total_yideld = total_yideld;
        this.win_index = win_index;
        this.index_yield = index_yield;
        this.total_money = total_money;
        this.hold_num = hold_num;
        this.position = position;
    }
}
