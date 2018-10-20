package com.zbmf.StocksMatch.listener;

import com.zbmf.StocksMatch.bean.PayListBean;
import com.zbmf.worklibrary.baseview.BaseView;

import java.util.Map;

/**
 * Created by pq
 * on 2018/4/9.
 */

public interface PayList extends BaseView {
    void payList(PayListBean payList);
    void payListErr(String msg);
    void wxPayResult(Map<String,String> map);
    void wxPayErr(String msg);
}
