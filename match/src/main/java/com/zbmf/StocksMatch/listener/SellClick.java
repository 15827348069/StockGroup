package com.zbmf.StocksMatch.listener;

import com.zbmf.StocksMatch.bean.HolderPositionBean;

/**
 * Created by pq
 * on 2018/4/9.
 */

public interface SellClick {
    void sellClick(HolderPositionBean.Result.Stocks stocks);
}
