package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.HostUrl;
import com.zbmf.worklibrary.model.BaseMode;

/**
 * Created by pq
 * on 2018/3/31.
 */

public class BaseStockDetailMode extends BaseMode {
    @Override
    protected String getHost() {
        return HostUrl.STOCKS_URL;
    }
}
