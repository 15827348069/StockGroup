package com.zbmf.StocksMatch.presenter;

import android.text.TextUtils;

import com.zbmf.StocksMatch.bean.MatchInfo;
import com.zbmf.StocksMatch.bean.TraderHolderPosition;
import com.zbmf.StocksMatch.bean.TraderInfo;
import com.zbmf.StocksMatch.bean.UserWallet;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.listener.IMatchTraderInfo;
import com.zbmf.StocksMatch.model.AddStockMode;
import com.zbmf.StocksMatch.model.MatchTraderMode;
import com.zbmf.StocksMatch.model.WalletMode;
import com.zbmf.StocksMatch.util.MatchSharedUtil;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;
import com.zbmf.worklibrary.util.Logx;

import java.util.List;

/**
 * Created by xuhao
 * on 2017/12/19.
 */

public class MatchTraderInfoPresenter extends BasePresenter<MatchTraderMode, IMatchTraderInfo> {
    private String traderId;

    public MatchTraderInfoPresenter(String traderId) {
        this.traderId = traderId;
    }

    @Override
    public void getDatas() {
        if (isFirst()) {
            setFirst(false);
        }
        getUserWallet();
        getMatchDetail(Constans.MATCH_ID, MatchSharedUtil.UserId());
        //获取操盘高手的信息
        if (!TextUtils.isEmpty(traderId)){//牛云  ID 65
            getTraderInfo(traderId);
            traderHolderPositionRecord(traderId);
        }
    }

    @Override
    public MatchTraderMode initMode() {
        return new MatchTraderMode();
    }

    public void getTraderInfo(String traderId){
        getMode().getMatchTraderList(traderId, new CallBack<TraderInfo>() {
            @Override
            public void onSuccess(TraderInfo traderInfo) {
                if (getView()!=null){
                    getView().onRushTraderInfo(traderInfo);
                    getView().dissLoading();
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView()!=null) {
                    getView().showToastMsg(msg);
                    getView().dissLoading();
                }
            }
        });
    }

    public void getMatchDetail(String matchID,String userId) {
        new AddStockMode().getMatchDetail(matchID, userId,new CallBack<MatchInfo>() {
            @Override
            public void onSuccess(MatchInfo matchInfo) {
                if (getView()!=null){
                    getView().rushMatchBean(matchInfo);
                    setFirst(false);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView()!=null) {
                    getView().rushMatchBeanErr(msg);
                    setFirst(false);
                }
            }
        });
    }

    public void traderBuy(String userID){
        getMode().traderBuy(userID, new CallBack<String>() {
            @Override
            public void onSuccess(String o) {
                if (getView()!=null){
                    getView().traderBuyState(o);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView()!=null){
                    getView().traderBuyState(msg);
                }
            }
        });
    }

    //操盘高手交易记录
//    public void traderDealRecord(String userID, int page, int perPage) {
//        getMode().getTraderDealRecord(userID, page, perPage, new CallBack<TraderDeals.Result>() {
//            @Override
//            public void onSuccess(TraderDeals.Result result) {
//                if (getView()!=null){
//                    getView().onRefreshDealRecord(result);
//                }
//            }
//
//            @Override
//            public void onFail(String msg) {
//                Logx.e(msg);
//            }
//        });
//    }

    //操盘高手持仓记录
    public void traderHolderPositionRecord(String userID) {
        getMode().getTraderHolderRecord(userID, new CallBack<List<TraderHolderPosition.Holds>>() {
            @Override
            public void onSuccess(List<TraderHolderPosition.Holds> holds) {
                if (getView() != null) {
                    getView().onRefreshHolderRecord(holds);
                }
            }

            @Override
            public void onFail(String msg) {
                Logx.e(msg);
            }
        });
    }

    public void getUserWallet() {
        new WalletMode().getUserMoney(new CallBack<UserWallet>() {
            @Override
            public void onSuccess(UserWallet o) {
                if (getView() != null) {
                    getView().userWallet(o);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().userWalletErr(msg);
                }
            }
        });
    }
}
