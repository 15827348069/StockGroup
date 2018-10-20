package com.zbmf.StocksMatch.model.imode;

import com.zbmf.worklibrary.model.CallBack;

/**
 * Created by pq
 * on 2018/4/4.
 */

public interface IMyTrustMode {
    void getTrustList(String match_id, String page, CallBack callBack);
    void buyMatchStock(String symbol, String price, String volumn,String matchID,CallBack callBack);
    void revoke(String id,String matchId,CallBack callBack);
}
