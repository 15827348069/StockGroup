package com.zbmf.StocksMatch.presenter;

import android.text.TextUtils;

import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.DealsRecordList;
import com.zbmf.StocksMatch.bean.TraderDeals;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.model.DealMode;
import com.zbmf.StocksMatch.model.QueryMode;
import com.zbmf.StocksMatch.model.imode.DealModeView;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;
import com.zbmf.worklibrary.util.Logx;

/**
 * Created by pq
 * on 2018/3/26.
 */

public class DealRecordPresenter extends BasePresenter<DealMode,DealModeView> {
    private String userID;
    private String flag;
    private String id;
    private String matchID;

    public DealRecordPresenter(String matchID,String userID,String flag,String id) {
        this.matchID=matchID;
        this.userID = userID;
        this.flag=flag;
        this.id=id;
    }

    @Override
    public void getDatas() {
        if (!TextUtils.isEmpty(userID)){
//            if (flag.equals(IntentKey.NOR_FLAG)){
//                if (!TextUtils.isEmpty(id)){
//                    getNormalDealRecord(matchID,String.valueOf(ParamsKey.D_PERPAGE),id);
//                }
//            }else
                if (flag.equals(IntentKey.TRADER_FLAG)){
                traderDealRecord(userID, ParamsKey.D_PAGE,ParamsKey.D_PERPAGE);
            }
        }
    }

    @Override
    public DealMode initMode() {
        return new DealMode();
    }

    //操盘高手交易记录
    public void traderDealRecord(String userID, int page, int perPage) {
        getMode().getTraderDealRecord(userID, page, perPage, new CallBack<TraderDeals.Result>() {
            @Override
            public void onSuccess(TraderDeals.Result result) {
                if (getView()!=null){
                    getView().onRefreshDealRecord(result);
                }
            }

            @Override
            public void onFail(String msg) {
                Logx.e(msg);
            }
        });
    }

    //普通用户的交易记录
    public void getNormalDealRecord(String matchID, String page, String id){
        new QueryMode().recordList(matchID,page,id,new CallBack<DealsRecordList.Result>() {
            @Override
            public void onSuccess(DealsRecordList.Result o) {
                if (getView() != null) {
                    getView().queryData(o);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView() != null) {
                    getView().queryErr(msg);
                }
            }
        });
    }
}
