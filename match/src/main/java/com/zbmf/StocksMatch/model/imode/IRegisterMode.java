package com.zbmf.StocksMatch.model.imode;

import com.zbmf.worklibrary.model.CallBack;
/**
 * Created by pq
 * on 2018/3/13.
 */

public interface IRegisterMode {
    void sendRegisterCode(String phone,CallBack callBack);
    void register(String name,String password,String code,CallBack callback);
}
