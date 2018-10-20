package com.zbmf.StocksMatch.listener;

import com.zbmf.StocksMatch.bean.MatchDescBean;
import com.zbmf.StocksMatch.bean.PopWindowBean;
import com.zbmf.worklibrary.baseview.BaseView;

/**
 * Created by pq
 * on 2018/3/19.
 */

public interface IMatchDescView extends BaseView{
    void refreshMatchDesc(MatchDescBean.Result result);
    void popWindow(PopWindowBean popWindowBean, int gainStatus,int matchID);
}
