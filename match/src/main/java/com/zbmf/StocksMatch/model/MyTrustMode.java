package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.BaseBean;
import com.zbmf.StocksMatch.bean.OrderList;
import com.zbmf.StocksMatch.bean.SellBean;
import com.zbmf.StocksMatch.model.imode.IMyTrustMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.GsonUtil;

/**
 * Created by pq
 * on 2018/4/4.
 */

public class MyTrustMode extends BaseStockMode implements IMyTrustMode {
    @Override
    public void getTrustList(String match_id, String page, final CallBack callBack) {
        postSubscrube(Method.MY_ORDER_LIST, SendParam.getOrderList(match_id, page), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                OrderList orderList = GsonUtil.parseData(o, OrderList.class);
                if (orderList.getStatus()) {
                    callBack.onSuccess(orderList.getResult());
                } else {
                    callBack.onFail(orderList.getErr().getMsg());
                }

            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    @Override
    public void revoke(String id, String matchId, final CallBack callBack) {
        postSubscrube(Method.REVOKE, SendParam.revoke(id, matchId), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                BaseBean baseBean = GsonUtil.parseData(o, BaseBean.class);
                if (baseBean.getStatus()) {
                    callBack.onSuccess("撤单成功!");
                } else {
                    callBack.onFail(baseBean.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }
    //提交委托
    @Override
    public void buyMatchStock(final String symbol, final String price, final String volumn,String matchId, final CallBack callBack) {
        postSubscrube(Method.NEW_BUY_STOCK, SendParam.buyStock(symbol, price, volumn,matchId), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                BaseBean baseBean = GsonUtil.parseData(o, BaseBean.class);
                assert baseBean != null;
                if (baseBean.getStatus()) {
                    callBack.onSuccess("委托成功");
                } else {
                    callBack.onFail(baseBean.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    public void sellMatchStock(final String symbol, String volumn, String price,String matchID, final CallBack callBack){
        postSubscrube(Method.NEW_SELL_STOCK, SendParam.sellStock(symbol, price, volumn,matchID), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                SellBean sellBean = GsonUtil.parseData(o, SellBean.class);
                if (sellBean.getStatus()){
                    callBack.onSuccess("委托成功");
                }else {
                    callBack.onFail(sellBean.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }
}
