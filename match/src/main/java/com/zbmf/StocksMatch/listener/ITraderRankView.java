package com.zbmf.StocksMatch.listener;

import com.zbmf.StocksMatch.bean.Traders;
import com.zbmf.worklibrary.baseview.BaseView;

import java.util.List;

/**
 * Created by pq
 * on 2018/3/28.
 */

public interface ITraderRankView extends BaseView{
    void traderRank(List<Traders>traders);
}
