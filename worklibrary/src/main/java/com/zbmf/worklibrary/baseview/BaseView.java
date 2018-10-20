package com.zbmf.worklibrary.baseview;

/**
 * Created by xiaote
 * on 2016/10/26.
 */

public interface BaseView {
    void loadMore();

    void dissLoading();

    void showToastMsg(String msg);

    void onRefreshComplete();
}
