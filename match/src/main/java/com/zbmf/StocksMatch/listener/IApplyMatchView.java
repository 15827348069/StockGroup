package com.zbmf.StocksMatch.listener;

import com.zbmf.StocksMatch.bean.BaseBean;
import com.zbmf.worklibrary.baseview.BaseView;

/**
 * Created by xuhao
 * on 2017/12/12.
 */

public interface IApplyMatchView extends BaseView {
    void onSucceed();
    void onFail(String msg);
    void successCode(BaseBean baseBean);
    void sendCodeErr(String msg);
}
