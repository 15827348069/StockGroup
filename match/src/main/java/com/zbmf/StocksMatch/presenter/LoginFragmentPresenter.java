package com.zbmf.StocksMatch.presenter;


import com.zbmf.StocksMatch.bean.LoginUser;
import com.zbmf.StocksMatch.listener.ILoginFragmentView;
import com.zbmf.StocksMatch.model.LoginMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;

/**
 * Created by xuhao
 * on 2017/11/21.
 */

public class LoginFragmentPresenter extends BasePresenter<LoginMode, ILoginFragmentView> {
    @Override
    public void getDatas() {}

    @Override
    public LoginMode initMode() {
        return new LoginMode();
    }

    public void Login(String name, String pass) {
        getMode().login(name, pass, new CallBack<LoginUser>() {
            @Override
            public void onSuccess(LoginUser user) {
                if (getView()!=null){
                    getView().dissLoading();
                    getView().onLoginSucceed(user);
                }
            }

            @Override
            public void onFail(String code) {
                if (getView()!=null){
                    getView().onLoginErr(code);
                }
            }
        });
    }
}
