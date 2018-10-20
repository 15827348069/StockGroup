package com.zbmf.StocksMatch.bean;

/**
 * Created by pq
 * on 2018/3/15.
 */

public class StockIndexBean/* extends BaseBean*/ {
    private String close;
    private String current;

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }
}
