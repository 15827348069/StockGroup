package com.zbmf.StocksMatch.model.imode;

import com.zbmf.worklibrary.model.CallBack;

/**
 * Created by pq
 * on 2018/4/11.
 */

public interface INoticeMode {
    void getMatchNoticeList(String matchId, String page, CallBack callBack);
}
