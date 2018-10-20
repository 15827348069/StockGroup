package com.zbmf.StocksMatch.model.imode;

import com.zbmf.worklibrary.model.CallBack;

/**
 * Created by xuhao
 * on 2017/12/1.
 */

public interface IMatchRankMode {
//    void getMatchRankList(RefreshStatus status, String matchid, /*String order, */int page, CallBack callBack);
    void getMatchYieldList(int page,int perPage,String matchID,String order,CallBack callBack);//比赛排行榜
//    void getTraderRanks(CallBack callBack);//操盘高手排行榜
}
