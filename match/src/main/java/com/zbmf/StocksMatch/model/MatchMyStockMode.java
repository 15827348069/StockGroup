package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.BaseBean;
import com.zbmf.StocksMatch.bean.StockHoldList;
import com.zbmf.StocksMatch.bean.StockholdsBean;
import com.zbmf.StocksMatch.model.imode.IMatchMyStockMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.pullrefreshrecycle.RefreshStatus;
import com.zbmf.worklibrary.util.GsonUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xuhao
 * on 2017/12/19.
 */

public class MatchMyStockMode extends /*BaseStockMode*/BaseMatchMode implements IMatchMyStockMode {
    private List<StockholdsBean> stockholdsBeans;

    //获取自选股列表
    @Override
    public void getMyStock(int page, final RefreshStatus status, final CallBack callBack) {
        if (stockholdsBeans == null) {
            stockholdsBeans = new ArrayList<>();
        }
        postSubscrube(Method.FOCUS_STOCK_LIST/*GET_FOCUS_LIST*/, SendParam.getFocusList(page), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                BaseBean baseBean = GsonUtil.parseData(o, BaseBean.class);
                if (baseBean.getStatus()) {
                    StockHoldList stockHoldList = GsonUtil.parseData(baseBean.getResult(), StockHoldList.class);
                    switch (status) {
                        case LOAD_DEFAULT:
                            stockholdsBeans.clear();
                            stockholdsBeans.addAll(stockHoldList.getStocks());
                            break;
                        case LOAD_MORE:
                            stockholdsBeans.addAll(stockHoldList.getStocks());
                            break;
                    }
                    stockHoldList.setStocks(stockholdsBeans);
                    callBack.onSuccess(stockHoldList);
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
