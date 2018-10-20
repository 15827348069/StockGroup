package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.City;
import com.zbmf.StocksMatch.bean.MatchList3Bean;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.model.imode.IMatchFragmentMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.GsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取城市列表
 * CITY：城市
 * SCHOOL:学校
 * BUSINESS：券商
 * Created by xuhao on 2017/11/28.
 */

public class MatchCityListMode extends BaseStockMode implements IMatchFragmentMode {
    private List<City> cityList;

    @Override
    public void getMatchLsit(int matchOrg, int page/*final RefreshStatus status*/, final CallBack callBack) {
        postSubscrube(Method.MATCH_LIST_3, SendParam.getMatchList3(matchOrg, page), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                MatchList3Bean matchList3Bean = GsonUtil.parseData(o, MatchList3Bean.class);
                assert matchList3Bean != null;
                if (matchList3Bean.getStatus()) {
                    if (matchList3Bean.getResult()!=null){
                        callBack.onSuccess(matchList3Bean.getResult());
                    }
                } else {
                    callBack.onFail(matchList3Bean.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });

//        if(cityList==null){
//            cityList=new ArrayList<>();
//        }
//        switch (status){
//            case LOAD_DEFAULT:
//                cityList.clear();
//                cityList.addAll(getMatches(flag,page));
//                break;
//            case LOAD_MORE:
//                cityList.addAll(getMatches(flag,page));
//                break;
//            case PULL_TO_REFRESH:
//                cityList.clear();
//                cityList.addAll(getMatches(flag,page));
//                break;
//        }
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                callBack.onSuccess(cityList);
//            }
//        },500);

    }

    private List<City> getMatches(int flag, int page) {
        List<City> infolist = new ArrayList<>();
        String name = "";
        switch (flag) {
            case Constans.CITY:
                name = "上海";
                break;
            case Constans.SCHOOL:
                name = "洛阳师范学院";
                break;
            case Constans.BUSINESS:
                name = "方正证券";
                break;
        }

        for (int i = cityList.size(); i < 20 * page; i++) {
            infolist.add(new City(name,
                    i,
                    i % 2 == 0 ? 1.35 : -2.06,
                    "http://img2.imgtn.bdimg.com/it/u=2450994032,3525797548&fm=214&gp=0.jpg",
                    "http://www.qqzhi.com/uploadpic/2015-01-22/022222987.jpg",
                    "http://www.qqzhi.com/uploadpic/2015-02-02/211841154.jpg"));
        }
        return infolist;
    }
}
