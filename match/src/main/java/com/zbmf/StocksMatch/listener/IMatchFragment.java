package com.zbmf.StocksMatch.listener;

import com.zbmf.StocksMatch.bean.MatchNewAllBean;
import com.zbmf.worklibrary.baseview.BaseView;

/**
 * Created by xuhao
 * on 2017/11/28.
 */

public interface IMatchFragment extends BaseView {
    void RushMatchList(MatchNewAllBean.Result result);
    void RushMatchListErr(String msg);
}
