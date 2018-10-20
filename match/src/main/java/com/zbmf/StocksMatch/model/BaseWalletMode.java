package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.HostUrl;
import com.zbmf.worklibrary.model.BaseMode;

/**
 * Created by pq
 * on 2018/4/8.
 */

public class BaseWalletMode extends BaseMode {
    @Override
    protected String getHost() {
        return HostUrl.WALLET_URL;
    }
}
