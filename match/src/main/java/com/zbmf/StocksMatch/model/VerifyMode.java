package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.VerifyCodeBean;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.GsonUtil;

/**
 * Created by pq
 * on 2018/4/16.
 */

public class VerifyMode extends BaseVerifyMode {

    public void verifyCode(String phone, String code, final CallBack callBack) {
        postSubscrube(Method.VER_CODE, SendParam.verifyCode(phone, code), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                VerifyCodeBean verifyCodeBean = GsonUtil.parseData(o, VerifyCodeBean.class);
                if (verifyCodeBean.getStatus()) {
                    callBack.onSuccess(verifyCodeBean);
                } else {
                    callBack.onFail(verifyCodeBean.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }
}
