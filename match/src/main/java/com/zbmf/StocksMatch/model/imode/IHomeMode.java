package com.zbmf.StocksMatch.model.imode;

import com.zbmf.worklibrary.model.CallBack;

/**
 * Created by xuhao
 * on 2017/11/22.
 */

public interface IHomeMode {
    void getSupremeMatch(CallBack callBack);
    void getImageList(CallBack callBack);
    void getMatchSchool(CallBack callBack);
    void getTrader(CallBack callBack);
    void getCity(CallBack callBack);
    void getHotMatch(CallBack callBack);
}
