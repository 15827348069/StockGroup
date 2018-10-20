package com.zbmf.StocksMatch.presenter;

import com.zbmf.StocksMatch.bean.Traders;
import com.zbmf.StocksMatch.listener.ITraderRankView;
import com.zbmf.StocksMatch.model.TraderRankMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;
import com.zbmf.worklibrary.util.Logx;

import java.util.List;

/**
 * Created by pq
 * on 2018/3/28.
 */

public class TraderRankPresenter extends BasePresenter<TraderRankMode, ITraderRankView> {
    @Override
    public void getDatas() {
        if (isFirst()) {
            setFirst(false);
        }
        getTraderRank();
    }

    @Override
    public TraderRankMode initMode() {
        return new TraderRankMode();
    }

    private void getTraderRank() {
        getMode().getTraderRank(new CallBack<List<Traders>>() {
            @Override
            public void onSuccess(List<Traders> o) {
                if (getView() != null) {
                    getView().traderRank(o);
                }
            }

            @Override
            public void onFail(String msg) {
                Logx.e(msg);
            }
        });
    }
}
