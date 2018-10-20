package com.zbmf.StocksMatch.bean;

import java.io.Serializable;

public class StockholdsBean extends BaseBean implements Serializable {
    private String id;
    private String symbol;
    private String stock_name;
    private String name;
    private double current;
    private double close;
    private double price_buy;
    private double price2;
    private double price_float;
    private double price_sell;
    private double profit;
    private double yield_float;
    private String volumn_total;
    private String volumn_unfrozen;
    private String created_at;
    private String posted_at;
    private String frozen;
    private String is_show;//1 可看
    private String is_buy;
    private boolean is_vip;
    private int comment_count;
    private String type;
    private String volumn;

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public boolean is_vip() {
        return is_vip;
    }

    public void setIs_vip(boolean is_vip) {
        this.is_vip = is_vip;
    }

    public String getIs_show() {
        return is_show;
    }

    public void setIs_show(String is_show) {
        this.is_show = is_show;
    }

    public String getIs_buy() {
        return is_buy;
    }

    public void setIs_buy(String is_buy) {
        this.is_buy = is_buy;
    }

    public String getFrozen() {
        return frozen;
    }

    public void setFrozen(String frozen) {
        this.frozen = frozen;
    }

    public String getPosted_at() {
        return posted_at;
    }

    public void setPosted_at(String posted_at) {
        this.posted_at = posted_at;
    }


    public String getVolumn() {
        return volumn;
    }

    public void setVolumn(String volumn) {
        this.volumn = volumn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double colose) {
        this.close = colose;
    }

    public double getPrice_buy() {
        return price_buy;
    }

    public void setPrice_buy(double price_buy) {
        this.price_buy = price_buy;
    }

    public double getPrice2() {
        return price2;
    }

    public void setPrice2(double price2) {
        this.price2 = price2;
    }

    public double getPrice_float() {
        return price_float;
    }

    public void setPrice_float(double price_float) {
        this.price_float = price_float;
    }

    public double getPrice_sell() {
        return price_sell;
    }

    public void setPrice_sell(double price_sell) {
        this.price_sell = price_sell;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public double getYield_float() {
        return yield_float;
    }

    public void setYield_float(double yield_float) {
        this.yield_float = yield_float;
    }

    public String getVolumn_total() {
        if (volumn_total.contains(".")){
            return volumn_total.substring(0, volumn_total.indexOf("."));
        }else {
            return volumn_total;
        }
    }

    public void setVolumn_total(String volumn_total) {
        this.volumn_total = volumn_total;
    }

    public String getVolumn_unfrozen() {
//        Log.i("--TAG","---- volumn_unfrozen"+volumn_unfrozen);
        if (volumn_unfrozen.contains(".")&&Float.parseFloat(volumn_unfrozen)>=0){
//            String substring = volumn_unfrozen.substring(0, volumn_total.indexOf("."));
            int i = volumn_unfrozen.indexOf(".");
            return volumn_unfrozen.substring(0, i);
        }else {
            return volumn_unfrozen;
        }
    }

    public void setVolumn_unfrozen(String volumn_unfrozen) {
        this.volumn_unfrozen = volumn_unfrozen;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public boolean isIs_vip() {
        return is_vip;
    }

    @Override
    public String toString() {
        return "StockholdsBean{" +
                "id='" + id + '\'' +
                ", symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", current=" + current +
                ", close=" + close +
                ", price_buy=" + price_buy +
                ", price2=" + price2 +
                ", price_float=" + price_float +
                ", price_sell=" + price_sell +
                ", profit=" + profit +
                ", yield_float=" + yield_float +
                ", volumn_total='" + volumn_total + '\'' +
                ", volumn_unfrozen='" + volumn_unfrozen + '\'' +
                ", created_at='" + created_at + '\'' +
                ", posted_at='" + posted_at + '\'' +
                ", frozen='" + frozen + '\'' +
                ", is_show='" + is_show + '\'' +
                ", is_buy='" + is_buy + '\'' +
                ", is_vip=" + is_vip +
                ", comment_count=" + comment_count +
                ", type='" + type + '\'' +
                ", volumn='" + volumn + '\'' +
                '}';
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }
}
