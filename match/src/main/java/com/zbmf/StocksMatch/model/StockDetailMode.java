package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.StockAskBean;
import com.zbmf.StocksMatch.model.imode.IStockDetailMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.GsonUtil;

/**
 * Created by pq
 * on 2018/3/31.
 */

public class StockDetailMode extends BaseStockDetailMode implements IStockDetailMode {
    //获取互动列表
    @Override
    public void getAskList(String symbol, String page, final CallBack callBack) {
        postSubscrube(Method.STOCK_ASK_LIST, SendParam.getAskStockList(symbol, page), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                StockAskBean stockAskBean = GsonUtil.parseData(o, StockAskBean.class);
                if (stockAskBean.getStatus()) {
                    if (stockAskBean.getResult() != null) {
                        callBack.onSuccess(stockAskBean.getResult());
                    } else {
                        callBack.onFail("暂无数据");
                    }
                } else {
                    callBack.onFail(stockAskBean.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }
}
