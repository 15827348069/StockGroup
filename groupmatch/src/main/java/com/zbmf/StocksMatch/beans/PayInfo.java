package com.zbmf.StocksMatch.beans;

/**
 * Created by Administrator on 2016/1/18.
 */
public class PayInfo extends General{
    private String mpay;
    private double discount;

    public String getMpay() {
        return mpay;
    }

    public void setMpay(String mpay) {
        this.mpay = mpay;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
