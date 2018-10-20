package com.zbmf.StockGroup.beans;

/**
 *  股票bean：
 * Created by lulu on 2016/1/6.
 */
public class Stock1 extends General{
    private String type;
    private String price;
    private String number;
    private boolean isup = false;

    public boolean isup() {
        return isup;
    }

    public void setIsup(boolean isup) {
        this.isup = isup;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
