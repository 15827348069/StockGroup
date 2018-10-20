package com.zbmf.StocksMatch.model;

import android.util.Log;

import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.BaseBean;
import com.zbmf.StocksMatch.bean.LoginUser;
import com.zbmf.StocksMatch.common.SharedKey;
import com.zbmf.StocksMatch.model.imode.ILoginMode;
import com.zbmf.StocksMatch.util.MatchSharedUtil;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.GsonUtil;
import com.zbmf.worklibrary.util.SharedpreferencesUtil;

/**
 * Created by xuhao
 * on 2017/11/21.
 */

public class LoginMode extends BasePaSSMode implements ILoginMode {
    @Override
    public void login(String name, String pass, final CallBack callBack) {
        postSubscrube(Method.LOGIN, SendParam.getLoginMap(name, pass), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                LoginUser loginUser = GsonUtil.parseData(o, LoginUser.class);
                assert loginUser != null;
                if (loginUser.getStatus()) {
                    MatchSharedUtil.saveUser(loginUser);
                    Log.i("--TAG","---- 存储用户登录信息   o  "+o);
                    SharedpreferencesUtil.getInstance().putString(SharedKey.USER_ID,String.valueOf(loginUser.getUser().getUser_id()));
                    callBack.onSuccess(loginUser);
                } else {
                    callBack.onFail(loginUser.getErr().getMsg());
                }

            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    @Override
    public void forgetPassword(String phone, final CallBack callBack) {
        postSubscrube(Method.FORGET_PASSWORD, SendParam.forgetPassword(phone), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                BaseBean baseBean = GsonUtil.parseData(o, BaseBean.class);
                if (baseBean.getStatus()) {
                    callBack.onSuccess("设置忘记密码成功");
                } else {
                    callBack.onFail(baseBean.getErr().getMsg());
                }

            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }
}
