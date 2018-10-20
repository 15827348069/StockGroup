package com.zbmf.StocksMatch.model.imode;



import com.zbmf.worklibrary.model.CallBack;

/**
 * Created by xuhao on 2017/12/11.
 */

public interface IDrillMode {
    void getMatchInfo(String matchid, CallBack callBack);
    void getHolders(String matchid, CallBack callBack);
}
