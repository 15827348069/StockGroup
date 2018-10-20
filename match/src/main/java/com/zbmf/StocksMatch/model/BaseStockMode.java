package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.HostUrl;
import com.zbmf.StocksMatch.common.SharedKey;
import com.zbmf.worklibrary.model.BaseMode;
import com.zbmf.worklibrary.util.SharedpreferencesUtil;

/**
 * Created by pq
 * on 2018/3/15.
 * 股票相关的域名
 */

public class BaseStockMode extends BaseMode{

    @Override
    protected String getHost() {
        return SharedpreferencesUtil.getInstance().getString(SharedKey.GUPIAO_HOST, HostUrl.GUPIAO_URLS);
    }
}
