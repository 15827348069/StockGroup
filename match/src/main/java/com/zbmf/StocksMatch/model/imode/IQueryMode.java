package com.zbmf.StocksMatch.model.imode;

import com.zbmf.worklibrary.model.CallBack;

/**
 * Created by pq
 * on 2018/4/8.
 */

public interface IQueryMode {
    void recordList(String matchID, String page,String id, CallBack callBack);
}
