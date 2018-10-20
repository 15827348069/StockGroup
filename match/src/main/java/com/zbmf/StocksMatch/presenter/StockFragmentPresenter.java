package com.zbmf.StocksMatch.presenter;

import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.StockHoldList;
import com.zbmf.StocksMatch.listener.IStockFragmentView;
import com.zbmf.StocksMatch.model.AddStockMode;
import com.zbmf.StocksMatch.model.MatchMyStockMode;
import com.zbmf.StocksMatch.model.SymbolStockMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;
import com.zbmf.worklibrary.pullrefreshrecycle.RefreshStatus;


/**
 * Created by xuhao
 * on 2017/12/19.
 */

public class StockFragmentPresenter extends BasePresenter<MatchMyStockMode, IStockFragmentView> {
    private int page;
    private int mTotal;

    @Override
    public void getDatas() {
        page = ParamsKey.D_PAGE;
        if (isFirst()) {
            setFirst(false);
        }
        getMyStock(page);
//        getMode().getMyStock(page, RefreshStatus.LOAD_DEFAULT,new CallBack<StockHoldList>() {
//
//            @Override
//            public void onSuccess(StockHoldList stockHoldList) {
//                page=stockHoldList.getPage();
//                mTotal = stockHoldList.getTotal();
//                if (getView()!=null){
//                    getView().onRushStockList(stockHoldList.getStocks(),mTotal);
//                }
//            }
//
//            @Override
//            public void onFail(String msg) {
//                if (getView()!=null){
//                    getView().onFail(msg);
//                }
//            }
//        });
    }

    public void loadMore() {
        page += 1;
        new AddStockMode().getMyStock(page, RefreshStatus.LOAD_MORE, new CallBack<StockHoldList>() {
            @Override
            public void onSuccess(StockHoldList stockHoldList) {
                page = stockHoldList.getPage();
                mTotal = stockHoldList.getTotal();
                if (getView() != null) {
                    getView().onRushStockList(stockHoldList.getStocks(), mTotal, page);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().onFail(msg);
                }
            }
        });

//        getMode().getMyStock(page, RefreshStatus.LOAD_MORE, new CallBack<StockHoldList>() {
//            @Override
//            public void onSuccess(StockHoldList stockHoldList) {
//                page = stockHoldList.getPage();
//                mTotal = stockHoldList.getTotal();
//                if (getView() != null) {
//                    getView().onRushStockList(stockHoldList.getStocks(), mTotal, page);
//                }
//            }
//
//            @Override
//            public void onFail(String msg) {
//                if (getView() != null) {
//                    getView().onFail(msg);
//                }
//            }
//        });
    }

    @Override
    public MatchMyStockMode initMode() {
        return new MatchMyStockMode();
    }

    public void getMyStock(int iPage) {
        new AddStockMode().getMyStock(iPage, RefreshStatus.LOAD_DEFAULT, new CallBack<StockHoldList>() {
            @Override
            public void onSuccess(StockHoldList stockHoldList) {
                page = stockHoldList.getPage();
                mTotal = stockHoldList.getTotal();
                if (getView() != null) {
                    getView().onRushStockList(stockHoldList.getStocks(), mTotal, page);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().onFail(msg);
                }
            }
        });

//        getMode().getMyStock(iPage, RefreshStatus.LOAD_DEFAULT, new CallBack<StockHoldList>() {
//
//            @Override
//            public void onSuccess(StockHoldList stockHoldList) {
//                page = stockHoldList.getPage();
//                mTotal = stockHoldList.getTotal();
//                if (getView() != null) {
//                    getView().onRushStockList(stockHoldList.getStocks(), mTotal, page);
//                }
//            }
//
//            @Override
//            public void onFail(String msg) {
//                if (getView() != null) {
//                    getView().onFail(msg);
//                }
//            }
//        });
    }

    public void deleteStockItem(String symbol) {
        new SymbolStockMode().deleteStockItem(symbol, new CallBack<String>() {
            @Override
            public void onSuccess(String o) {
                if (getView() != null) {
                    getView().deleteStockStatus(o);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().deleteStockStatus(msg);
                }
            }
        });
    }
}
