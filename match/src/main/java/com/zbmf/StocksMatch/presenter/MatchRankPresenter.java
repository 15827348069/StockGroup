package com.zbmf.StocksMatch.presenter;

import android.text.TextUtils;

import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.MatchRank;
import com.zbmf.StocksMatch.listener.IMatchRankView;
import com.zbmf.StocksMatch.model.MatchRankMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;

/**
 * Created by xuhao
 * on 2017/12/1.
 */

public class MatchRankPresenter extends BasePresenter<MatchRankMode, IMatchRankView> {
    private String match_id, order;
    private int page;
    /*private RefreshStatus status;*/

    public MatchRankPresenter(/*RefreshStatus status,*/String match_id, String order) {
        /*this.status=status;*/
        this.match_id = match_id;
        this.order = order;
    }

     /*public void setStatus(RefreshStatus status){
        this.status=status;
     }*/
     public void setPage(int page){
         this.page=page;
     }

    @Override
    public void getDatas() {
        if (isFirst()) {
            setFirst(false);
            page = ParamsKey.D_PAGE;
            if (!TextUtils.isEmpty(match_id) && !order.equals("-1")){
                getMatchYieldList(page, ParamsKey.D_PERPAGE, match_id, order);
            }
        } else {
            loadMore();
        }
    }

    public void loadMore() {
        getMatchYieldList(page, ParamsKey.D_PERPAGE, match_id, order);
    }

    @Override
    public MatchRankMode initMode() {
        return new MatchRankMode();
    }

    //获取排行榜单
    private void getMatchYieldList(int page, int perPage, String matchID, String order) {
        getMode().getMatchYieldList(page, perPage, matchID, order, new CallBack<MatchRank>() {
            @Override
            public void onSuccess(MatchRank matchRank) {
                if (getView() != null) {
                    getView().RushDealList(matchRank);
                }
            }

            @Override
            public void onFail(String msg) {
               if (getView()!=null){
                   getView().rushErr(msg);
               }
            }
        });
    }
}
