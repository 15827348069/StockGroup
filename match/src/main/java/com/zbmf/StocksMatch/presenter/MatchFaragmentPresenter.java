package com.zbmf.StocksMatch.presenter;

import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.MatchNewAllBean;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.listener.IMatchFragment;
import com.zbmf.StocksMatch.model.MatchListMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;


/**
 * Created by xuhao
 * on 2017/11/28.
 */

public class MatchFaragmentPresenter extends BasePresenter<MatchListMode,IMatchFragment> {
    private int flag,page;
    public MatchFaragmentPresenter(int flag){
        this.flag=flag;
    }
    @Override
    public void getDatas() {
        if (flag== Constans.NEW_MATCH){
            getMatchList(ParamsKey.D_PAGE,10, Method.MATCH_NEW);
        }else if (flag==Constans.All_MATCH){
            getMatchList(ParamsKey.D_PAGE,ParamsKey.D_PERPAGE, Method.MATCH_ALL);
        }
    }

    @Override
    public MatchListMode initMode() {
        return new MatchListMode();
    }
    public void getMatchList(int page,int perPage,String method){
        getMode().newAllMatchMode(page, perPage,method, new CallBack<MatchNewAllBean.Result>() {
            @Override
            public void onSuccess(MatchNewAllBean.Result result) {
                if (getView()!=null){
                    getView().RushMatchList(result);
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
