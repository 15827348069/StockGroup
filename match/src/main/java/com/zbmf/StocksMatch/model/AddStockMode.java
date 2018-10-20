package com.zbmf.StocksMatch.model;

import android.annotation.SuppressLint;

import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.BaseBean;
import com.zbmf.StocksMatch.bean.DealsList;
import com.zbmf.StocksMatch.bean.MatchInfo;
import com.zbmf.StocksMatch.bean.StockHoldList;
import com.zbmf.StocksMatch.bean.StockholdsBean;
import com.zbmf.StocksMatch.model.imode.IAddStockMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.pullrefreshrecycle.RefreshStatus;
import com.zbmf.worklibrary.util.GsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pq
 * on 2018/3/29.
 */

public class AddStockMode extends BaseStockMode implements IAddStockMode {

    //最新交易
    public void getRecentDeals(String matchId, final RefreshStatus status, int page,/* int perPage,*/ final CallBack callBack) {
        postSubscrube(Method.RECENT_DEALS/*DEALSYS*/, SendParam.dealSys(page, matchId), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                DealsList dealsList = GsonUtil.parseData(o, DealsList.class);
                if (dealsList.getStatus()) {
                    DealsList.Result result = dealsList.getResult();
                    callBack.onSuccess(result);
                } else {
                    callBack.onFail(dealsList.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    public void getMatchDetail(final String matchId, String userID, final CallBack callBack) {
        postSubscrube(Method.MATCH_JOIN/*GETPLAYER*/, SendParam.getMatchDetail(matchId, userID), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                MatchInfo matchInfo = GsonUtil.parseData(o, MatchInfo.class);
                assert matchInfo != null;
                if (matchInfo.getStatus()) {
                    callBack.onSuccess(matchInfo);
                } else {
                    callBack.onFail(matchInfo.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    private List<StockholdsBean> stockholdsBeans;

    public void getMyStock(int page, final RefreshStatus status, final CallBack callBack) {
        if (stockholdsBeans == null) {
            stockholdsBeans = new ArrayList<>();
        }
        postSubscrube(Method.FOCUS_STOCK_LIST/*GET_FOCUS_LIST*/, SendParam.getFocusList(page), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                BaseBean baseBean = GsonUtil.parseData(o, BaseBean.class);
                if (baseBean.getStatus()) {
                    StockHoldList stockHoldList = GsonUtil.parseData(baseBean.getResult(), StockHoldList.class);
                    switch (status) {
                        case LOAD_DEFAULT:
                            stockholdsBeans.clear();
                            stockholdsBeans.addAll(stockHoldList.getStocks());
                            break;
                        case LOAD_MORE:
                            stockholdsBeans.addAll(stockHoldList.getStocks());
                            break;
                    }
                    stockHoldList.setStocks(stockholdsBeans);
                    callBack.onSuccess(stockHoldList);
                } else {
                    callBack.onFail(baseBean.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    //新增自选股
    @Override
    public void addStockMode(String symbol, String remark, final CallBack callBack) {
        postSubscrube(Method.ADD_STOCK, SendParam.addNewStock(symbol, remark), new CallBack() {
            @SuppressLint("ShowToast")
            @Override
            public void onSuccess(Object o) {
                BaseBean baseBean = GsonUtil.parseData(o, BaseBean.class);
                if (baseBean.getStatus()) {
                    callBack.onSuccess("ok");
                } else {
                    if (baseBean.getErr().getMsg().equals("Object exists")) {
                        callBack.onFail("不支持重复添加!");
                    } else {
                        callBack.onFail(baseBean.getErr().getMsg());
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    public void resetMatch(String matchID, final CallBack callBack) {
        postSubscrube(Method.RESET_MATCH, SendParam.resetMatch(matchID), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                BaseBean baseBean = GsonUtil.parseData(o, BaseBean.class);
                if (baseBean.getStatus()) {
                    callBack.onSuccess("重置成功!");
                } else {
                    if ((int) (baseBean.getErr().getCode()) == 2110) {
                        callBack.onFail("支付失败，魔方宝不足!");
                    } else if ((int) (baseBean.getErr().getCode()) == 1105) {
                        callBack.onFail("比赛未开始或已结束!");
                    } else if ((int) (baseBean.getErr().getCode()) == 1104) {
                        callBack.onFail("用户未参赛!");
                    } else if ((int) (baseBean.getErr().getCode()) == 1013) {
                        callBack.onFail("比赛不存在!");
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg
                );
            }
        });
    }
}
