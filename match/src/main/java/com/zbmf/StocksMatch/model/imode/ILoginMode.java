package com.zbmf.StocksMatch.model.imode;

import com.zbmf.worklibrary.model.CallBack;

/**
 * Created by xuhao
 * on 2017/11/21.
 */

public interface ILoginMode{
    void login(String name, String pass, CallBack callBack);
    void forgetPassword(String phone,CallBack callBack);
}
