package com.zbmf.StocksMatch.listener;

import com.zbmf.StocksMatch.bean.LoginUser;
import com.zbmf.StocksMatch.bean.VerifyCodeBean;
import com.zbmf.worklibrary.baseview.BaseView;

/**
 * Created by pq
 * on 2018/4/16.
 */

public interface ResetPassword extends BaseView {
    void resetPassword(String msg);
    void resetPasswordErr(String msg);
    void verifyCodeSuccess(VerifyCodeBean verifyCodeBean);
    void verifyCodeBeanFail(String msg);
    void sendCodeSuccess(String msg);
    void sendCodeFail(String msg);
    void onLoginSucceed(LoginUser user);     //登录成功
    void onLoginErr(String msg);
    void forgetPassword(String msg);
}
