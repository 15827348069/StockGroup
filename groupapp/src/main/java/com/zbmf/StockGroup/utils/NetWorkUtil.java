package com.zbmf.StockGroup.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by xuhao on 2017/7/26.
 */

public class NetWorkUtil {
    enum NetworkStatus {
        WIFI,
        MOBILEWEB,
        NETLESS,
    }
    public static final boolean isWifi(Context context){
        return getNetWork(context)!=null&&getNetWork(context).getType() == ConnectivityManager.TYPE_WIFI;
    }
    public static final boolean isNetWork(Context context){
        return getNetWork(context)!=null&&getNetWork(context).isAvailable();
    }
    public static final NetworkInfo getNetWork(Context context){
        ConnectivityManager  cm = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
       return cm.getActiveNetworkInfo();
    }
}
