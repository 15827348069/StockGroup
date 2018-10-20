package com.zbmf.StocksMatch.presenter;

import android.text.TextUtils;

import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.HolderPositionBean;
import com.zbmf.StocksMatch.bean.TraderHolderPosition;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.listener.IMatchHold;
import com.zbmf.StocksMatch.model.JoinMatchMode;
import com.zbmf.StocksMatch.model.MatchHoldMode;
import com.zbmf.StocksMatch.util.MatchSharedUtil;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;

import java.util.List;

/**
 * Created by xuhao
 * on 2017/12/18.
 * 获取持仓数据 / 交易数据
 */

public class MatchHoldPresenter extends BasePresenter<MatchHoldMode, IMatchHold> {
    private String matchId;
    private String userId;
    private int flag;

    public MatchHoldPresenter(String matchId, String userId,int flag) {
        this.matchId = matchId;
        this.userId = userId;
        this.flag=flag;
    }

    @Override
    public void getDatas() {
        if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(matchId)&&flag!=0) {
            if (flag== IntentKey.DEFAULT_FLAG_INT){
                getHolderList(String.valueOf(ParamsKey.D_PAGE),Integer.parseInt(matchId), MatchSharedUtil.UserId());
            }else if (flag==IntentKey.TRADER_FLAG_INT){
                traderHolderPositionRecord(userId);
            }
        }
    }

    @Override
    public MatchHoldMode initMode() {
        return new MatchHoldMode();
    }

/*    //操盘高手交易记录
    public void traderDealRecord(int userID, int page, int perPage) {
        getMode().getTraderDealRecord(userID, page, perPage, new CallBack<TraderDeals.Result>() {
            @Override
            public void onSuccess(TraderDeals.Result result) {
                if (getView()!=null){
                    getView().onRefreshDealRecord(result);
                }
            }

            @Override
            public void onFail(String msg) {
                Logx.e(msg);
            }
        });
    }*/

    //操盘高手持仓记录
    public void traderHolderPositionRecord(String userID) {
        getMode().getTraderHolderRecord(userID, new CallBack<List<TraderHolderPosition.Holds>>() {
            @Override
            public void onSuccess(List<TraderHolderPosition.Holds> holds) {
                if (getView() != null) {
                    getView().onRefreshHolderRecord(holds);
                    setFirst(false);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().holderListErr(msg);
                }
            }
        });
    }

    public void getHolderList(String page,int matchId,String userId) {
        new JoinMatchMode().holderPosition(matchId,page,userId, new CallBack<HolderPositionBean.Result>() {
            @Override
            public void onSuccess(HolderPositionBean.Result o) {
                if (getView() != null) {
                    getView().RushHoldList(o);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().holderListErr(msg);
                }
            }
        });
    }
}
