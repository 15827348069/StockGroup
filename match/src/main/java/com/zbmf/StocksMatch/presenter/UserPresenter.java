package com.zbmf.StocksMatch.presenter;

import android.content.Context;

import com.zbmf.StocksMatch.model.UserMode;
import com.zbmf.StocksMatch.model.imode.UserModeView;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;

import org.json.JSONObject;

/**
 * Created by pq
 * on 2018/3/26.
 */

public class UserPresenter extends BasePresenter<UserMode, UserModeView> {
    @Override
    public void getDatas() {

    }

    @Override
    public UserMode initMode() {
        return new UserMode();
    }

    public void upAvatar(String avatar, Context context) {
        getMode().upAvatar(avatar, context, new CallBack<JSONObject>() {
            @Override
            public void onSuccess(JSONObject o) {
                if (getView() != null) {
                    getView().upAvatar(o);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().upErr(msg);
                }
            }
        });
    }

    public void upDateUser(String nickName) {
        getMode().upDateUser(nickName, new CallBack<String>() {
            @Override
            public void onSuccess(String o) {
                if (getView() != null) {
                    getView().nickName(o);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().upErr(msg);
                }
            }
        });
    }
}
