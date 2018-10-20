package com.zbmf.StocksMatch.callback;

import android.os.Handler;

/**
 * Created by xuhao
 * on 2017/7/25.
 */
public abstract class ResultCallback<T> {
    private  Handler mHandler=new Handler();
    public ResultCallback() {
    }

    public abstract void onSuccess(T str);

    public abstract void onError(String message);

    public void onFail(final String errMessage) {
        mHandler.post(new Runnable() {
            public void run() {
               onError(errMessage);
            }
        });
    }

   public void onCallback(final T t) {
        mHandler.post(new Runnable() {
            public void run() {
                onSuccess(t);
            }
        });
    }
}