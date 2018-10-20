package com.zbmf.StocksMatch.adapter.interfaces;

import com.zbmf.StocksMatch.bean.DealSys;

/**
 * Created by xuhao
 * on 2017/12/1.
 */

public interface OnAdapterClickListener{
    void onClickBuyListener(DealSys dealSys);
    void onClickClBuyListener(DealSys dealSys);
    void onClickCommentListener(DealSys dealSys);
}
