package com.zbmf.StocksMatch.presenter;

import com.zbmf.StocksMatch.bean.Adverts;
import com.zbmf.StocksMatch.bean.City;
import com.zbmf.StocksMatch.bean.HomeMatchList;
import com.zbmf.StocksMatch.bean.MatchDescBean;
import com.zbmf.StocksMatch.bean.PopWindowBean;
import com.zbmf.StocksMatch.bean.SchoolBean;
import com.zbmf.StocksMatch.bean.Traders;
import com.zbmf.StocksMatch.bean.UserWallet;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.listener.IHomeView;
import com.zbmf.StocksMatch.model.MatchDescMode;
import com.zbmf.StocksMatch.model.PopWindowMode;
import com.zbmf.StocksMatch.model.SymbolStockMode;
import com.zbmf.StocksMatch.model.WalletMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by xuhao
 * on 2017/11/24.
 */

public class HomePresenter extends BasePresenter<SymbolStockMode, IHomeView> {
    private static HomePresenter sHomePresenter;
    private boolean supreMatch, bannerImg, matchSchool;

    public static HomePresenter getInstance() {
        if (sHomePresenter == null) {
            synchronized (HomePresenter.class) {
                if (sHomePresenter == null) {
                    sHomePresenter = new HomePresenter();
                    return sHomePresenter;
                }
            }
        }
        return sHomePresenter;
    }

    @Override
    public void getDatas() {
        if (isFirst()) {
            setFirst(false);
        }
        getStockIndex();//获取指数
        getMatchSchool();//获取参赛高校
        getPopWindow();//弹窗--此处matchID可以不用传
        getImageList(Constans.HOME_TOP_BANNER);//获取推荐比赛图片
        getSponsorAdImg(Constans.HOME_SPONSOR_ADS);//获取首页底部赞助商广告
        getSupremeMatch();//获取推荐比赛---和热门比赛格式一样，将后三个作为热门比赛
        getMatchTrader();//获取操盘高手
//        getHotMatch();//获取热门比赛推荐
        getCityList();//获取城市比赛推荐
    }

    @Override
    public SymbolStockMode initMode() {
        return new SymbolStockMode();
    }

    public void getStockIndex() {
        if (getMode() != null) {
            getMode().getStockIndex(new CallBack<Map<String, String>>() {

                @Override
                public void onSuccess(Map<String, String> map) {
                    if (map != null) {
                        JSONObject jsonObject = null;
                        Map<String, String> valueMap = new HashMap<>();
                        try {
                            jsonObject = new JSONObject(map.get("result"));
                            Iterator<String> keys1 = jsonObject.keys();
                            while (keys1.hasNext()) {
                                String next = keys1.next();
                                String value0 = jsonObject.getString(next);
                                valueMap.put(next, value0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (getView() != null) {
                            getView().refreshStockIndex(valueMap);
                        }
                    }
                }

                @Override
                public void onFail(String msg) {
                    if (getView() != null) {
                        getView().onErr(msg);
                    }
                }
            });
        }
    }

    private void getSupremeMatch() {
        getMode().getSupremeMatch(new CallBack<HomeMatchList>() {
            @Override
            public void onSuccess(HomeMatchList homeMatch) {
                if (homeMatch != null) {
                    List<HomeMatchList.Result.Hot> hot = homeMatch.getResult().getHot();
                    List<HomeMatchList.Result.Recommend> recommend = homeMatch.getResult().getRecommend();
                    if (getView() != null) {
                        getView().RusnSupremeMatchAdapter(recommend);
                        getView().RushHostMatch(hot);
                        supreMatch = true;
                        Rush();
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                supreMatch = true;
                Rush();
                if (getView() != null) {
                    getView().onErr(msg);
                }
            }
        });
    }

    //获取弹窗
    private void getPopWindow() {
        new PopWindowMode().getPopWindow("", new CallBack<PopWindowBean>() {
            @Override
            public void onSuccess(PopWindowBean popWindowBean) {
                if (getView() != null) {
                    getView().popWindow(popWindowBean,Constans.GAIN_DATA_SUCCESS);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().popWindow(null,Constans.GAIN_DATA_FAIL);
                }
            }
        });
    }

    private void getMatchTrader() {
        getMode().getTrader(new CallBack<List<Traders>>() {
            @Override
            public void onSuccess(List<Traders> tradersList) {
                if (getView() != null) {
                    getView().RushTraderList(tradersList);
                }
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    //获取首页底部赞助商广告
    private void getSponsorAdImg(String category) {
        getMode().getImageList(category, new CallBack<List<Adverts>>() {

            @Override
            public void onSuccess(List<Adverts> sponsor) {
                if (getView() != null) {
                    getView().rushSponsorAds(sponsor, Constans.GAIN_DATA_SUCCESS);
                    bannerImg = true;
                    Rush();
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().rushSponsorAds(null, Constans.GAIN_DATA_FAIL);
                    bannerImg = true;
                    Rush();
                }
            }
        });
    }

    //获取首页轮播图广告
    private void getImageList(String category) {
        getMode().getImageList(category, new CallBack<List<Adverts>>() {
            @Override
            public void onSuccess(List<Adverts> imagelist) {
                if (getView() != null) {
                    getView().RushBannerImage(imagelist);
                    bannerImg = true;
                    Rush();
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    bannerImg = true;
                    Rush();
                }
            }
        });
    }

    private void getMatchSchool() {
        if (getMode() != null) {
            getMode().getMatchSchool(new CallBack<SchoolBean>() {
                @Override
                public void onSuccess(SchoolBean schoolBean) {
                    if (getView() != null) {
                        getView().RushMatchSchool(schoolBean);
                        matchSchool = true;
                        Rush();
                    }
                }

                @Override
                public void onFail(String msg) {
                    if (getView() != null) {
                        matchSchool = true;
                        Rush();
                    }
                }
            });
        }
    }

    private void getHotMatch() {
    }

    private void getCityList() {
        getMode().getCity(new CallBack<List<City>>() {
            @Override
            public void onSuccess(List<City> cities) {
                if (getView() != null) {
                    getView().RushCity(cities);
                }
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    private void Rush() {
        if (bannerImg && supreMatch && matchSchool) {
            if (getView() != null) {
                getView().dissLoading();
                getView().RushScrollView();
            }
        }
    }

    public void getMatchDesc(int matchId) {
        new MatchDescMode().matchDesc(matchId, new CallBack<MatchDescBean.Result>() {
            @Override
            public void onSuccess(MatchDescBean.Result result) {
                if (getView() != null) {
                    getView().refreshMatchDesc(result);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().refreshMatchDescErr(msg);
                }
            }
        });
    }

    //获取用户的魔方余额
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
}
