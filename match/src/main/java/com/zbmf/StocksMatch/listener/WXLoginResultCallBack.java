package com.zbmf.StocksMatch.listener;

/**
 * Created by pq
 * on 2018/4/26.
 */

public interface WXLoginResultCallBack {
    void onSuccess(String msg);
    void onFail(String msg);
}
