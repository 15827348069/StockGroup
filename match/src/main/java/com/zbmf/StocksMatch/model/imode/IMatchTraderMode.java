package com.zbmf.StocksMatch.model.imode;

import com.zbmf.worklibrary.model.CallBack;

/**
 * Created by xuhao
 * on 2017/12/19.
 */

public interface IMatchTraderMode {
    void getMatchTraderList(String traderId, CallBack callBack);
    void getTraderDealRecord(String user_id,int page,int perPage,CallBack callBack);
    void getTraderHolderRecord(String user_id,CallBack callBack);
}
