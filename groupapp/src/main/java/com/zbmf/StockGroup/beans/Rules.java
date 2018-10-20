package com.zbmf.StockGroup.beans;

import java.io.Serializable;

/**
 * Created by pq
 * on 2018/6/7.
 */

public class Rules implements Serializable {
    private int is_incr;
    private int is_ratio;
    private int category;
    private int maximum;
    private double amount;

    public Rules(int category, int is_incr, int is_ratio, double amount, int maximum) {
        this.is_incr = is_incr;
        this.is_ratio = is_ratio;
        this.category = category;
        this.maximum = maximum;
        this.amount = amount;
    }
    public Rules(int is_ratio, int category, int maximum, double amount) {
        this.is_ratio = is_ratio;
        this.category = category;
        this.maximum = maximum;
        this.amount = amount;
    }
    public int getIs_incr() {
        return is_incr;
    }

    public void setIs_incr(int is_incr) {
        this.is_incr = is_incr;
    }

    public int getIs_ratio() {
        return is_ratio;
    }

    public void setIs_ratio(int is_ratio) {
        this.is_ratio = is_ratio;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getMaximum() {
        return maximum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
