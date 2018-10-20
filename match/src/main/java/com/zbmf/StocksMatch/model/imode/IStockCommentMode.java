package com.zbmf.StocksMatch.model.imode;

import com.zbmf.worklibrary.model.CallBack;

/**
 * Created by pq
 * on 2018/3/31.
 */

public interface IStockCommentMode {
    void getStockCommentList(String contract_id, String page, CallBack callBack);
    void addStockComment(String symbol,String desc,CallBack callBack);
}
