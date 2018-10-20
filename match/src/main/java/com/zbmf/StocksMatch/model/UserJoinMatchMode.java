package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.MatchNewAllBean;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.GsonUtil;

/**
 * Created by pq
 * on 2018/4/28.
 */

public class UserJoinMatchMode extends BaseStockMode {
    public void getUserMatch(String page,String userId,final CallBack callBack) {
        postSubscrube(Method.USER_MATCH, SendParam.getUserMatch(page,userId), new CallBack() {
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
