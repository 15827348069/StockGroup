package com.zbmf.StocksMatch.presenter;

import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.DealsList;
import com.zbmf.StocksMatch.bean.MatchInfo;
import com.zbmf.StocksMatch.bean.MatchNewAllBean;
import com.zbmf.StocksMatch.listener.IMatchNeaDealView;
import com.zbmf.StocksMatch.model.AddStockMode;
import com.zbmf.StocksMatch.model.JoinMatchMode;
import com.zbmf.StocksMatch.model.MatchNewDiealMode;
import com.zbmf.StocksMatch.util.MatchSharedUtil;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;
import com.zbmf.worklibrary.pullrefreshrecycle.RefreshStatus;

/**
 * Created by xuhao
 * on 2017/12/1.
 */

public class MatchNewDealPresenter extends BasePresenter<MatchNewDiealMode, IMatchNeaDealView> {
    private int page;
    private String matchID;
    private RefreshStatus status;

    public MatchNewDealPresenter(RefreshStatus status, String matchID) {
        this.status=status;
        this.matchID = matchID;
        /*this.page = page;*/
    }

    public void setstatus(RefreshStatus status){
        this.status=status;
    }

    @Override
    public void getDatas() {
        if (isFirst()) {//首次加载
            setFirst(false);
            page= ParamsKey.D_PAGE;
            getUserMatch(String.valueOf(page), MatchSharedUtil.UserId());
            data();
        }else {
            data();
        }
    }

    private void data(){
        /*if (page!=-1&&perPage!=-1&&!TextUtils.isEmpty(matchID)){
            getDealSys(matchID,status,page);
        }*/
        if (page!=-1&&status!=null){
            getDealSys(status,page,matchID);
        }
    }

    @Override
    public MatchNewDiealMode initMode() {
        return new MatchNewDiealMode();
    }

    public void loadMore(int page) {
        getDealSys(status,page,matchID);
    }

  private void getDealSys(RefreshStatus status,int page,String matchID){
          new AddStockMode().getRecentDeals(matchID,status, page,new CallBack<DealsList.Result>() {
              @Override
              public void onSuccess(DealsList.Result dealSysList) {
                  if (getView()!=null){
                      getView().dissLoading();
                      getView().RushDealList(dealSysList);
                  }
              }

              @Override
              public void onFail(String msg) {
                  getView().dissLoading();
                  getView().showToastMsg(msg);
              }
          });
      }

    public void getMatchInfo(final String matchID,String userId){
        new AddStockMode().getMatchDetail(matchID, userId,new CallBack<MatchInfo>() {
            @Override
            public void onSuccess(MatchInfo matchInfo) {
                if (getView()!=null){
                    getView().dissLoading();
                    getView().rushMatchBean(matchInfo);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView()!=null){
                    getView().dissLoading();
                    getView().RushMatchListErr(msg);
                }
            }
        });
    }

    public void getUserMatch(String page,String userId) {
        new JoinMatchMode().getUserMatch(page,userId, new CallBack<MatchNewAllBean.Result>() {
            @Override
            public void onSuccess(MatchNewAllBean.Result o) {
                if (getView() != null) {
                    getView().RushMatchList(o);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getView()!=null){
                    getView().RushMatchListErr(msg);
                }
            }
        });
    }
}
