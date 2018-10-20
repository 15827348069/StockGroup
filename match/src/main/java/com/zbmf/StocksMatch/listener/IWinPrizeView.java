package com.zbmf.StocksMatch.listener;

import com.zbmf.StocksMatch.bean.PrizeListBean;
import com.zbmf.worklibrary.baseview.BaseView;

/**
 * Created by pq
 * on 2018/4/8.
 */

public interface IWinPrizeView extends BaseView{
    void winAPrizeList(PrizeListBean.Result result);
    void winPrizeErr(String msg);
}
