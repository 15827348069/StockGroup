package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.LoginUser;
import com.zbmf.StocksMatch.bean.SendRegisterCode;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.model.imode.IRegisterMode;
import com.zbmf.StocksMatch.util.MatchSharedUtil;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.GsonUtil;

/**
 * Created by pq
 * on 2018/3/13.
 */

public class RegisterMode extends BaseRegisterMode implements IRegisterMode {

    @Override
    public void sendRegisterCode(String phone, final CallBack callBack) {
        postSubscrube(Method.REGISTER_CODE, SendParam.sendRegisterCode(phone), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                SendRegisterCode sendRegisterCode = GsonUtil.parseData(o, SendRegisterCode.class);
                if (sendRegisterCode.getStatus()) {
                    callBack.onSuccess("验证码发送成功");
                } else {
                    if (sendRegisterCode.getErr().getCode() == 2103) {
                        callBack.onFail(String.valueOf(Constans.ACCOUNT_EXIT));
                    } else {
                        callBack.onFail("验证码发送失败");
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail("验证码发送失败");
            }
        });
    }

    public void sendBindPhoneCode(String phone,final CallBack callBack){
        postSubscrube(Method.SEND_FORGET_PASSWORD_CODE, SendParam.sendForgetPasswordCode(phone), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                SendRegisterCode sendRegisterCode = GsonUtil.parseData(o, SendRegisterCode.class);
                if (sendRegisterCode.getStatus()) {
                    callBack.onSuccess("验证码发送成功");
                } else {
                    if (sendRegisterCode.getErr().getCode() == Integer.parseInt(Constans.ACCOUNT_EXIT)) {
                        callBack.onFail("该号码未注册！");
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail("验证码发送失败");
            }
        });
    }

    @Override
    public void register(String name, String password, String code, final CallBack callback) {
        //请求后台注册
        postSubscrube(Method.REGISTER, SendParam.register(name, password, code), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                LoginUser loginUser = GsonUtil.parseData(o, LoginUser.class);
                if (loginUser.getStatus()) {
                    MatchSharedUtil.saveUser(loginUser);
                    callback.onSuccess(loginUser);
                } else {
                    callback.onFail(loginUser.getErr().getMsg());
                }

            }

            @Override
            public void onFail(String msg) {
                callback.onFail("注册失败");
            }
        });
    }
}
