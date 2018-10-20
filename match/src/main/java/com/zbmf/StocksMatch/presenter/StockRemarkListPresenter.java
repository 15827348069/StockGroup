package com.zbmf.StocksMatch.presenter;

import android.text.TextUtils;

import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.StockRemarkListBean;
import com.zbmf.StocksMatch.listener.IStockRemarkView;
import com.zbmf.StocksMatch.model.StockRemarkMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;

/**
 * Created by pq
 * on 2018/3/30.
 */

public class StockRemarkListPresenter extends BasePresenter<StockRemarkMode,IStockRemarkView>{
    private String symbol;
    private int page;
    public StockRemarkListPresenter(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public void getDatas() {
        if (isFirst()){
            setFirst(false);
            page= ParamsKey.D_PAGE;
        }
        if (!TextUtils.isEmpty(symbol)) {
            stockRemarkList(symbol,String.valueOf(page));
        }
    }

    @Override
    public StockRemarkMode initMode() {
        return new StockRemarkMode();
    }

    public void stockRemarkList(String symbol,String page){
        getMode().stockRemarkList(symbol, page, new CallBack<StockRemarkListBean.Result>() {
            @Override
            public void onSuccess(StockRemarkListBean.Result o) {
  if (getView()!=null){
      getView().refreshStockRemarkList(o);
  }
            }

            @Override
            public void onFail(String msg) {
if (getView()!=null){
    getView().refreshStockRemarkListStatus(msg);
}
            }
        });
    }

    public void addStockRemark(String symbol,String remark){
        getMode().addStockRemark(symbol, remark, new CallBack<String>() {
            @Override
            public void onSuccess(String o) {
                if (getView()!=null){
                    getView().addStockRemarkStatus(o);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView()!=null){
                    getView().addStockRemarkStatus(msg);
                }
            }
        });
    }

    public void delStockRemark(String symbol,String remarkID){
        getMode().deleteStockRemark(symbol, remarkID, new CallBack<String>() {
            @Override
            public void onSuccess(String o) {
                if (getView()!=null){
                    getView().deleteStockRemarkStatus(o);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView()!=null){
                    getView().deleteStockRemarkStatus(msg);
                }
            }
        });
    }
}
