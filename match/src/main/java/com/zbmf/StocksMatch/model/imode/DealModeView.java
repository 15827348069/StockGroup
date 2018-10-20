package com.zbmf.StocksMatch.model.imode;

import com.zbmf.StocksMatch.bean.DealsRecordList;
import com.zbmf.StocksMatch.bean.TraderDeals;
import com.zbmf.worklibrary.baseview.BaseView;

/**
 * Created by pq
 * on 2018/3/26.
 */

public interface DealModeView extends BaseView{
    void onRefreshDealRecord(TraderDeals.Result result);
    void queryData(DealsRecordList.Result result);
    void queryErr(String msg);
}
