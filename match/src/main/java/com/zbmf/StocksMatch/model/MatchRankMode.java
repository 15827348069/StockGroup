package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.MatchRank;
import com.zbmf.StocksMatch.bean.Yield;
import com.zbmf.StocksMatch.model.imode.IMatchRankMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.GsonUtil;

import java.util.List;

/**
 * Created by xuhao
 * on 2017/12/1.
 */

public class MatchRankMode extends BaseStockMode implements IMatchRankMode {
    private List<Yield> yieldLists;

    @Override
    public void getMatchYieldList(int page, int perPage, String matchID, final String order, final CallBack callBack) {
        postSubscrube(Method.MATCH_RANK, SendParam.getMatchRank(matchID, String.valueOf(page),
                String.valueOf(perPage), order), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                MatchRank matchRank = GsonUtil.parseData(o, MatchRank.class);
                assert matchRank != null;
                if (matchRank.getStatus()) {
                 callBack.onSuccess(matchRank);
                }else {
                    callBack.onFail(matchRank.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }

/*    @Override
    public void getMatchRankList(final RefreshStatus status, String matchid,*//* String order, *//*final int page, final CallBack callBack) {
        if(yieldLists==null){
            yieldLists=new ArrayList<>();
        }
//        postSubscrube(Method.GET_MATCH_YIELD_LIST, SendParam.getYieldList(matchid, order, page), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                BaseBean baseBean= GsonUtil.parseData(o,BaseBean.class);
                if(baseBean.getStatus()){
                    YieldList yieldList=GsonUtil.parseData(baseBean.getResult(),YieldList.class);
                    switch (status){
                        case LOAD_DEFAULT:
                            yieldLists.clear();
                            yieldLists.addAll(yieldList.getYields());
                            break;
                        case PULL_TO_REFRESH:
                            yieldLists.clear();
                            yieldLists.addAll(yieldList.getYields());
                            break;
                        case LOAD_MORE:
                            yieldLists.addAll(yieldList.getYields());
                            break;
                    }
                    yieldList.setYields(yieldLists);
                    callBack.onSuccess(yieldList);
                }else{
                    callBack.onFail(baseBean.getErr().getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }*/

}
