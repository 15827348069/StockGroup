package com.zbmf.StocksMatch.model.imode;

import com.zbmf.worklibrary.model.CallBack;

/**
 * Created by pq
 * on 2018/3/26.
 */

public interface IDealMode {
    void getTraderDealRecord(String user_id, int page, int perPage, final CallBack callBack);
}
