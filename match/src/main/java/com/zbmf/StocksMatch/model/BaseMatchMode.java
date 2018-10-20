package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.HostUrl;

import com.zbmf.StocksMatch.common.SharedKey;
import com.zbmf.worklibrary.model.BaseMode;
import com.zbmf.worklibrary.util.SharedpreferencesUtil;

/**
 * Created by xuhao
 * on 2017/11/22.
 * 炒股大赛的域名
 */

public class BaseMatchMode extends BaseMode{
    @Override
    protected String getHost() {
        return SharedpreferencesUtil.getInstance().getString(SharedKey.MATCH_HOST,HostUrl.MATCH_URL);
    }
}
