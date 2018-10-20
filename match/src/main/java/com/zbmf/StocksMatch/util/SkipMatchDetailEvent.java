package com.zbmf.StocksMatch.util;

import com.zbmf.StocksMatch.bean.MatchRank;

/**
 * Created by pq
 * on 2018/4/28.
 */

public class SkipMatchDetailEvent {
    //标记跳转到比赛详情的activity只可以查看
    private String matchName, matchID, myFlag;
    private MatchRank.Result.Yields yields ;

    public SkipMatchDetailEvent(String matchName, String matchID, String myFlag,MatchRank.Result.Yields yields ) {
        this.matchName = matchName;
        this.matchID = matchID;
        this.myFlag = myFlag;
        this.yields=yields;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public String getMatchID() {
        return matchID;
    }

    public void setMatchID(String matchID) {
        this.matchID = matchID;
    }

    public String getMyFlag() {
        return myFlag;
    }

    public void setMyFlag(String myFlag) {
        this.myFlag = myFlag;
    }

    public MatchRank.Result.Yields getLast_deal() {
        return yields;
    }

    public void setLast_deal(MatchRank.Result.Yields yields) {
        this.yields = yields;
    }
}
