package com.zbmf.StocksMatch.listener;

import com.zbmf.worklibrary.baseview.BaseView;

/**
 * Created by pq
 * on 2018/3/29.
 */

public interface AddStockView extends BaseView {
    void addResult(String msg);
    void addErro(String msg);
}
