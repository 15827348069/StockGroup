package com.zbmf.StocksMatch.presenter;

import android.text.TextUtils;

import com.zbmf.StocksMatch.bean.PrizeListBean;
import com.zbmf.StocksMatch.listener.IWinPrizeView;
import com.zbmf.StocksMatch.model.WinAPrizeMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;

/**
 * Created by pq
 * on 2018/4/8.
 */

public class WinAPrizePresenter extends BasePresenter<WinAPrizeMode, IWinPrizeView> {
    private String matchID, userID, page;

    public WinAPrizePresenter(String matchID, String userID, String page) {
        this.matchID = matchID;
        this.userID = userID;
        this.page = page;
    }

    @Override
    public void getDatas() {
        if (isFirst()) {
            setFirst(true);
        }
        if (!TextUtils.isEmpty(matchID) && !TextUtils.isEmpty(userID) && !TextUtils.isEmpty(page)) {
            getWinAPrizeList(matchID, userID, page);
        }
    }

    @Override
    public WinAPrizeMode initMode() {
        return new WinAPrizeMode();
    }

    public void getWinAPrizeList(String matchID, String userID, String page) {
        getMode().getUserPrizeList(matchID, userID, page, new CallBack<PrizeListBean.Result>() {
            @Override
            public void onSuccess(PrizeListBean.Result o) {
                if (getView() != null) {
                    getView().winAPrizeList(o);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().winPrizeErr(msg);
                }
            }
        });
    }
}
