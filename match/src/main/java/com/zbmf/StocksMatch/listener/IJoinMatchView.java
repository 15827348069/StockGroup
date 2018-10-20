package com.zbmf.StocksMatch.listener;

import com.zbmf.StocksMatch.bean.HolderPositionBean;
import com.zbmf.StocksMatch.bean.JoinMatchBean;
import com.zbmf.StocksMatch.bean.MatchBottomAdsBean;
import com.zbmf.StocksMatch.bean.MatchDescBean;
import com.zbmf.StocksMatch.bean.MatchInfo;
import com.zbmf.StocksMatch.bean.NoticeBean;
import com.zbmf.StocksMatch.bean.PopWindowBean;
import com.zbmf.StocksMatch.bean.UserWallet;
import com.zbmf.worklibrary.baseview.BaseView;

/**
 * Created by pq
 * on 2018/3/19.
 */

public interface IJoinMatchView extends BaseView {
    void refreshMatchJoin(JoinMatchBean.Result result);
    void refreshMatchJoinErr(String msg);
    void refreshMatchHolder(HolderPositionBean.Result result);
//    void rushHold(StockHoldList stockHoldList);
//    void rushHolderErr(String msg);
    void matchInfo(MatchInfo matchInfo);
    void userWallet(UserWallet userWallet);
    void userWalletErr(String msg);
    void resetOnSuccess(String msg);
    void resetOnFail(String msg);
    void notice(NoticeBean.Result o);
    void noticeErr(String msg);
    void popWindow(PopWindowBean popWindowBean, int gainStatus,int match_id);
    void refreshMatchDesc(MatchDescBean.Result result,int gainStatus);
    void bottomAds(MatchBottomAdsBean adverts);

    void RushHoldList(HolderPositionBean.Result result);
    void holderListErr(String msg);
}
