package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.model.imode.IMatchDetailMode;
import com.zbmf.worklibrary.model.CallBack;

/**
 * Created by xuhao
 * on 2017/11/29.
 */

public class MatchDetailMode extends BaseMatchMode implements IMatchDetailMode{
    //获取比赛信息
    @Override
    public void getMatchDetail(final String matchId,String userID, final CallBack callBack) {
//        postSubscrube(Method.MATCH_JOIN/*GETPLAYER*/, SendParam.getMatchDetail(matchId,userID), new CallBack() {
//            @Override
//            public void onSuccess(Object o) {
//                MatchInfo matchInfo= GsonUtil.parseData(o,MatchInfo.class);
//                assert matchInfo != null;
//                if(matchInfo.getStatus()){
//                    callBack.onSuccess(matchInfo);
//                }else{
//                    callBack.onFail(matchInfo.getErr().getMsg());
//                }
//            }
//
//            @Override
//            public void onFail(String msg) {
//                callBack.onFail(msg);
//            }
//        });
    }

    @Override
    public void getMatchHold(String matchId, String userID,final CallBack callBack) {
//        postSubscrube(Method.GET_HOLD_LIST, SendParam.getHoldList(matchId,userID), new CallBack() {
//            @Override
//            public void onSuccess(Object o) {
//                BaseBean baseBean=GsonUtil.parseData(o,BaseBean.class);
//                assert baseBean != null;
//                if(baseBean.getStatus()&&baseBean.getResult()!=null){
//                    StockHoldList stockHoldList=GsonUtil.parseData(baseBean.getResult(),StockHoldList.class);
//                    if (stockHoldList!=null){
//                        callBack.onSuccess(stockHoldList);
//                    }
//                }else{
//                    callBack.onFail(baseBean.getErr().getMsg());
//                }
//            }
//
//            @Override
//            public void onFail(String msg) {
//                callBack.onFail(msg);
//            }
//        });
    }
}
