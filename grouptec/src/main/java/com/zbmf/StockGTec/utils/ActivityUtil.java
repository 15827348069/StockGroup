package com.zbmf.StockGTec.utils;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

/**
 * lower manager class
 * Created by xuhao on 2017/1/5.
 */

public class ActivityUtil {
    public static ActivityUtil activityUtil;

    public static ActivityUtil getInstance() {
        if (activityUtil == null) {
            activityUtil = new ActivityUtil();
        }
        return activityUtil;
    }

    public static Map<String, Activity> map;

    public static Map<String, Activity> getMapInstance() {
        if (map == null) {
            map = new HashMap<>();
        }
        return map;
    }

    /**
     * 存储Activity对象
     *
     * @param key
     * @param activity
     */
    public void putActivity(String key, Activity activity) {
        getMapInstance().put(key, activity);
    }

    /**
     * 取出Activity
     *
     * @param key
     * @return
     */
    public Activity getActivity(String key) {
        return getMapInstance().get(key);
    }


}
