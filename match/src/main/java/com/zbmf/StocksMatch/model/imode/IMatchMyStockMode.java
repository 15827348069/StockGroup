package com.zbmf.StocksMatch.model.imode;

import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.pullrefreshrecycle.RefreshStatus;

/**
 * Created by xuhao
 * on 2017/12/19.
 */

public interface IMatchMyStockMode {
    void getMyStock(int page, RefreshStatus status, CallBack callBack);
}
