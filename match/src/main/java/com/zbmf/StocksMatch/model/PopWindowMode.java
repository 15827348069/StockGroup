package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.MatchBottomAdsBean;
import com.zbmf.StocksMatch.bean.PopWindowBean;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.GsonUtil;

/**
 * Created by pq
 * on 2018/5/15.
 */

public class PopWindowMode extends BasePopWindow {

    public void getPopWindow(String matchID, final CallBack callBack) {
        postSubscrube(Method.POP_WINDOW, SendParam.getPopWindow(matchID), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                PopWindowBean popWindowBean = GsonUtil.parseData(o, PopWindowBean.class);
                if (popWindowBean.getStatus()) {
                    callBack.onSuccess(popWindowBean);
                } else {
                    callBack.onFail(popWindowBean.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    public void getMatchBottomAds(final String matchId, final CallBack callBack) {
        postSubscrube(Method.MATCH_BOTTOM_ADS, SendParam.getMatchBottomAds(matchId), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                MatchBottomAdsBean matchBottomAdsBean = GsonUtil.parseData(o, MatchBottomAdsBean.class);
                if (matchBottomAdsBean.getStatus()) {
                    callBack.onSuccess(matchBottomAdsBean);
                } else {
                    callBack.onFail(matchBottomAdsBean.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }
}
