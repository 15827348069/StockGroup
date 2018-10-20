package com.zbmf.StocksMatch.model.imode;

import com.zbmf.worklibrary.model.CallBack;

/**
 * Created by pq
 * on 2018/3/19.
 */

public interface IJoinMatchMode {
    void joinMatch(int matchID,String userID, CallBack callBack);
    void holderPosition(int matchID,String page,String userId,CallBack callBack);
    void getUserMatch(String page,String userId,CallBack callBack);
}
