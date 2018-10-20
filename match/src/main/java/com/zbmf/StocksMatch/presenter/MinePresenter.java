package com.zbmf.StocksMatch.presenter;

import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.MatchList;
import com.zbmf.StocksMatch.bean.MatchNewAllBean;
import com.zbmf.StocksMatch.bean.User;
import com.zbmf.StocksMatch.listener.IMineView;
import com.zbmf.StocksMatch.model.JoinMatchMode;
import com.zbmf.StocksMatch.model.MineMode;
import com.zbmf.StocksMatch.util.MatchSharedUtil;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;
import com.zbmf.worklibrary.pullrefreshrecycle.RefreshStatus;


/**
 * Created by xuhao
 * on 2017/11/30.
 */

public class MinePresenter extends BasePresenter<MineMode, IMineView> {
    @Override
    public void getDatas() {
        getUserInfo();
//        getMatchList(RefreshStatus.LOAD_DEFAULT);
        getUserMatch(String.valueOf(ParamsKey.D_PAGE),MatchSharedUtil.UserId());
    }

    @Override
    public MineMode initMode() {
        return new MineMode();
    }

    private void getMatchList(RefreshStatus status) {
        getMode().getMatchList(status, new CallBack<MatchList>() {
            @Override
            public void onSuccess(MatchList matchList) {
//                getView().RushMatchList(matchList.getMatches());
            }

            @Override
            public void onFail(String msg) {
                if (getView()!=null){
                    getView().onError(msg);
                }
            }
        });
    }

    private void getUserInfo() {
        getMode().getMineDetail(new CallBack<User>() {
            @Override
            public void onSuccess(User user) {
                MatchSharedUtil.saveUser(user);
                if (getView()!=null){
                    getView().RushMineMessage(user);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView()!=null){
                    getView().onError(msg);
                }
            }
        });
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
