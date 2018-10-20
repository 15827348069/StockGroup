package com.zbmf.StocksMatch.presenter;

import com.zbmf.StocksMatch.listener.AddStockView;
import com.zbmf.StocksMatch.model.AddStockMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;

/**
 * Created by pq
 * on 2018/3/29.
 */

public class AddStockPresenter extends BasePresenter<AddStockMode,AddStockView>{
    @Override
    public void getDatas() {

    }

    @Override
    public AddStockMode initMode() {
        return new AddStockMode();
    }

    public void addStock(String symbol, String remark){
        getMode().addStockMode(symbol, remark, new CallBack<String>() {
            @Override
            public void onSuccess(String o) {
                if (getView()!=null){getView().addResult(o);}
            }

            @Override
            public void onFail(String msg) {
                if (getView()!=null){
                    getView().addErro(msg);
                }
            }
        });
    }
}
