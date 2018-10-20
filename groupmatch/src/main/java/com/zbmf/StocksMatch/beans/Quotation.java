package com.zbmf.StocksMatch.beans;

import java.util.List;

/**
 * 行情bean
 * Created by kubo on 2016/1/4.
 */
public class Quotation extends General{
    private String name;
    private String symbol;
    private String current;
    private String close;
    public String id;
    private List<Quotation> list;
    private List<List<Quotation>> lists;
    public String getName() {
        return name;
    }

    private String buy;
    private String sell;
    private String midprice;

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public String getMidprice() {
        return midprice;
    }

    public void setMidprice(String midprice) {
        this.midprice = midprice;
    }

    public void setName(String name) {
        this.name = name;
    }

        public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Quotation> getList() {
        return list;
    }

    public void setList(List<Quotation> list) {
        this.list = list;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public List<List<Quotation>> getLists() {
        return lists;
    }

    public void setLists(List<List<Quotation>> lists) {
        this.lists = lists;
    }
}
