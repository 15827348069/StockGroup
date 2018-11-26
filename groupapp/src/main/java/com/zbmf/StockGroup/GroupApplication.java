package com.zbmf.StockGroup;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.android.thinkive.framework.ThinkiveInitializer;
import com.igexin.sdk.PushManager;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.zbmf.StockGroup.constans.AppConfig;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.service.DemoPushService;
import com.zbmf.StockGroup.utils.DataSet;
import com.zbmf.StockGroup.utils.ImageLoaderUtil;
import com.zbmf.StockGroup.utils.LogUtil;
import com.zbmf.StockGroup.utils.PgyUtil;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;

/**
 * Created by xuhao
 * on 2016/12/12.
 */

public class GroupApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.init();
        SettingDefaultsManager.getInstance().setSharedPreferences(this);
        ImageLoaderUtil.init(this);
        DataSet.init(this);
        WbSdk.install(this,new AuthInfo(this, Constants.WBSDKAppKey,
                Constants.REDIRECT_URL, Constants.SCOPE));
        ThinkiveInitializer.getInstance().initialze(this);//初始化思迪开户配置
        PgyUtil.registerCrash(this, AppConfig.IS_DEBUG);
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        PushManager.getInstance().initialize(this.getApplicationContext(), DemoPushService.class);

    }

}
