package com.zbmf.StocksMatch.listener;

import com.zbmf.StocksMatch.bean.Adverts;
import com.zbmf.StocksMatch.bean.MatchDescBean;
import com.zbmf.worklibrary.baseview.BaseView;

import java.util.List;

/**
 * Created by xuhao
 * on 2017/11/24.
 */

public interface IlaunchView extends BaseView {
    void toLogin();
    void rushLunchImage(List<Adverts> imgList);
    void rushLunchImageErr(String msg);
    void refreshMatchDesc(MatchDescBean.Result result);
    void refreshMatchDescErr(String msg);
}
