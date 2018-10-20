package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.BaseBean;
import com.zbmf.StocksMatch.bean.StockCommentsBean;
import com.zbmf.StocksMatch.model.imode.IStockCommentMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.GsonUtil;

/**
 * Created by pq
 * on 2018/3/31.
 */

public class StockCommentMode extends BaseMatchMode implements IStockCommentMode {
    @Override
    public void getStockCommentList(final String contract_id, final String page, final CallBack callBack) {
        postSubscrube(Method.COMMENTS, SendParam.getStockComments(contract_id, page), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                StockCommentsBean stockCommentsBean = GsonUtil.parseData(o, StockCommentsBean.class);
                if (stockCommentsBean.getStatus()){
                    StockCommentsBean.Result result = stockCommentsBean.getResult();
                    callBack.onSuccess(result);
                }else {
                    callBack.onFail(stockCommentsBean.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    @Override
    public void addStockComment(String symbol, String desc, final CallBack callBack) {
        postSubscrube(Method.ADD_COMMENTS, SendParam.addStockComments(symbol, desc), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                BaseBean baseBean = GsonUtil.parseData(o, BaseBean.class);
                if (baseBean.getStatus()) {
                    callBack.onSuccess("发表成功");
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
