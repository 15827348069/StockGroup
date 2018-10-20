package com.zbmf.StocksMatch.presenter;

import com.zbmf.StocksMatch.bean.LoginUser;
import com.zbmf.StocksMatch.bean.VerifyCodeBean;
import com.zbmf.StocksMatch.listener.ResetPassword;
import com.zbmf.StocksMatch.model.ForgetPassWordMode;
import com.zbmf.StocksMatch.model.LoginMode;
import com.zbmf.StocksMatch.model.RegisterMode;
import com.zbmf.StocksMatch.model.VerifyMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;

/**
 * Created by pq
 * on 2018/4/16.
 */

public class ForgetPassWordPresenter extends BasePresenter<ForgetPassWordMode, ResetPassword> {
    @Override
    public void getDatas() {

    }

    @Override
    public ForgetPassWordMode initMode() {
        return new ForgetPassWordMode();
    }

    public void resetPassword(String log_id, String phone, String password) {
        getMode().resetPassword(log_id, phone, password, new CallBack<String>() {
            @Override
            public void onSuccess(String o) {
                if (getView() != null) {
                    getView().resetPassword(o);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().resetPasswordErr(msg);
                }
            }
        });
    }

    public void forget(String phone) {
    new LoginMode().forgetPassword(phone, new CallBack<String>() {
            @Override
            public void onSuccess(String o) {
                if (getView() != null) {
                    getView().forgetPassword(o);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().forgetPassword(msg);
                }
            }
        });
    }


    public void verifyCode(String phone, String code) {
        new VerifyMode().verifyCode(phone, code, new CallBack<VerifyCodeBean>() {
            @Override
            public void onSuccess(VerifyCodeBean verifyCodeBean) {
                if (getView() != null) {
                    getView().verifyCodeSuccess(verifyCodeBean);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().verifyCodeBeanFail(msg);
                }
            }
        });
    }

    public void sendCode(String phone) {
        new RegisterMode().sendBindPhoneCode(phone, new CallBack<String>() {
            @Override
            public void onSuccess(String o) {
                if (getView() != null) {
                    getView().sendCodeSuccess(o);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().sendCodeFail(msg);
                }
            }
        });
    }

    public void login(String name, String password) {
        new LoginMode().login(name, password, new CallBack<LoginUser>() {
            @Override
            public void onSuccess(LoginUser loginUser) {
                if (getView() != null) {
                    getView().onLoginSucceed(loginUser);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().onLoginErr(msg);
                }
            }
        });
    }
}
