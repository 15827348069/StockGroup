package com.zbmf.StocksMatch;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.zbmf.StocksMatch.common.AppConfig;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.worklibrary.util.Logx;
import com.zbmf.worklibrary.util.SharedpreferencesUtil;

/**
 * Created by xuhao
 * on 2017/11/22.
 */

public class MatchApplication extends Application {
    private static MatchApplication instance=null;
    private static Context mContext;
    public static MatchApplication getInstance(){
        if (instance==null){
            synchronized (MatchApplication.class){
                if (instance==null){
                    instance=new MatchApplication();
                }
            }
        }
        return instance;
    }
    public static Context getContext(){
        return mContext;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        Logx.init(AppConfig.IS_DEBUG);
        SharedpreferencesUtil.getInstance().initSharedUtil(this);
        WbSdk.install(this,new AuthInfo(this, Constans.WBSDKAppKey,
                Constans.REDIRECT_URL, Constans.SCOPE));
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
