package com.zbmf.StocksMatch.model.imode;

import com.zbmf.worklibrary.model.CallBack;

/**
 * Created by pq
 * on 2018/3/28.
 */

public interface IGroupWebViewClientMode {
    void getStockRealTimeInfo(String symbol, /*JSONHandler jsonHandler,*/ CallBack callBack);
    void searchUserBlogInfo(String blog_id,CallBack callBack);
    void getVideoInfo(String videoID,CallBack callBack);
}
