package com.zbmf.StocksMatch.model.imode;

import com.zbmf.worklibrary.model.CallBack;

/**
 * Created by pq
 * on 2018/3/29.
 */

public interface IGetStockMode {
    void getStockMode(String searchKey, CallBack callBack);
}
