package com.zbmf.StocksMatch.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xuhao
 * on 2017/12/11.
 */

public class StockHoldList extends BaseListBean implements Serializable{
    private List<StockholdsBean>stocks;

    public List<StockholdsBean> getStocks() {
        return stocks;
    }

    public void setStocks(List<StockholdsBean> stocks) {
        this.stocks = stocks;
    }

    @Override
    public String toString() {
        return "StockHoldList{" +
                "stocks=" + stocks +
                '}';
    }
}
