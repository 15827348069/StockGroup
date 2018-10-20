package com.zbmf.StocksMatch.presenter;

import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.BaseBean;
import com.zbmf.StocksMatch.listener.IApplyMatchView;
import com.zbmf.StocksMatch.model.MatchApplyMode;
import com.zbmf.StocksMatch.model.QueryMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;

import java.util.Map;

/**
 * Created by xuhao
 * on 2017/12/12.
 */

public class ApplyMatchPresenter extends BasePresenter<MatchApplyMode, IApplyMatchView> {
    private String matchId;

    public ApplyMatchPresenter(String matchId) {
        this.matchId = matchId;
    }

    @Override
    public void getDatas() {

    }

    @Override
    public MatchApplyMode initMode() {
        return new MatchApplyMode();
    }

    public void AppleMatchMode(Map<String, String> map) {
        map.put(ParamsKey.MATCH_ID, matchId);
        getMode().onApplyMatch(map, new CallBack() {
            @Override
            public void onSuccess(Object o) {
                if (getView() != null) {
                    getView().onSucceed();
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView()!=null){
                    getView().onFail(msg);
                }
            }
        });
    }

    public void sendCode(int matchID, String mobile) {
        new QueryMode().applyMatchCode(matchID, mobile, new CallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean o) {
                if (getView() != null) {
                    getView().successCode(o);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().sendCodeErr(msg);
                }
            }
        });
//        getMode().applyMatchCode(matchID,mobile, new CallBack<BaseBean>() {
//            @Override
//            public void onSuccess(BaseBean baseBean) {
//                if (getView()!=null){
//                    getView().successCode(baseBean);
//                }
//            }
//
//            @Override
//            public void onFail(String msg) {
//                Logx.e(msg);
//            }
//        });
    }
}
