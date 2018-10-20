package com.zbmf.StocksMatch.listener;

import com.zbmf.StocksMatch.bean.MatchList3Bean;
import com.zbmf.worklibrary.baseview.BaseView;

/**
 * Created by xuhao
 * on 2017/11/28.
 */

public interface IMatchCityFragment extends BaseView {
    void RushCityList(MatchList3Bean.Result result);
    void RushCityErr(String msg);
}
