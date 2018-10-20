package com.zbmf.StocksMatch.presenter;

import android.text.TextUtils;

import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.HolderPositionBean;
import com.zbmf.StocksMatch.bean.MatchInfo;
import com.zbmf.StocksMatch.bean.NoticeBean;
import com.zbmf.StocksMatch.bean.UserWallet;
import com.zbmf.StocksMatch.listener.IDrillFragment;
import com.zbmf.StocksMatch.model.AddStockMode;
import com.zbmf.StocksMatch.model.JoinMatchMode;
import com.zbmf.StocksMatch.model.MatchDetailMode;
import com.zbmf.StocksMatch.model.MatchListMode;
import com.zbmf.StocksMatch.model.WalletMode;
import com.zbmf.StocksMatch.util.MatchSharedUtil;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;

/**
 * Created by xuhao
 * on 2017/12/11.
 */

public class DrillPresenter extends BasePresenter<MatchDetailMode, IDrillFragment> {
    private String matchId;

    public DrillPresenter(String matchId) {
        this.matchId = matchId;
    }

    @Override
    public void getDatas() {
        if (isFirst()) {
            setFirst(false);
        }
        if (!TextUtils.isEmpty(matchId)) {
            getMatchDetail(matchId,MatchSharedUtil.UserId());
            getMatchHolder(matchId,ParamsKey.D_PAGE, MatchSharedUtil.UserId());
            getMatchNotice(matchId, String.valueOf(ParamsKey.D_PAGE));
        }
    }

    @Override
    public MatchDetailMode initMode() {
        return new MatchDetailMode();
    }

    public void getMatchDetail(String matchID,String userId) {
        new AddStockMode().getMatchDetail(matchID, userId,new CallBack<MatchInfo>() {
            @Override
            public void onSuccess(MatchInfo matchInfo) {
                if (getView() != null) {
                    getView().rushMatchBean(matchInfo);
                    setFirst(false);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().rushHoldErr(msg);
                    setFirst(false);
                }
            }
        });
    }

    public void getMatchHolder(String matchId,int page,String userID) {
        new JoinMatchMode().holderPosition(Integer.parseInt(matchId),String.valueOf(page),userID, new CallBack<HolderPositionBean.Result>() {
            @Override
            public void onSuccess(HolderPositionBean.Result result) {
                if (getView() != null) {
                    getView().RushHoldList(result);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().holderListErr(msg);
                }
            }
        });
    }

    public void getMatchNotice(String matchId, String page) {
        new MatchListMode().getNotice(matchId, page, new CallBack<NoticeBean.Result>() {
            @Override
            public void onSuccess(NoticeBean.Result o) {
                if (getView() != null) {
                    getView().notice(o);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().noticeErr(msg);
                }
            }
        });
    }

    public void getUserWallet() {
        new WalletMode().getUserMoney(new CallBack<UserWallet>() {
            @Override
            public void onSuccess(UserWallet o) {
                if (getView() != null) {
                    getView().userWallet(o);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().userWalletErr(msg);
                }
            }
        });
    }

    public void resetMatch(String matchID) {
        new AddStockMode().resetMatch(matchID, new CallBack<String>() {
            @Override
            public void onSuccess(String o) {
                if (getView() != null) {
                    getView().resetOnSuccess(o);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().resetOnFail(msg);
                }
            }
        });
    }
}
