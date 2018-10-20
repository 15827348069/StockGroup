package com.zbmf.StocksMatch.model.imode;

import com.zbmf.worklibrary.model.CallBack;

/**
 * Created by pq
 * on 2018/3/31.
 */

public interface IStockDetailMode {
    void getAskList(String symbol, String page, /*String perPage, */CallBack callBack);
}
