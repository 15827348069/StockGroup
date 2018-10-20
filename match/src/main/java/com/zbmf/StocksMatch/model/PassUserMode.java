package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.QQSinaLoginBean;
import com.zbmf.StocksMatch.bean.WeChatLoginBean;
import com.zbmf.StocksMatch.model.imode.IPassUserMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.GsonUtil;

/**
 * Created by pq
 * on 2018/4/10.
 */

public class PassUserMode extends BasePassUrl implements IPassUserMode {

    @Override
    public void loginWeChat(String code, final CallBack callBack) {
        postSubscrube(Method.LOGIN_WE_CHAT, SendParam.loginWeChat(code), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                WeChatLoginBean weChatLoginBean = GsonUtil.parseData(o, WeChatLoginBean.class);
                if (weChatLoginBean.getStatus()) {
                    callBack.onSuccess(weChatLoginBean);
                } else {
                    callBack.onFail(weChatLoginBean.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    @Override
    public void loginQQSina(String open_id, String token, String api_type, final CallBack callBack) {
        postSubscrube(Method.LOGIN_Q_SINA, SendParam.loginForQQ_Sina(open_id, token, api_type), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                QQSinaLoginBean qqSinaLoginBean = GsonUtil.parseData(o, QQSinaLoginBean.class);
                if (qqSinaLoginBean.getStatus()) {
                    callBack.onSuccess(qqSinaLoginBean);
                } else {
                    callBack.onFail(qqSinaLoginBean.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }


}
