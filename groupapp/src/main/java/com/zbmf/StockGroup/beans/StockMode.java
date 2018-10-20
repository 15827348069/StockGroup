package com.zbmf.StockGroup.beans;

import java.io.Serializable;

/**
 * Created by xuhao on 2017/12/4.
 */

public class StockMode implements Serializable{
    private String stockNmae;
    private String symbol;
    private double price;
    private double yield;
    private double allYield;
    private double yellow;
    private double before_yesterday_price;
    private double yesterday_price;
    private double today_price;
    private double before_yesterday_yield;
    private double yesterday_yield;
    private double today_yield;
    private int repeat;
    public StockMode(String stockNmae, String symbol) {
        this.stockNmae = stockNmae;
        this.symbol = symbol;
    }

    public StockMode(String stockNmae, String symbol, double price, double yield, double allYield, double yellow,int repeat) {
        this.stockNmae = stockNmae;
        this.symbol = symbol;
        this.price = price;
        this.yield = yield;
        this.allYield = allYield;
        this.yellow = yellow;
        this.repeat=repeat;
    }

    public StockMode(String stockNmae, String symbol, double price, double yield, double allYield, double yellow, double before_yesterday_price, double yesterday_price, double today_price, double before_yesterday_yield, double yesterday_yield, double today_yield) {
        this.stockNmae = stockNmae;
        this.symbol = symbol;
        this.price = price;
        this.yield = yield;
        this.allYield = allYield;
        this.yellow = yellow;
        this.before_yesterday_price = before_yesterday_price;
        this.yesterday_price = yesterday_price;
        this.today_price = today_price;
        this.before_yesterday_yield = before_yesterday_yield;
        this.yesterday_yield = yesterday_yield;
        this.today_yield = today_yield;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getStockNmae() {
        return stockNmae;
    }

    public void setStockNmae(String stockNmae) {
        this.stockNmae = stockNmae;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getYield() {
        return yield;
    }

    public void setYield(double yield) {
        this.yield = yield;
    }

    public double getAllYield() {
        return allYield;
    }

    public void setAllYield(double allYield) {
        this.allYield = allYield;
    }

    public double getYellow() {
        return yellow;
    }

    public void setYellow(double yellow) {
        this.yellow = yellow;
    }

    public double getBefore_yesterday_price() {
        return before_yesterday_price;
    }

    public void setBefore_yesterday_price(double before_yesterday_price) {
        this.before_yesterday_price = before_yesterday_price;
    }

    public double getYesterday_price() {
        return yesterday_price;
    }

    public void setYesterday_price(double yesterday_price) {
        this.yesterday_price = yesterday_price;
    }

    public double getToday_price() {
        return today_price;
    }

    public void setToday_price(double today_price) {
        this.today_price = today_price;
    }

    public double getBefore_yesterday_yield() {
        return before_yesterday_yield;
    }

    public void setBefore_yesterday_yield(double before_yesterday_yield) {
        this.before_yesterday_yield = before_yesterday_yield;
    }

    public double getYesterday_yield() {
        return yesterday_yield;
    }

    public void setYesterday_yield(double yesterday_yield) {
        this.yesterday_yield = yesterday_yield;
    }

    public double getToday_yield() {
        return today_yield;
    }

    public void setToday_yield(double today_yield) {
        this.today_yield = today_yield;
    }
}
