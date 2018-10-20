package com.zbmf.StocksMatch.listener;

import com.zbmf.StocksMatch.bean.StockInfo;
import com.zbmf.worklibrary.baseview.BaseView;

/**
 * Created by pq
 * on 2018/3/27.
 */

public interface RefreshStockRealInfo extends BaseView{
    void refreshStockInfo(StockInfo.Result stock);
    void refreshStockInfoErr(String msg);
    void entrustStock(String msg);//委托股票
    void sellStockStatus(String msg);
}
