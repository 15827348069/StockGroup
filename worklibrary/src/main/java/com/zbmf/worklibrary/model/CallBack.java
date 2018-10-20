package com.zbmf.worklibrary.model;


/**
 * Created by xuhao
 * on 2017/11/21.
 */

public interface CallBack<T> {
    void onSuccess(T t);
    void onFail(String msg);
}
