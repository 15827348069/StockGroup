package com.zbmf.StocksMatch.presenter;

import android.text.TextUtils;

import com.zbmf.StocksMatch.bean.DealsRecordList;
import com.zbmf.StocksMatch.listener.IQueryView;
import com.zbmf.StocksMatch.model.QueryMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;

/**
 * Created by pq
 * on 2018/4/8.
 */

public class QueryPresenter extends BasePresenter<QueryMode, IQueryView> {
    private String matchID;
    private String page;
    private String id;

    public QueryPresenter(String matchID, String page, String id) {
        this.matchID = matchID;
        this.page = page;
        this.id = id;
    }

    @Override
    public void getDatas() {
        if (isFirst()) {
            setFirst(false);
        }
        if (!TextUtils.isEmpty(matchID) && !TextUtils.isEmpty(page) && !TextUtils.isEmpty(id)) {
            getDealList(matchID, page, id);
        }
    }

    @Override
    public QueryMode initMode() {
        return new QueryMode();
    }

    public void getDealList(String matchID, String page, String id) {
        getMode().recordList(matchID, page, id, new CallBack<DealsRecordList.Result>() {
            @Override
            public void onSuccess(DealsRecordList.Result o) {
                if (getView() != null) {
                    getView().queryData(o);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().queryErr(msg);
                }
            }
        });
    }
}
