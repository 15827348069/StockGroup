package com.zbmf.StocksMatch.model.imode;

import com.zbmf.worklibrary.model.CallBack;

/**
 * Created by pq
 * on 2018/3/27.
 */

public interface IBuyStockMode {
    void getStockRealInfo(String symbol, CallBack callBack);
//    void buyMatchStock(String symbol, String price, String volumn,CallBack callBack);
    void getStock(String values,CallBack callBack);
}
