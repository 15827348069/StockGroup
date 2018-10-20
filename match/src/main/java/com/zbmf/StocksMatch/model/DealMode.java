package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.DealsRecordList;
import com.zbmf.StocksMatch.bean.TraderDeals;
import com.zbmf.StocksMatch.model.imode.IDealMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.GsonUtil;

/**
 * Created by pq
 * on 2018/3/26.
 */

public class DealMode extends BaseMatchMode implements IDealMode {
    //获取操盘高手的交易记录
    @Override
    public void getTraderDealRecord(String user_id, int page, int perPage, final CallBack callBack) {
        postSubscrube(Method.TRADER_DEAL_RECORD, SendParam.getTraderDealRecord(user_id,page,perPage), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                TraderDeals traderDeals = GsonUtil.parseData(o, TraderDeals.class);
                assert traderDeals != null;
                if (traderDeals.getStatus()){
                    if (traderDeals.getResult()!=null){
                        callBack.onSuccess(traderDeals.getResult());
                    }
                }else {
                    callBack.onFail(traderDeals.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }
    //普通用户的交易记录
    public void recordList(String matchID, String page, String id, final CallBack callBack) {
        postSubscrube(Method.RECORD_LIST,SendParam.getRecordList(matchID,page,id),new CallBack() {
            @Override
            public void onSuccess(Object o) {
                DealsRecordList dealsRecordList = GsonUtil.parseData(o, DealsRecordList.class);
                if (dealsRecordList.getStatus()){
                    callBack.onSuccess(dealsRecordList.getResult());
                }else {
                    callBack.onFail(dealsRecordList.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }
}
