package com.zbmf.StocksMatch.listener;

import com.zbmf.StocksMatch.bean.MatchNewAllBean;

/**
 * Created by pq
 * on 2018/4/27.
 */

public interface FlowItemClickToBuy {
    void flowItemClickToBuy(MatchNewAllBean.Result.Matches matches);
}
