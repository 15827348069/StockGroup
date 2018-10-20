package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.HostUrl;

import com.zbmf.StocksMatch.common.SharedKey;
import com.zbmf.worklibrary.model.BaseMode;
import com.zbmf.worklibrary.util.SharedpreferencesUtil;

/**
 * Created by xuhao
 * on 2017/11/22.
 * 炒股圈子的域名
 */

public class BaseGroupMode extends BaseMode{
    @Override
    protected String getHost() {
        return SharedpreferencesUtil.getInstance().getString(SharedKey.GROUP_HOST,HostUrl.GROUP_URL);
    }
}
