package com.zbmf.groupro.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by xuhao on 2016/12/16.
 */

public class ServiceUtil {
    /**
     * 判断服务是否正在运行
     *
     * @param context
     * @param className 判断的服务名字：包名+类名
     * @return true在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        //获取所有的服务
        List<ActivityManager.RunningServiceInfo> services= activityManager.getRunningServices(Integer.MAX_VALUE);
        if(services!=null&&services.size()>0){
            for(ActivityManager.RunningServiceInfo service : services){
                if(className.equals(service.service.getClassName())){
                    isRunning=true;
                    break;
                }
            }
        }
        return isRunning;
    }
}
