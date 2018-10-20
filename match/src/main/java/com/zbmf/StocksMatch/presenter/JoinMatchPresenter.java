package com.zbmf.StocksMatch.presenter;

import android.text.TextUtils;

import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.HolderPositionBean;
import com.zbmf.StocksMatch.bean.JoinMatchBean;
import com.zbmf.StocksMatch.bean.MatchBottomAdsBean;
import com.zbmf.StocksMatch.bean.MatchDescBean;
import com.zbmf.StocksMatch.bean.MatchInfo;
import com.zbmf.StocksMatch.bean.NoticeBean;
import com.zbmf.StocksMatch.bean.PopWindowBean;
import com.zbmf.StocksMatch.bean.UserWallet;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.listener.IJoinMatchView;
import com.zbmf.StocksMatch.model.AddStockMode;
import com.zbmf.StocksMatch.model.JoinMatchMode;
import com.zbmf.StocksMatch.model.MatchDescMode;
import com.zbmf.StocksMatch.model.MatchListMode;
import com.zbmf.StocksMatch.model.PopWindowMode;
import com.zbmf.StocksMatch.model.WalletMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;
import com.zbmf.worklibrary.util.Logx;

/**
 * Created by pq
 * on 2018/3/19.
 */

public class JoinMatchPresenter extends BasePresenter<JoinMatchMode, IJoinMatchView> {
    private int matchID;
    private String userID;

    public JoinMatchPresenter(int matchID, String userID) {
        this.matchID = matchID;
        this.userID = userID;
    }

    @Override
    public void getDatas() {
        if (matchID != -1 && !TextUtils.isEmpty(userID)) {
            matchDesc(matchID);//获取比赛详情
            joinMatch(matchID, userID);
            getPopWindow(matchID);//弹窗
            holderPosition(matchID,ParamsKey.D_PAGE, userID);
            getMatchInfo(String.valueOf(matchID), userID);//matchInfo
            getMatchNotice(String.valueOf(matchID), String.valueOf(ParamsKey.D_PAGE));
            getMatchBottomAds(String.valueOf(matchID));
        }
    }

    @Override
    public JoinMatchMode initMode() {
        return new JoinMatchMode();
    }

    //获取比赛详情
    public void matchDesc(int match_id) {
        new MatchDescMode().matchDesc(match_id, new CallBack<MatchDescBean.Result>() {
            @Override
            public void onSuccess(MatchDescBean.Result result) {
                if (result != null && getView() != null) {
                    getView().refreshMatchDesc(result, Constans.GAIN_DATA_SUCCESS);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().refreshMatchDesc(null, Constans.GAIN_DATA_FAIL);
                }
            }
        });
    }

    //过去参赛的信息
    public void joinMatch(int matchID, String userID) {
        getMode().joinMatch(matchID, userID, new CallBack<JoinMatchBean.Result>() {

            @Override
            public void onSuccess(JoinMatchBean.Result result) {
                if (result != null) {
                    if (getView() != null) {
                        getView().refreshMatchJoin(result);
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().refreshMatchJoinErr(msg);
                }
            }
        });
    }

    //持仓数据
    public void holderPosition(int matchID,int page, String userID) {
        new JoinMatchMode().holderPosition(matchID,String.valueOf(page),userID, new CallBack<HolderPositionBean.Result>() {
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

//        new MatchDetailMode().getMatchHold(String.valueOf(matchID), userID, new CallBack<StockHoldList>() {
//            @Override
//            public void onSuccess(StockHoldList stockHoldList) {
//                if (getView() != null) {
//                    getView().rushHold(stockHoldList);
//                }
//            }
//
//            @Override
//            public void onFail(String msg) {
//                if (getView() != null) {
//                    getView().rushHolderErr(msg);
//                }
//            }
//        });
//        getMode().holderPosition(matchID,page, new CallBack<HolderPositionBean.Result>() {
//            @Override
//            public void onSuccess(HolderPositionBean.Result result) {
//                if (result!=null){
//                    getView().refreshMatchHolder(result);
//                }
//            }
//
//            @Override
//            public void onFail(String msg) {
//                Logx.e(msg);
//            }
//        });
    }

    public void getMatchInfo(String matchID, String userId) {
        new AddStockMode().getMatchDetail(matchID, userId, new CallBack<MatchInfo>() {
            @Override
            public void onSuccess(MatchInfo matchInfo) {
                if (getView() != null) {
                    getView().matchInfo(matchInfo);
                }
            }

            @Override
            public void onFail(String msg) {
                Logx.e(msg);
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

    //获取弹窗
    private void getPopWindow(final int match_id) {
        new PopWindowMode().getPopWindow(String.valueOf(match_id), new CallBack<PopWindowBean>() {
            @Override
            public void onSuccess(PopWindowBean popWindowBean) {
                if (getView() != null) {
                    getView().popWindow(popWindowBean, Constans.GAIN_DATA_SUCCESS, match_id);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().popWindow(null, Constans.GAIN_DATA_FAIL, match_id);
                }
            }
        });
    }

    //获取底部广告位
    private void getMatchBottomAds(String matchId) {
        new PopWindowMode().getMatchBottomAds(matchId, new CallBack<MatchBottomAdsBean>() {

            @Override
            public void onSuccess(MatchBottomAdsBean adverts) {
                if (adverts != null && getView() != null) {
                    getView().bottomAds(adverts);
                }
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }
}
