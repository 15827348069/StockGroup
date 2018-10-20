package com.zbmf.worklibrary.presenter;

import com.zbmf.worklibrary.baseview.BaseView;

/**
 * Created by xuhao
 * on 2017/11/23.
 */

public interface IBasePresenter<D extends BaseView> {
    void onStart(D view);
    void onDestroy();
}
