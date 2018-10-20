package com.zbmf.StocksMatch.listener;

import com.zbmf.StocksMatch.bean.LoginUser;

import com.zbmf.worklibrary.baseview.BaseView;


/**
 * Created by xuhao
 * on 2017/11/21.
 */

public interface ILoginFragmentView extends BaseView {
    void onLoginSucceed(LoginUser user);     //登录成功
    void onLoginErr(String msg);
//    void forgetPassword(String msg);
//    void forgetPasswordErr(String msg);
}
