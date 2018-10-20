package com.zbmf.StocksMatch.model.imode;

import com.zbmf.worklibrary.model.CallBack;

/**
 * Created by xuhao
 * on 2017/12/18.
 */

public interface IMatchHoldMode {
    /*void getHoldList(String matchId,String userId , CallBack callBack);
    void getTraderDealRecord(int user_id,int page,int perPage,CallBack callBack);*/
    void getTraderHolderRecord(String user_id,CallBack callBack);
}
