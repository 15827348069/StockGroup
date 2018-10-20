package com.zbmf.StockGroup.beans;

import java.io.Serializable;

/**
 * Created by pq
 * on 2018/5/30.
 */

public class DealsRecord implements Serializable {
    private String id;
    private String symbol;
    private String name;
    private String volumn;
    private String price;
    private String price_buy;
    private String price_sell;
    private int contract_id;
    private int type;
    private double profit;
    private String posted_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVolumn() {
        return volumn;
    }

    public void setVolumn(String volumn) {
        this.volumn = volumn;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice_buy() {
        return price_buy;
    }

    public void setPrice_buy(String price_buy) {
        this.price_buy = price_buy;
    }

    public String getPrice_sell() {
        return price_sell;
    }

    public void setPrice_sell(String price_sell) {
        this.price_sell = price_sell;
    }

    public int getContract_id() {
        return contract_id;
    }

    public void setContract_id(int contract_id) {
        this.contract_id = contract_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public String getPosted_at() {
        return posted_at;
    }

    public void setPosted_at(String posted_at) {
        this.posted_at = posted_at;
    }
}
