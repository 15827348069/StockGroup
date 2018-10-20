package com.zbmf.StocksMatch.listener;

import com.zbmf.StocksMatch.bean.MatchInfo;
import com.zbmf.StocksMatch.bean.TraderDeals;
import com.zbmf.StocksMatch.bean.TraderHolderPosition;
import com.zbmf.StocksMatch.bean.TraderInfo;
import com.zbmf.StocksMatch.bean.UserWallet;
import com.zbmf.worklibrary.baseview.BaseView;

import java.util.List;

/**
 * Created by xuhao
 * on 2017/12/19.
 */

public interface IMatchTraderInfo extends BaseView {
    void rushMatchBean(MatchInfo matchInfo);
    void rushMatchBeanErr(String msg);
    void traderBuyState(String msg);
    void onRushTraderInfo(TraderInfo traderInfo);
    void onRefreshHolderRecord(List<TraderHolderPosition.Holds> holds);
    void onRefreshDealRecord(TraderDeals.Result result);
    void userWallet(UserWallet userWallet);
    void userWalletErr(String msg);
}
