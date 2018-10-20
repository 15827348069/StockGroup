package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.TraderHolderPosition;
import com.zbmf.StocksMatch.model.imode.IMatchHoldMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.GsonUtil;

/**
 * Created by xuhao
 * on 2017/12/18.
 * 获取持仓数据或交易数据
 */

public class MatchHoldMode extends BaseMatchMode implements IMatchHoldMode{

   /* @Override
    public void getHoldList(String matchId,String userId,final CallBack callBack) {
        postSubscrube(Method.GET_HOLD_LIST, SendParam.getHoldList(matchId,userId), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                BaseBean baseBean= GsonUtil.parseData(o,BaseBean.class);
                assert baseBean != null;
                if(baseBean.getStatus()&&baseBean.getResult()!=null){
                    StockHoldList stockHoldList=GsonUtil.parseData(baseBean.getResult(),StockHoldList.class);
                    callBack.onSuccess(stockHoldList.getStocks());
                }else{
                    callBack.onFail(baseBean.getErr().getMsg());
                }
            }
            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }*/

 /*   //获取操盘高手的交易记录
    @Override
    public void getTraderDealRecord(int user_id, int page, int perPage, final CallBack callBack) {
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
    }*/
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
}
