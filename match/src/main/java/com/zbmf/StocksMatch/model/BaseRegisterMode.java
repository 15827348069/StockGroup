package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.HostUrl;
import com.zbmf.worklibrary.model.BaseMode;

/**
 * Created by pq
 * on 2018/3/13.
 * 用户注册时的域名
 */

public class BaseRegisterMode extends BaseMode {
    @Override
    protected String getHost() {
        return HostUrl.PASS_URLS;
    }
}
