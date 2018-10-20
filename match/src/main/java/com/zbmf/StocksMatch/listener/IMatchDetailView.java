package com.zbmf.StocksMatch.listener;

import com.zbmf.StocksMatch.bean.HolderPositionBean;
import com.zbmf.StocksMatch.bean.MatchInfo;
import com.zbmf.worklibrary.baseview.BaseView;

/**
 * Created by xuhao
 * on 2017/11/29.
 */

public interface IMatchDetailView extends BaseView{
    void RushMatchDetail(MatchInfo matchInfo);
//    void RushMatchHold(StockHoldList stockHoldList);

    void RushHoldList(HolderPositionBean.Result result);
    void holderListErr(String msg);
}
