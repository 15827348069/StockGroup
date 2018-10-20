package com.zbmf.StocksMatch.listener;

import com.zbmf.StocksMatch.bean.StockCommentsBean;
import com.zbmf.worklibrary.baseview.BaseView;

/**
 * Created by pq
 * on 2018/3/31.
 */

public interface IStockCommentView extends BaseView{
    void getStockCommentList(StockCommentsBean.Result o);
    void getStockCommentErr(String msg);
    void addStockCommentStatus(String msg);
}
