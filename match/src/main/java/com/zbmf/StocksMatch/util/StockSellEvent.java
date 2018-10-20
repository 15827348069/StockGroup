package com.zbmf.StocksMatch.util;

/**
 * Created by pq
 * on 2018/4/27.
 */

public class StockSellEvent {
    private String stockSum;//持有的剩余的可用股数量
    public StockSellEvent(String message) {
        this.stockSum = message;
    }

    public String getMessage() {
        return stockSum;
    }

    public void setMessage(String message) {
        this.stockSum = message;
    }
}
