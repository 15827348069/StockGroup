package com.zbmf.StocksMatch.presenter;

import android.text.TextUtils;

import com.zbmf.StocksMatch.bean.StockInfo;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.listener.RefreshStockRealInfo;
import com.zbmf.StocksMatch.model.BuyStockMode;
import com.zbmf.StocksMatch.model.MyTrustMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;

/**
 * Created by pq
 * on 2018/3/27.
 */

public class BuyStockPresenter extends BasePresenter<BuyStockMode, RefreshStockRealInfo> {
    private String mSymbol;
    private int flag;

    public BuyStockPresenter(int flag) {
        this.flag = flag;
    }

    public void setSymbol(String symbol) {
        mSymbol = symbol;
    }

    @Override
    public void getDatas() {
        if (isFirst()) {
            setFirst(false);
        }
        if (!TextUtils.isEmpty(mSymbol) && flag == Constans.BUY_FLAG) {
            getStockRealInfo(mSymbol);
        } else if (!TextUtils.isEmpty(mSymbol) && flag == Constans.SELL_FLAG) {
            getStockInfo(mSymbol);
        }
    }

    @Override
    public BuyStockMode initMode() {
        return new BuyStockMode();
    }

    public void getStockRealInfo(String symbol) {
        getMode().getStockRealInfo(symbol, new CallBack<StockInfo.Result>() {
            @Override
            public void onSuccess(StockInfo.Result o) {
                if (getView() != null) {
                    getView().refreshStockInfo(o);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().refreshStockInfoErr(msg);
                }
            }
        });
    }

    public void getStockInfo(String symbol) {
        getMode().getStockInfo(symbol, new CallBack<StockInfo.Result>() {
            @Override
            public void onSuccess(StockInfo.Result o) {
                if (getView() != null) {
                    getView().refreshStockInfo(o);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().refreshStockInfoErr(msg);
                }
            }
        });
    }

    public void buyMatchStock(String symbol, String price, String volumn,String matchID) {
        new MyTrustMode().buyMatchStock(symbol, price, volumn, matchID,new CallBack<String>() {
            @Override
            public void onSuccess(String o) {
                if (getView() != null && !TextUtils.isEmpty(o)) {
                    getView().entrustStock(o);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null && !TextUtils.isEmpty(msg)) {
                    getView().entrustStock(msg);
                }
            }
        });
    }

    public void sellMatchStock(String symbol, String volumn, String price,String matchID) {
        new MyTrustMode().sellMatchStock(symbol, volumn, price,matchID, new CallBack<String>() {
            @Override
            public void onSuccess(String o) {
                if (getView() != null) {
                    getView().sellStockStatus(o);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().sellStockStatus(msg);
                }
            }
        });
    }
}
