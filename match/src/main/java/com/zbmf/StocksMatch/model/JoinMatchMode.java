package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.HolderPositionBean;
import com.zbmf.StocksMatch.bean.JoinMatchBean;
import com.zbmf.StocksMatch.bean.MatchNewAllBean;
import com.zbmf.StocksMatch.model.imode.IJoinMatchMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.GsonUtil;

/**
 * Created by pq
 * on 2018/3/19.
 */

public class JoinMatchMode extends BaseStockMode implements IJoinMatchMode {
    //获取参加比赛页面的数据
    @Override
    public void joinMatch(int matchID, String userID, final CallBack callBack) {
        postSubscrube(Method.MATCH_JOIN, SendParam.getMatchJoin(matchID, userID), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                JoinMatchBean joinMatchBean = GsonUtil.parseData(o, JoinMatchBean.class);
                assert joinMatchBean != null;
                if (joinMatchBean.getStatus()) {
                    callBack.onSuccess(joinMatchBean.getResult());
                } else {
                    callBack.onFail(joinMatchBean.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    //获取持仓数据
    @Override
    public void holderPosition(int matchID, String page, String userId, final CallBack callBack) {
        postSubscrube(Method.HOLDER_POSITION, SendParam.getHolderPosition(page, matchID, userId), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                HolderPositionBean holderPositionBean = GsonUtil.parseData(o, HolderPositionBean.class);
                assert holderPositionBean != null;
                if (holderPositionBean.getStatus()) {
                    callBack.onSuccess(holderPositionBean.getResult());
                } else {
                    callBack.onFail(holderPositionBean.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    @Override
    public void getUserMatch(String page, String userId, final CallBack callBack) {
        postSubscrube(Method.USER_MATCH, SendParam.getUserMatch(page, userId), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                MatchNewAllBean newAllBean = GsonUtil.parseData(o, MatchNewAllBean.class);
                if (newAllBean.getStatus()) {
                    callBack.onSuccess(newAllBean.getResult());
                } else {
                    callBack.onFail(newAllBean.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }
}
