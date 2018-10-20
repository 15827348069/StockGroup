package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.MatchDescBean;
import com.zbmf.StocksMatch.model.imode.IMatchDescMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.GsonUtil;

/**
 * Created by pq
 * on 2018/3/19.
 */

public class MatchDescMode extends BaseStockMode implements IMatchDescMode {

    @Override
    public void matchDesc(final int match_id, final CallBack callBack) {
        postSubscrube(Method.MATCH_DESC, SendParam.getMatchDesc(match_id), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                MatchDescBean matchDescBean = GsonUtil.parseData(o, MatchDescBean.class);
                assert matchDescBean != null;
                if (matchDescBean.getStatus()) {
                    callBack.onSuccess(matchDescBean.getResult());
                } else {
                    callBack.onFail(matchDescBean.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }
}
