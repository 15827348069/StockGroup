package com.zbmf.StocksMatch.listener;

import com.zbmf.StocksMatch.bean.StockholdsBean;
import com.zbmf.worklibrary.baseview.BaseView;

import java.util.List;

/**
 * Created by xuhao
 * on 2017/12/19.
 */

public interface IStockFragmentView extends BaseView{
    void onRushStockList(List<StockholdsBean>stockholdsBeans,int total,int page);
    void onFail(String msg);
    void deleteStockStatus(String msg);
}
