package com.zbmf.StocksMatch.presenter;


import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.MatchList3Bean;
import com.zbmf.StocksMatch.listener.IMatchCityFragment;
import com.zbmf.StocksMatch.model.MatchCityListMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;

/**
 * 获取比赛城市列表
 * Created by xuhao on 2017/11/29.
 */

public class MatchCityPresenter extends BasePresenter<MatchCityListMode, IMatchCityFragment> {
//    private int mPage;
//    private int mPerPage;
//    private int mMatchOrg;
    private int mFlag;
//
//    public void setMatchParams(int matchOrg, int page, int perPage) {
//        this.mMatchOrg = matchOrg;
//        this.mPage = page;
//        this.mPerPage = perPage;
//    }

    public void setFlag(int flag){
        this.mFlag=flag;
    }

    @Override
    public void getDatas() {
        //默认加载城市的数据
        if (mFlag!=-1){
            if (mFlag==ParamsKey.MATCH_ORG_CITY){
                getCityList(ParamsKey.MATCH_ORG_CITY, ParamsKey.D_PAGE);
            }else if (mFlag==ParamsKey.MATCH_ORG_SCHOOL){
                getCityList(ParamsKey.MATCH_ORG_SCHOOL, ParamsKey.D_PAGE);
            }else if (mFlag==ParamsKey.MATCH_ORG_BUSINESS){
                getCityList(ParamsKey.MATCH_ORG_BUSINESS, ParamsKey.D_PAGE);
            }
        }
    }

    @Override
    public MatchCityListMode initMode() {
        return new MatchCityListMode();
    }

    public void getCityList(int matchOrg, int page) {
        getMode().getMatchLsit(matchOrg, page, new CallBack<MatchList3Bean.Result>() {

            @Override
            public void onSuccess(MatchList3Bean.Result result) {
                if (getView() != null) {
                    getView().RushCityList(result);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView()!=null){
                    getView().RushCityErr(msg);
                }
            }
        });
    }
}
