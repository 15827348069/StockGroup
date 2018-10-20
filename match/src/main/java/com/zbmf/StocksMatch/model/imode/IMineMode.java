package com.zbmf.StocksMatch.model.imode;

import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.pullrefreshrecycle.RefreshStatus;

/**
 * Created by xuhao
 * on 2017/11/30.
 */

public interface IMineMode {
    void getMineDetail(CallBack callBack);
    void getMatchList(RefreshStatus status,CallBack callBack);
}
