package com.zbmf.StocksMatch.listener;

import com.zbmf.StocksMatch.bean.HolderPositionBean;
import com.zbmf.StocksMatch.bean.TraderHolderPosition;
import com.zbmf.worklibrary.baseview.BaseView;

import java.util.List;

/**
 * Created by xuhao
 * on 2017/12/18.
 */

public interface IMatchHold extends BaseView{
    void RushHoldList(HolderPositionBean.Result result);
    void holderListErr(String msg);
    void onRefreshHolderRecord(List<TraderHolderPosition.Holds> holds);
   /* void onRefreshDealRecord(TraderDeals.Result result);*/
}
