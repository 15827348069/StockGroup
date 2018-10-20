package com.zbmf.worklibrary.rxjava;




import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.Logx;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by xuhao
 * on 2017/6/12.
 */

public class BaseObserver implements Observer<Object> {
    private Disposable mDisposable;
    private CallBack callBack;
    private boolean isDestory;
    public BaseObserver(CallBack callBack){
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
    public void onNext(Object value) {
        if(!isDestory){
            callBack.onSuccess(value);
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
