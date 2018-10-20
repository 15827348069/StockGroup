package com.zbmf.StocksMatch.model.imode;

import com.zbmf.worklibrary.model.CallBack;

/**
 * Created by pq
 * on 2018/3/22.
 */

public interface INewAllMatchMode {
void newAllMatchMode(int page, int perPage, String method,CallBack callBack);
}
