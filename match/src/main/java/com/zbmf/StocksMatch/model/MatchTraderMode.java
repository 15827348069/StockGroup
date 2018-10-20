package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.BaseBean;
import com.zbmf.StocksMatch.bean.TraderDeals;
import com.zbmf.StocksMatch.bean.TraderHolderPosition;
import com.zbmf.StocksMatch.bean.TraderInfo;
import com.zbmf.StocksMatch.model.imode.IMatchTraderMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.GsonUtil;

/**
 * Created by xuhao
 * on 2017/12/19.
 */

public class MatchTraderMode extends BaseMatchMode implements IMatchTraderMode {
    //获取操盘高手的信息
    @Override
    public void getMatchTraderList(String traderId, final CallBack callBack) {
        postSubscrube(Method.GET_TRADER_INFO, SendParam.getTraderInfo(traderId), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                TraderInfo traderInfo= GsonUtil.parseData(o,TraderInfo.class);
                assert traderInfo != null;
                if(traderInfo.getStatus()){
                    callBack.onSuccess(traderInfo);
                }else{
                    callBack.onFail(traderInfo.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }
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
    //获取操盘高手的持仓列表
    @Override
    public void getTraderHolderRecord(String user_id, final CallBack callBack) {
        postSubscrube(Method.TRADER_HOLDER_POSITION_RECORD,SendParam.getTraderHolderPotionRecord(user_id), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                TraderHolderPosition traderHolderPosition = GsonUtil.parseData(o, TraderHolderPosition.class);
                assert traderHolderPosition != null;
                if (traderHolderPosition.getStatus()){
                    if (traderHolderPosition.getHolds()!=null){
                        callBack.onSuccess(traderHolderPosition.getHolds());
                    }
                }else {
                    callBack.onFail(traderHolderPosition.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }
    public void traderBuy(String user_id, final CallBack callBack){
        postSubscrube(Method.TRADER_BUY, SendParam.traderBuy(user_id), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                BaseBean baseBean = GsonUtil.parseData(o, BaseBean.class);
                if (baseBean.getStatus()){
                    callBack.onSuccess("续订成功");
                }else {
                    callBack.onFail("续订失败");
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }
}
