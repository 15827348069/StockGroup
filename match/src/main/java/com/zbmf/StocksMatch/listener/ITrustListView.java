package com.zbmf.StocksMatch.listener;

import com.zbmf.StocksMatch.bean.OrderList;
import com.zbmf.worklibrary.baseview.BaseView;

/**
 * Created by pq
 * on 2018/4/4.
 */

public interface ITrustListView extends BaseView {
    void getTrustList(OrderList.Result result);
    void err(String msg);
    void revokeResult(String msg);
}
