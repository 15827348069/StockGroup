package com.zbmf.groupro.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.zbmf.groupro.GroupActivity;
import com.zbmf.groupro.activity.AddFansActivity;
import com.zbmf.groupro.activity.MfbActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuhao on 2017/1/5.
 */

public class ActivityUtil {
    public static ActivityUtil activityUtil;
    public static ActivityUtil getInstance()
    {
        if (activityUtil == null)
        {
            activityUtil = new ActivityUtil();
        }
        return activityUtil;
    }
    public static Map<String ,Activity>map;
    public static Map<String ,Activity> getMapInstance()
    {
        if (map == null)
        {
            map = new HashMap<>();
        }
        return map;
    }
    public static final String GROUPACTIVITY="GroupActivity";
    public static final String CHATACTIVITY="Chat1Activity";
    public static final String FANSACTIVITY="FansActivity";
    public static final String REDPACKGEDACTIVITY="RedPackgedActivity";
    public static final String BIGIMAGEACTIVITY="BigImageActivity";
    public static final String LoginActivity="LoginActivity";
    public static final String AddFansActivity="AddFansActivity";
    public static final String MfbActivitys="MfbActivity";
    public static final String PayDetailActivity="PayDetailActivity";
    /**
     * 存储Activity对象
     * @param key
     * @param activity
     */
    public void putActivity(String key,Activity activity){
        getMapInstance().put(key,activity);
    }

    /**
     * 取出Activity
     * @param key
     * @return
     */
    public Activity getActivity(String key){
        return getMapInstance().get(key);
    }

    /**
     * 移除Activity对象
     * @param key
     */
    public void removeActivity(String key){
        Activity activity=map.get(key);
        if(activity!=null){
            activity.finish();
        }
        map.remove(key);
    }
    /**
     * 获取Activity是否是在运行
     * @param cmdName
     * @param context
     * @return
     */
    public static boolean isTopActivy(String cmdName, Context context)
    {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(Integer.MAX_VALUE);
        String cmpNameTemp = null;
        if (null != runningTaskInfos)
        {
            cmpNameTemp = (runningTaskInfos.get(0).topActivity).toString();
        }

        if (null == cmpNameTemp)
        {
            return false;
        }
        return cmpNameTemp.equals(cmdName);
    }
    public void UpdaUserMessage(){
        GroupActivity groupActivity= (GroupActivity)getActivity(GROUPACTIVITY);
        if(groupActivity!=null){
            groupActivity.getWolle();
        }
    }
    public void UpdaUserMfbMessage(){
        AddFansActivity addFansActivity= (AddFansActivity)getActivity(AddFansActivity);
        if(addFansActivity!=null){
            addFansActivity.setAddFansMessage();
        }
        MfbActivity mfbActivitys= (MfbActivity)getActivity(MfbActivitys);
        if(mfbActivitys!=null){
            mfbActivitys.initData();
        }
    }
    public void clearAll(){
        for (String key : map.keySet()) {
            Activity activity= map.get(key);
            if(activity!=null){
                activity.finish();
            }
        }
        map.clear();
    }
}
