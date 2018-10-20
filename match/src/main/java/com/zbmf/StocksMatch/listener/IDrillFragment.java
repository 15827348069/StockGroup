package com.zbmf.StocksMatch.listener;

import com.zbmf.StocksMatch.bean.HolderPositionBean;
import com.zbmf.StocksMatch.bean.MatchInfo;
import com.zbmf.StocksMatch.bean.NoticeBean;
import com.zbmf.StocksMatch.bean.UserWallet;
import com.zbmf.worklibrary.baseview.BaseView;

/**
 * Created by xuhao
 * on 2017/12/11.
 */

public interface IDrillFragment extends BaseView{
    void rushMatchBean(MatchInfo matchBean);
//    void rushHold(StockHoldList stockHoldList);
    void rushHoldErr(String msg);
    void userWallet(UserWallet userWallet);
    void userWalletErr(String msg);
    void resetOnSuccess(String msg);
    void resetOnFail(String msg);
    void notice(NoticeBean.Result notice);
    void noticeErr(String msg);
    void RushHoldList(HolderPositionBean.Result result);
    void holderListErr(String msg);
}
