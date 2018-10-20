package com.zbmf.worklibrary.rxjava;


import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.Logx;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by xuhao on 2017/6/12.
 */

public class StockObserver implements Observer<Object> {
    private Disposable mDisposable;
    private CallBack callBack;
    private boolean isDestory;
    public StockObserver(CallBack callBack){
        this.callBack=callBack;
    }
    public void setDestory(boolean isDestory){
        this.isDestory=isDestory;
    }
    @Override
    public void onSubscribe(Disposable d) {
        Logx.e("开始获取数据");
        this.mDisposable = d;
    }

    @Override
    public void onNext(Object requestBody) {
        if(!isDestory){
            callBack.onSuccess(requestBody);
        }
    }

    @Override
    public void onError(Throwable e) {
        Logx.e("获取数据失败"+e.getMessage());
        if(!isDestory){
            callBack.onFail(e.getMessage());
        }

    }

    @Override
    public void onComplete() {
        Logx.e("获取数据完成,销毁线程");
        if(mDisposable!=null){
            mDisposable.dispose();
        }
    }

}
