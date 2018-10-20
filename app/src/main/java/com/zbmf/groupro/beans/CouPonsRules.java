package com.zbmf.groupro.beans;

import java.io.Serializable;

/**
 * Created by xuhao on 2017/1/14.
 */

public class CouPonsRules implements Serializable {
    private double maximum;//最大优惠  0为无限制

    private int is_ratio;//是否是百分比
    private double cateogry;//优惠条件
    private double amount;//优惠数量
    private int is_incr;//0：满减  1：满加

    public double getMaximum() {
        return maximum;
    }

    public void setMaximum(double maximum) {
        this.maximum = maximum;
    }

    public int getIs_ratio() {
        return is_ratio;
    }

    public void setIs_ratio(int is_ratio) {
        this.is_ratio = is_ratio;
    }

    public double getCateogry() {
        return cateogry;
    }

    public void setCateogry(double cateogry) {
        this.cateogry = cateogry;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getIs_incr() {
        return is_incr;
    }

    public void setIs_incr(int is_incr) {
        this.is_incr = is_incr;
    }
}
