package com.zbmf.StocksMatch.presenter;

import android.text.TextUtils;

import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.MatchNewAllBean;
import com.zbmf.StocksMatch.listener.IUserJoinMatchView;
import com.zbmf.StocksMatch.model.JoinMatchMode;
import com.zbmf.StocksMatch.model.UserJoinMatchMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;

/**
 * Created by pq
 * on 2018/4/28.
 */

public class UserJoinMatchPresenter extends BasePresenter<UserJoinMatchMode,IUserJoinMatchView> {
    private String userId;

    public UserJoinMatchPresenter(String userId) {
        this.userId = userId;
    }

    @Override
    public void getDatas() {
        if (isFirst()){
            setFirst(false);
        }
        if (!TextUtils.isEmpty(userId)){
            getUserMatch(String.valueOf(ParamsKey.D_PAGE),userId);
        }
    }

    @Override
    public UserJoinMatchMode initMode() {
        return new UserJoinMatchMode();
    }

    public void getUserMatch(String page,String userId) {
        new JoinMatchMode().getUserMatch(page,userId, new CallBack<MatchNewAllBean.Result>() {
            @Override
            public void onSuccess(MatchNewAllBean.Result o) {
                if (getView() != null) {
                    getView().RushMatchList(o);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView()!=null){
                    getView().RushMatchListErr(msg);
                }
            }
        });
    }
}
