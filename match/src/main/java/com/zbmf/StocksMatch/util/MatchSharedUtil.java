package com.zbmf.StocksMatch.util;

import com.zbmf.StocksMatch.bean.LoginUser;
import com.zbmf.StocksMatch.bean.User;
import com.zbmf.StocksMatch.common.SharedKey;
import com.zbmf.worklibrary.util.SharedpreferencesUtil;

/**
 * Created by xuhao
 * on 2017/11/24.
 * 存储用户信息
 */

public class MatchSharedUtil {
    private static final String defaultValue="";
    public static void saveUser(LoginUser loginUser){
        SharedpreferencesUtil.getInstance().putString(SharedKey.AUTH_TOKEN,loginUser.getAuth_token());
        saveUser(loginUser.getUser());
    }
    public static void saveUser(User user){
        SharedpreferencesUtil.getInstance().putString(SharedKey.USER_ID,String.valueOf(user.getUser_id()));
        SharedpreferencesUtil.getInstance().putString(SharedKey.USER_NAME,user.getUsername());
        SharedpreferencesUtil.getInstance().putString(SharedKey.NICK_NAME,user.getNickname());
        SharedpreferencesUtil.getInstance().putString(SharedKey.TRUE_NAME,user.getTruename());
        SharedpreferencesUtil.getInstance().putString(SharedKey.AVATAR,user.getAvatar());
        SharedpreferencesUtil.getInstance().putString(SharedKey.ROLE,user.getRole());
        SharedpreferencesUtil.getInstance().putString(SharedKey.PHONE,user.getPhone());
        SharedpreferencesUtil.getInstance().putString(SharedKey.MPAY,String.valueOf(user.getMpay()));
        SharedpreferencesUtil.getInstance().putString(SharedKey.IDCARD,user.getIdcard());
    }
    public static void clearUser(){
        SharedpreferencesUtil.getInstance().removeKey(SharedKey.AUTH_TOKEN);
        SharedpreferencesUtil.getInstance().removeKey(SharedKey.USER_ID);
        SharedpreferencesUtil.getInstance().removeKey(SharedKey.USER_NAME);
        SharedpreferencesUtil.getInstance().removeKey(SharedKey.NICK_NAME);
        SharedpreferencesUtil.getInstance().removeKey(SharedKey.TRUE_NAME);
        SharedpreferencesUtil.getInstance().removeKey(SharedKey.AVATAR);
        SharedpreferencesUtil.getInstance().removeKey(SharedKey.ROLE);
        SharedpreferencesUtil.getInstance().removeKey(SharedKey.PHONE);
        SharedpreferencesUtil.getInstance().removeKey(SharedKey.MPAY);
        SharedpreferencesUtil.getInstance().removeKey(SharedKey.IDCARD);
    }
    public static void clearAuthToken(){
        SharedpreferencesUtil.getInstance().removeKey(SharedKey.AUTH_TOKEN);
    }
    public static String AuthToken(){
        return  SharedpreferencesUtil.getInstance().getString(SharedKey.AUTH_TOKEN,defaultValue);
    }
    public static String UserId(){
        return  SharedpreferencesUtil.getInstance().getString(SharedKey.USER_ID,defaultValue);
    }
    public static String UserName(){
        return  SharedpreferencesUtil.getInstance().getString(SharedKey.USER_NAME,defaultValue);
    }
    public static String UserPhone(){
        return  SharedpreferencesUtil.getInstance().getString(SharedKey.PHONE,defaultValue);
    }
    public static String UserAvatar(){
        return  SharedpreferencesUtil.getInstance().getString(SharedKey.AVATAR,defaultValue);
    }
    public static String UserMpay(){
        return  SharedpreferencesUtil.getInstance().getString(SharedKey.MPAY,defaultValue);
    }
    public static String NickName(){
        return  SharedpreferencesUtil.getInstance().getString(SharedKey.NICK_NAME,defaultValue);
    }
    public static String UserIDCard(){
        return SharedpreferencesUtil.getInstance().getString(SharedKey.IDCARD,defaultValue);
    }
}
