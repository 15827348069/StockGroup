package com.zbmf.StocksMatch.model;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.zbmf.StocksMatch.api.HostUrl;
import com.zbmf.StocksMatch.api.JSONHandler;
import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.UserNiceAndAvatar;
import com.zbmf.StocksMatch.model.imode.IUserMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.GsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pq
 * on 2018/3/26.
 */

public class UserMode extends BaseUserMode implements IUserMode {
    //上传图像
    @Override
    public void upAvatar(String avatar, Context context, final CallBack callBack) {
        new AsyncHttpClient().post(HostUrl.PASS_URLS, SendParam.upAvatar1(avatar),
                new JSONHandler(true, context, "正在保存...") {
                    @Override
                    public void onSuccess(JSONObject obj) {
                        if (obj != null) {
                            try {
                                if (obj.getString("status").equals("ok")) {
                                    callBack.onSuccess(obj);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(String err_msg) {
                        callBack.onFail(err_msg);
                    }
                });
    }

    @Override
    public void getDefaultAvatar(CallBack callBack) {

    }

    @Override
    public void upDateUser(String nickName, final CallBack callBack) {
        postSubscrube(Method.UP_NICK, SendParam.upUserNick(nickName), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                UserNiceAndAvatar userNiceAndAvatar = GsonUtil.parseData(o, UserNiceAndAvatar.class);
                if (userNiceAndAvatar.getStatus()) {
                    callBack.onSuccess(userNiceAndAvatar.getNickname());
                } else {
                    callBack.onFail(userNiceAndAvatar.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }
}
