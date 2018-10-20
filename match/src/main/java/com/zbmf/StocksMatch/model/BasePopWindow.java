package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.HostUrl;
import com.zbmf.worklibrary.model.BaseMode;

/**
 * Created by pq
 * on 2018/5/15.
 */

public class BasePopWindow extends BaseMode {
    @Override
    protected String getHost() {
        return HostUrl.POP_WINDOW;
    }
}
