package com.zbmf.StocksMatch.presenter;

import com.zbmf.StocksMatch.bean.MatchDescBean;
import com.zbmf.StocksMatch.bean.PopWindowBean;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.listener.IMatchDescView;
import com.zbmf.StocksMatch.model.MatchDescMode;
import com.zbmf.StocksMatch.model.PopWindowMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;
import com.zbmf.worklibrary.util.Logx;

/**
 * Created by pq
 * on 2018/3/19.
 */

public class MatchDescPresenter extends BasePresenter<MatchDescMode,IMatchDescView> {
    private int matchID;

    public MatchDescPresenter(int matchID) {
        this.matchID = matchID;
    }

    @Override
    public void getDatas() {
        matchDesc(matchID);
        getPopWindow(matchID);//弹窗
    }

    @Override
    public MatchDescMode initMode() {
        return new MatchDescMode();
    }
    //获取比赛详情
    public void matchDesc(int match_id){
        getMode().matchDesc(match_id, new CallBack<MatchDescBean.Result>() {
            @Override
            public void onSuccess(MatchDescBean.Result result) {
                if (result!=null){
                    getView().refreshMatchDesc(result);
                }
            }

            @Override
            public void onFail(String msg) {
                Logx.e(msg);
            }
        });
    }

    //获取弹窗
    private void getPopWindow(final int match_id) {
        new PopWindowMode().getPopWindow(String.valueOf(match_id), new CallBack<PopWindowBean>() {
            @Override
            public void onSuccess(PopWindowBean popWindowBean) {
                if (getView() != null) {
                    getView().popWindow(popWindowBean, Constans.GAIN_DATA_SUCCESS,match_id);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().popWindow(null,Constans.GAIN_DATA_FAIL,match_id);
                }
            }
        });
    }

}
