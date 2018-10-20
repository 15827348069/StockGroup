package com.zbmf.StocksMatch.presenter;

import com.zbmf.StocksMatch.bean.LoginUser;
import com.zbmf.StocksMatch.listener.IRegisterFragmentView;
import com.zbmf.StocksMatch.model.RegisterMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;

/**
 * Created by pq
 * on 2018/3/13.
 */

public class RegisterFragmentPresenter extends BasePresenter<RegisterMode, IRegisterFragmentView> {
    public void mRegister(String name, String password, String code) {
        getMode().register(name, password, code, new CallBack<LoginUser>() {
            @Override
            public void onSuccess(LoginUser loginUser) {
                if (getView() != null) {
                    getView().onRegisterSucceed(loginUser);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().onRegisterErr(msg);
                }
            }
        });
    }

    @Override
    public void getDatas() {

    }

    @Override
    public RegisterMode initMode() {
        return new RegisterMode();
    }

    public void sendRegisterCode(String phone) {
        getMode().sendRegisterCode(phone, new CallBack<String>() {
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
}
