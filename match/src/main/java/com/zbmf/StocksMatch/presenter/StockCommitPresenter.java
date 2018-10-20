package com.zbmf.StocksMatch.presenter;

import android.text.TextUtils;

import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.StockCommentsBean;
import com.zbmf.StocksMatch.listener.IStockCommentView;
import com.zbmf.StocksMatch.model.StockCommentMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;

/**
 * Created by pq
 * on 2018/3/31.
 */

public class StockCommitPresenter extends BasePresenter<StockCommentMode, IStockCommentView> {
    private String contract_id;
    private String page;

    public StockCommitPresenter(String contract_id, String page) {
        this.contract_id = contract_id;
        this.page = page;
    }

    @Override
    public void getDatas() {
        if (isFirst()) {
            setFirst(false);
        }
        if (!TextUtils.isEmpty(contract_id) && !TextUtils.isEmpty(page)) {
            getStockCommentList(contract_id, String.valueOf(ParamsKey.D_PAGE));
        }
    }

    @Override
    public StockCommentMode initMode() {
        return new StockCommentMode();
    }

    public void getStockCommentList(String contract_id, String page) {
        getMode().getStockCommentList(contract_id, page, new CallBack<StockCommentsBean.Result>() {
            @Override
            public void onSuccess(StockCommentsBean.Result o) {
                  if (getView()!=null){
                      getView().getStockCommentList(o);
                  }
            }

            @Override
            public void onFail(String msg) {
              if(getView()!=null){
                  getView().getStockCommentErr(msg);
                };
            }
        });
    }
    public void addStockComments(String symbol,String desc){
        getMode().addStockComment(symbol, desc, new CallBack<String>() {
            @Override
            public void onSuccess(String o) {
                if (getView()!=null){
                    getView().addStockCommentStatus(o);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView()!=null){
                    getView().addStockCommentStatus(msg);
                }
            }
        });
    }
}
