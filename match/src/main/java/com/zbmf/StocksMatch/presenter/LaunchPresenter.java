package com.zbmf.StocksMatch.presenter;

import com.zbmf.StocksMatch.api.HostUrl;
import com.zbmf.StocksMatch.bean.Address;
import com.zbmf.StocksMatch.bean.Adverts;
import com.zbmf.StocksMatch.bean.MatchDescBean;
import com.zbmf.StocksMatch.bean.Vers;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.common.SharedKey;
import com.zbmf.StocksMatch.listener.IlaunchView;
import com.zbmf.StocksMatch.model.LaunchMode;
import com.zbmf.StocksMatch.model.MatchDescMode;
import com.zbmf.StocksMatch.model.SymbolStockMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;
import com.zbmf.worklibrary.util.SharedpreferencesUtil;

import java.util.List;

/**
 * Created by xuhao
 * on 2017/11/24.
 */

public class LaunchPresenter extends BasePresenter<LaunchMode, IlaunchView> {
    @Override
    public void getDatas() {
        getImageList(Constans.LUNCH_ADS);//获取启动页的广告
        getMode().getVers(new CallBack<Vers>() {
            @Override
            public void onSuccess(Vers v) {
                //获取当前APP版本
                Address address = v.getAddress();
                Address.group group = address.getGroup();
                HostUrl.GROUP_URL = group.getHost() + group.getApi();
                Address.match match = address.getMatch();
                HostUrl.MATCH_URL = match.getHost() + match.getApi();
                Address.www www = address.getWww();
                HostUrl.WWW_URL = www.getHost() + www.getApi();
                Address.passport passport = address.getPassport();
                HostUrl.PASS_URL = passport.getHost() + passport.getApi();
                Address.stock stock = address.getStock();
                HostUrl.STOCK_URL = stock.getHost() + stock.getApi();
                int kchart = v.getKchart();//是否显示K线图(0:不显示，1：显示)
                SharedpreferencesUtil.getInstance().putString(SharedKey.STOCK_HOST, HostUrl.STOCK_URL);
                SharedpreferencesUtil.getInstance().putString(SharedKey.GROUP_HOST, HostUrl.GROUP_URL);
                SharedpreferencesUtil.getInstance().putString(SharedKey.MATCH_HOST, HostUrl.MATCH_URL);
                SharedpreferencesUtil.getInstance().putString(SharedKey.WWW_HOST, HostUrl.WWW_URL);
                SharedpreferencesUtil.getInstance().putString(SharedKey.PASS_HOST, HostUrl.PASS_URL);
                SharedpreferencesUtil.getInstance().putString(SharedKey.GUPIAO_HOST, HostUrl.GUPIAO_URLS);
                SharedpreferencesUtil.getInstance().putInt(SharedKey.IS_SHOW_KLINE_CHART, kchart);
                if (getView() != null) {
                    getView().toLogin();
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().showToastMsg(msg);
                    getView().toLogin();
                }
            }
        });
    }

    @Override
    public LaunchMode initMode() {
        return new LaunchMode();
    }

    private void getImageList(String category) {
        new SymbolStockMode().getImageList(category, new CallBack<List<Adverts>>() {
            @Override
            public void onSuccess(List<Adverts> ads) {
                if (getView() != null) {
                    getView().rushLunchImage(ads);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().rushLunchImageErr(msg);
                }
            }
        });
    }

    public void getMatchDesc() {
        new MatchDescMode().matchDesc(Integer.parseInt(Constans.MATCH_ID), new CallBack<MatchDescBean.Result>() {
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
}
