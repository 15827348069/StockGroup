package com.zbmf.StocksMatch.listener;

import com.zbmf.StocksMatch.bean.MatchInfo;
import com.zbmf.StocksMatch.bean.StockAskBean;
import com.zbmf.worklibrary.baseview.BaseView;

/**
 * Created by pq
 * on 2018/3/31.
 */

public interface IStockDetailView extends BaseView {
    void stockAskList(StockAskBean.Result o);
    void stockAskErr(String msg);
    void matchInfo(MatchInfo matchInfo);
    void matchInfoErr(String msg);
}
