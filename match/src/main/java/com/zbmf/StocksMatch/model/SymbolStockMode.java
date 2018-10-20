package com.zbmf.StocksMatch.model;

import com.google.gson.Gson;
import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.Adverts;
import com.zbmf.StocksMatch.bean.BaseBean;
import com.zbmf.StocksMatch.bean.City;
import com.zbmf.StocksMatch.bean.HomeMatchList;
import com.zbmf.StocksMatch.bean.MatchList;
import com.zbmf.StocksMatch.bean.PhoneAdList;
import com.zbmf.StocksMatch.bean.SchoolBean;
import com.zbmf.StocksMatch.bean.TraderList;
import com.zbmf.StocksMatch.model.imode.IStockMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.GsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by pq
 * on 2018/3/15.
 */

public class SymbolStockMode extends BaseStockMode implements IStockMode {
    @Override
    public void getStockIndex(final CallBack callBack) {//证券指数
        postSubscrube(Method.HOME_STOCK_INDEX, SendParam.getStockIndex(), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                if (o != null) {
                    Gson gson = new Gson();
                    String toJson = gson.toJson(o);
                    //解析
                    JSONObject jsonObject = null;
                    Map<String, String> map = new HashMap<>();
                    try {
                        //将json数据当作不确定数据来解析
                        jsonObject = new JSONObject(toJson);
                        Iterator<String> keys = jsonObject.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            String value = jsonObject.getString(key);
                            map.put(key, value);
                        }
                        if (map.get("status").equals("ok")) {
                            callBack.onSuccess(map);
                        } else {
                            callBack.onFail("---- 证券指数获取失败 ");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    @Override
    public void getSupremeMatch(final CallBack callBack) {//获取首页推荐比赛
        postSubscrube(Method.HOME_MATCH_RECOMMEND, SendParam.getSupremeMatch(), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                HomeMatchList homeMatchList = GsonUtil.parseData(o, HomeMatchList.class);
                assert homeMatchList != null;
                if (homeMatchList.getStatus()) {
                    callBack.onSuccess(homeMatchList);
                } else {
                    callBack.onFail(String.valueOf(homeMatchList.getErr().getCode()));
                }

            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
        postSubscrube(Method.SUPREME_MATCH, SendParam.getSupremeMatch(), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                MatchList matchList = GsonUtil.parseData(o, MatchList.class);
                assert matchList != null;
                if (matchList.getStatus()) {
                    callBack.onSuccess(matchList.getMatch());
                } else {
                    callBack.onFail(matchList.getErr().getMsg());
                }

            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    @Override
    public void getImageList(String category, final CallBack callBack) {//获取top轮播图片
        new LaunchMode().postSubscrube(Method.HOME_TOP_AD, SendParam.getReconpic(category), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                PhoneAdList phoneAdList = GsonUtil.parseData(o, PhoneAdList.class);
                if (phoneAdList != null) {
                    if (phoneAdList.getStatus()) {
                        List<Adverts> adverts = phoneAdList.getAdverts();
                        callBack.onSuccess(adverts);
                    } else {
                        callBack.onFail(phoneAdList.getErr().getMsg());
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    //获取参赛人数和举办比赛场次
    @Override
    public void getMatchSchool(final CallBack callBack) {//获取参赛的学校
        postSubscrube(Method.HOME_MATCH_SCHOOL, SendParam.getSchool(), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                SchoolBean schoolBean = GsonUtil.parseData(o, SchoolBean.class);
                if (schoolBean.getStatus()) {
                    callBack.onSuccess(schoolBean);
                } else {
                    callBack.onFail(schoolBean.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    @Override
    public void getTrader(final CallBack callBack) {//获取操盘高手
        new HomeMode().postSubscrube(Method.TRADER_RANKS, SendParam.getTraders(), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                TraderList traderList = GsonUtil.parseData(o, TraderList.class);
                if (traderList.getStatus()) {
                    if (traderList.getTraders().size() > 3) {
                        callBack.onSuccess(traderList.getTraders().subList(0, 3));
                    } else {
                        callBack.onSuccess(traderList.getTraders());
                    }
                } else {
                    callBack.onFail(traderList.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    @Override
    public void getCity(CallBack callBack) {//获取城市比赛推荐
        // TODO: 2018/3/16 暂时用假数据,接口暂无数据
        List<City> cities = new ArrayList<>();
        cities.add(new City("浙江","2120"));
        cities.add(new City("北京","2129"));
        cities.add(new City("上海","2127"));
        cities.add(new City("广东","2125"));
        cities.add(new City("江苏","2126"));
        cities.add(new City("山东","2128"));
        callBack.onSuccess(cities);
//      postSubscrube(Method.HOME_MATCH_CITY, SendParam.getMatchCity(), new CallBack() {
//          @Override
//          public void onSuccess(Object o) {
//              Log.i("--TAG","-------------  -- "+o.toString());
//          }
//
//          @Override
//          public void onFail(String msg) {
//
//          }
//      });
    }

    @Override
    public void getHotMatch(CallBack callBack) {//获取热门比赛推荐
    }

    @Override
    public void deleteStockItem(String symbol, final CallBack callBack) {
        postSubscrube(Method.DEL_STOCK, SendParam.delStockItem(symbol), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                BaseBean baseBean = GsonUtil.parseData(o, BaseBean.class);
                if (baseBean.getStatus()) {
                    callBack.onSuccess("删除成功");
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
}
