package com.zbmf.StocksMatch.presenter;

import android.text.TextUtils;

import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.OrderList;
import com.zbmf.StocksMatch.listener.ITrustListView;
import com.zbmf.StocksMatch.model.MyTrustMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;

/**
 * Created by pq
 * on 2018/4/4.
 */

public class MyTrustPresenter extends BasePresenter<MyTrustMode, ITrustListView> {
private String matchID;

    public MyTrustPresenter(String matchID) {
        this.matchID = matchID;
    }

    @Override
    public void getDatas() {
        if (isFirst()) {
            setFirst(false);
        }
        if (!TextUtils.isEmpty(matchID)){
            getMyOrderList(matchID, String.valueOf(ParamsKey.D_PAGE));
        }
    }

    @Override
    public MyTrustMode initMode() {
        return new MyTrustMode();
    }

    public void getMyOrderList(String matchID, String page) {
        getMode().getTrustList(matchID, page, new CallBack<OrderList.Result>() {
            @Override
            public void onSuccess(OrderList.Result o) {
             if (getView()!=null){
                 getView().getTrustList(o);
             }
            }

            @Override
            public void onFail(String msg) {
                if (getView()!=null){
                    getView().err(msg);
                }
            }
        });
    }

    public void revoke(String id ,String matchID){
        getMode().revoke(id, matchID, new CallBack<String>(){
            @Override
            public void onSuccess(String o) {
                if (getView()!=null){
                    getView().revokeResult(o);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView()!=null){
                    getView().revokeResult(msg);
                }
            }
        });
    }
}
