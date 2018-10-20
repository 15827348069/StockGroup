package com.zbmf.StocksMatch.listener;

import com.zbmf.StocksMatch.bean.DealsList;
import com.zbmf.StocksMatch.bean.MatchInfo;
import com.zbmf.StocksMatch.bean.MatchNewAllBean;
import com.zbmf.worklibrary.baseview.BaseView;

/**
 * Created by xuhao
 * on 2017/12/1.
 * 最新交易的数据回调接口
 */

public interface IMatchNeaDealView extends BaseView {
    void RushDealList(DealsList.Result deals_sys);
    void rushMatchBean(MatchInfo matchBean);
    void RushMatchList(MatchNewAllBean.Result userMatch);
    void RushMatchListErr(String msg);
}
