package com.zbmf.StocksMatch.model.imode;

import com.zbmf.worklibrary.model.CallBack;

/**
 * Created by pq
 * on 2018/3/21.
 */

public interface ISearchMode {
    void searchMatch(String keyWord,int page,int per_page, CallBack callBack);
}
