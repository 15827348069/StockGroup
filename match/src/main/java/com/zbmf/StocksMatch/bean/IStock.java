package com.zbmf.StocksMatch.bean;

import java.io.Serializable;

/**
 * Created by pq
 * on 2018/4/2.
 */

public class IStock implements Serializable {
    private String dt;
    private int market;
    private int symbol;
    private String name;
    private double close;
    private double open;
    private double high;
    private double low;
    private double current;
    private double total_volumn;
    private double total_amount;
    private double buy1;
    private double buy2;
    private double buy3;
    private double buy4;
    private double buy5;
    private double sell1;
    private double sell2;
    private double sell3;
    private double sell4;
    private double sell5;
    private double volumn_buy1;
    private double volumn_buy2;
    private double volumn_buy3;
    private double volumn_buy4;
    private double volumn_buy5;
    private double volumn_sell1;
    private double volumn_sell2;
    private double volumn_sell3;
    private double volumn_sell4;
    private double volumn_sell5;
    private int midprice;
    private int amount;
    private int volumn;
    private int limit;
    private int valid;
    private boolean optional_stock_user;

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public int getMarket() {
        return market;
    }

    public void setMarket(int market) {
        this.market = market;
    }

    public int getSymbol() {
        return symbol;
    }

    public void setSymbol(int symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public double getTotal_volumn() {
        return total_volumn;
    }

    public void setTotal_volumn(double total_volumn) {
        this.total_volumn = total_volumn;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public double getBuy1() {
        return buy1;
    }

    public void setBuy1(double buy1) {
        this.buy1 = buy1;
    }

    public double getBuy2() {
        return buy2;
    }

    public void setBuy2(double buy2) {
        this.buy2 = buy2;
    }

    public double getBuy3() {
        return buy3;
    }

    public void setBuy3(double buy3) {
        this.buy3 = buy3;
    }

    public double getBuy4() {
        return buy4;
    }

    public void setBuy4(double buy4) {
        this.buy4 = buy4;
    }

    public double getBuy5() {
        return buy5;
    }

    public void setBuy5(double buy5) {
        this.buy5 = buy5;
    }

    public double getSell1() {
        return sell1;
    }

    public void setSell1(double sell1) {
        this.sell1 = sell1;
    }

    public double getSell2() {
        return sell2;
    }

    public void setSell2(double sell2) {
        this.sell2 = sell2;
    }

    public double getSell3() {
        return sell3;
    }

    public void setSell3(double sell3) {
        this.sell3 = sell3;
    }

    public double getSell4() {
        return sell4;
    }

    public void setSell4(double sell4) {
        this.sell4 = sell4;
    }

    public double getSell5() {
        return sell5;
    }

    public void setSell5(double sell5) {
        this.sell5 = sell5;
    }

    public double getVolumn_buy1() {
        return volumn_buy1;
    }

    public void setVolumn_buy1(double volumn_buy1) {
        this.volumn_buy1 = volumn_buy1;
    }

    public double getVolumn_buy2() {
        return volumn_buy2;
    }

    public void setVolumn_buy2(double volumn_buy2) {
        this.volumn_buy2 = volumn_buy2;
    }

    public double getVolumn_buy3() {
        return volumn_buy3;
    }

    public void setVolumn_buy3(double volumn_buy3) {
        this.volumn_buy3 = volumn_buy3;
    }

    public double getVolumn_buy4() {
        return volumn_buy4;
    }

    public void setVolumn_buy4(double volumn_buy4) {
        this.volumn_buy4 = volumn_buy4;
    }

    public double getVolumn_buy5() {
        return volumn_buy5;
    }

    public void setVolumn_buy5(double volumn_buy5) {
        this.volumn_buy5 = volumn_buy5;
    }

    public double getVolumn_sell1() {
        return volumn_sell1;
    }

    public void setVolumn_sell1(double volumn_sell1) {
        this.volumn_sell1 = volumn_sell1;
    }

    public double getVolumn_sell2() {
        return volumn_sell2;
    }

    public void setVolumn_sell2(double volumn_sell2) {
        this.volumn_sell2 = volumn_sell2;
    }

    public double getVolumn_sell3() {
        return volumn_sell3;
    }

    public void setVolumn_sell3(double volumn_sell3) {
        this.volumn_sell3 = volumn_sell3;
    }

    public double getVolumn_sell4() {
        return volumn_sell4;
    }

    public void setVolumn_sell4(double volumn_sell4) {
        this.volumn_sell4 = volumn_sell4;
    }

    public double getVolumn_sell5() {
        return volumn_sell5;
    }

    public void setVolumn_sell5(double volumn_sell5) {
        this.volumn_sell5 = volumn_sell5;
    }

    public int getMidprice() {
        return midprice;
    }

    public void setMidprice(int midprice) {
        this.midprice = midprice;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getVolumn() {
        return volumn;
    }

    public void setVolumn(int volumn) {
        this.volumn = volumn;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getValid() {
        return valid;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }

    public boolean isOptional_stock_user() {
        return optional_stock_user;
    }

    public void setOptional_stock_user(boolean optional_stock_user) {
        this.optional_stock_user = optional_stock_user;
    }
}
