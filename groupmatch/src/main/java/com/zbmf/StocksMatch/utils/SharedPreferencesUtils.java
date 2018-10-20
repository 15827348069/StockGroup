package com.zbmf.StocksMatch.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {

    public SharedPreferences sharedpreferences;

    public final static String SELECTDATE="zbmf";
    public final static String CURRENT_ACCOUNT="current_account";
    public final static String PICURL="pic_url";
    public final static String NUM_MATCH="num_match";
    public final static String UPDATEDATE="updated";

    public SharedPreferencesUtils (Context mContext){
        sharedpreferences=mContext.getSharedPreferences(SELECTDATE, 0);
    }

    public SharedPreferences getSharedpreferences() {
        return sharedpreferences;
    }

    public String getAccount(){
        return sharedpreferences.getString(CURRENT_ACCOUNT, "");
    }

    public void setAccount(String phone){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(CURRENT_ACCOUNT, phone);
        editor.commit();
    }

    public SharedPreferences.Editor edit(){
        return sharedpreferences.edit();
    }

    public void setPicUrl(String url){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PICURL,url);
        editor.commit();
    }

    public String getPicUrl(){
        return sharedpreferences.getString(PICURL, "");
    }

    public void setMatchNum(String num){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(NUM_MATCH,num);
        editor.commit();
    }

    public String getMatchNum(){return  sharedpreferences.getString(NUM_MATCH,"500");}

    public void setUpdated(String updated){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(UPDATEDATE,updated);
        editor.commit();
    }

    public String getUpdated(){
        return sharedpreferences.getString(UPDATEDATE,"");
    }
}
