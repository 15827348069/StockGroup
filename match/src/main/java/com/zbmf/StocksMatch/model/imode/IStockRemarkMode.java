package com.zbmf.StocksMatch.model.imode;

import com.zbmf.worklibrary.model.CallBack;

/**
 * Created by pq
 * on 2018/3/30.
 */

public interface IStockRemarkMode {
    void stockRemarkList(String symbol, String page, CallBack callBack);
    void addStockRemark(String symbol,String remark,CallBack callBack);
    void deleteStockRemark(String symbol,String remarkID,CallBack callBack);
}
