package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.HostUrl;
import com.zbmf.StocksMatch.common.SharedKey;
import com.zbmf.worklibrary.model.BaseMode;
import com.zbmf.worklibrary.util.SharedpreferencesUtil;

/**
 * Created by pq
 * on 2018/3/26.
 */

public class BaseUserMode extends BaseMode {
    @Override
    protected String getHost() {
        return SharedpreferencesUtil.getInstance().getString(SharedKey.PASS_HOST, HostUrl.PASS_URL);
    }
}
