package com.zbmf.StocksMatch.model.imode;

import com.zbmf.worklibrary.model.CallBack;

/**
 * Created by pq
 * on 2018/4/9.
 */

public interface IPayMode {
    void getPayList(CallBack callBack);
    void wxPay(String id,String pro_num,CallBack callBack);
}
