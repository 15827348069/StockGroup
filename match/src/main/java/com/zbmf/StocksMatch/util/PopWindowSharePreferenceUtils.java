package com.zbmf.StocksMatch.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.zbmf.StocksMatch.common.Constans;

/**
 * Created by pq
 * on 2018/5/15.
 */

public class PopWindowSharePreferenceUtils {
    private static PopWindowSharePreferenceUtils instance;
    private static SharedPreferences sSharedPreferences;
    private static SharedPreferences.Editor sEdit;

    public static PopWindowSharePreferenceUtils getInstance(){
        if (instance==null){
            synchronized (PopWindowSharePreferenceUtils.class){
                if (instance==null){
                    instance=new PopWindowSharePreferenceUtils();
                }
            }
        }
        return instance;
    }

    @SuppressLint("CommitPrefEdits")
    public void initShareUtils(Context context, String saveFileName){
        if (sSharedPreferences==null||sEdit==null){
            sSharedPreferences = context.getSharedPreferences(saveFileName, Context.MODE_PRIVATE);
            sEdit = sSharedPreferences.edit();
        }
    }
    //存储一个初始化的标记时间
    public void putInitTime(String initTime){
        sEdit.putString(Constans.INIT_TIME_STAMP,initTime);
        sEdit.commit();
    }
    //获取初始化的标记时间
    public String getInitTime(String defaultTime){
        return sSharedPreferences.getString(Constans.INIT_TIME_STAMP,defaultTime);
    }
    /**
     * 存储弹窗的信息
     * @param userId   当前用户的ID
     */
    public void putPopWindowMsg(String userId){
        sEdit.putString(Constans.USER_ID,userId);
        sEdit.commit();
    }
    public String getUserId(String defaultUserId){
        return sSharedPreferences.getString(Constans.USER_ID,defaultUserId);
    }
    //存储全站弹窗的信息
    public void putPopTime(String timestamp){
        sEdit.putString(Constans.TIME_STAMP,timestamp);
        sEdit.commit();
    }
    //获取存储的时间戳日期
    public String getPopTime(String defaultTimestamp){
        return sSharedPreferences.getString(Constans.TIME_STAMP,defaultTimestamp);
    }
    /**
     * 存储全站的PopID  弹窗ID
     * @param popId   当前弹窗内容信息的ID
     */
    public void putPopId(String popId){
        sEdit.putString(Constans.POP_ID,popId);
        sEdit.commit();
    }
    public String getPopId(String defaultPopId){
        return sSharedPreferences.getString(Constans.POP_ID,defaultPopId);
    }
    /**
     * 存储单个比赛时的弹窗的MatchID
     * @param matchIdK  matchID 键
     * @param matchIdV  matchId 值
     */
    public void putPopMatchID(String matchIdK,String matchIdV){
        sEdit.putString(matchIdK,matchIdV);
        sEdit.commit();
    }
    //获取相应弹窗的matchID
    public String getPopMatchID(String matchIdK,String defaultMatchIdV){
        return sSharedPreferences.getString(matchIdK,defaultMatchIdV);
    }
    /**
     * 存储单个比赛时弹窗的ID
     * @param popWindowIdK
     * @param popWindowIdV
     */
    public void putPopWindowId(String popWindowIdK,String popWindowIdV){
        sEdit.putString(popWindowIdK,popWindowIdV);
        sEdit.commit();
    }
    //获取单个比赛的弹窗ID
    public String getPopWindowId(String popWindowIdK,String defaultPopWindowId) {
        return sSharedPreferences.getString(popWindowIdK,defaultPopWindowId);
    }
    //存储单个比赛的时间
    public void putMatchTime(String matchTimeK,String timeV){
        sEdit.putString(matchTimeK,timeV);
        sEdit.commit();
    }
    //获取单个比赛的时间
    public String getMatchTime(String matchTimeK,String defaultTimestamp){
        return sSharedPreferences.getString(matchTimeK,defaultTimestamp);
    }
    //清除SharePreference数据
    public void clearPopData(){
        sEdit.clear().commit();
    }
}
