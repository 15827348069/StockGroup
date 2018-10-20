package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.BaseBean;
import com.zbmf.StocksMatch.bean.StockRemarkListBean;
import com.zbmf.StocksMatch.model.imode.IStockRemarkMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.GsonUtil;

/**
 * Created by pq
 * on 2018/3/30.
 */

public class StockRemarkMode extends BaseStockMode implements IStockRemarkMode {
    @Override
    public void stockRemarkList(String symbol, String page, final CallBack callBack) {
        postSubscrube(Method.STOCK_REMARK_LIST, SendParam.getStockRemarkList(symbol, page), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                StockRemarkListBean stockRemarkListBean = GsonUtil.parseData(o, StockRemarkListBean.class);
                if (stockRemarkListBean.getStatus()) {
                    if (stockRemarkListBean.getResult() != null) {
                        callBack.onSuccess(stockRemarkListBean.getResult());
                    } else {
                        callBack.onFail("没有数据");
                    }
                } else {
                    callBack.onFail(stockRemarkListBean.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    @Override
    public void addStockRemark(String symbol, String remark, final CallBack callBack) {
        postSubscrube(Method.ADD_STOCK_REMARK, SendParam.addStockRemark(symbol, remark), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                BaseBean baseBean = GsonUtil.parseData(o, BaseBean.class);
                if (baseBean.getStatus()) {
                    callBack.onSuccess("新增成功");
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

    @Override
    public void deleteStockRemark(String symbol, String remarkID, final CallBack callBack) {
        postSubscrube(Method.DEL_STOCK_REMARK, SendParam.delStockRemark(symbol, remarkID), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                BaseBean baseBean = GsonUtil.parseData(o, BaseBean.class);
                if (baseBean.getStatus()) {
                    callBack.onSuccess("ok");
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
}
