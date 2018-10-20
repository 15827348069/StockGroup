package com.zbmf.StocksMatch.model.imode;

import com.zbmf.worklibrary.model.CallBack;

/**
 * Created by pq
 * on 2018/4/8.
 */

public interface IWinAPrizeMode {
    void getUserPrizeList(String match_id, String user_id, String page, CallBack callBack);
}
