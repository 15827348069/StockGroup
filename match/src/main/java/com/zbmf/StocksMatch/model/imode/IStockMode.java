package com.zbmf.StocksMatch.model.imode;

import com.zbmf.worklibrary.model.CallBack;

/**
 * Created by pq
 * on 2018/3/15.
 */

public interface IStockMode {
    void getStockIndex(CallBack callBack);
    void getSupremeMatch(CallBack callBack);
    void getImageList(String category,CallBack callBack);
    void getMatchSchool(CallBack callBack);
    void getTrader(CallBack callBack);
    void getCity(CallBack callBack);
    void getHotMatch(CallBack callBack);
    void deleteStockItem(String symbol,CallBack callback);
}
