package com.zbmf.groupro.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.zbmf.groupro.activity.AddFansActivity;
import com.zbmf.groupro.activity.BigImageActivity;
import com.zbmf.groupro.activity.BlogDetailActivity;
import com.zbmf.groupro.activity.BlogPingActivity;
import com.zbmf.groupro.activity.BoxDetailActivity;
import com.zbmf.groupro.activity.BoxDetailWebActivity;
import com.zbmf.groupro.activity.Chat1Activity;
import com.zbmf.groupro.activity.ConponsActivity;
import com.zbmf.groupro.activity.FansActivity;
import com.zbmf.groupro.activity.GroupDetailActivity;
import com.zbmf.groupro.activity.HistoryDateActivity;
import com.zbmf.groupro.activity.LiveHistoryActivity;
import com.zbmf.groupro.activity.LoginActivity;
import com.zbmf.groupro.activity.MyDetailActivity;
import com.zbmf.groupro.activity.PayDetailActivity;
import com.zbmf.groupro.activity.RedPackgedActivity;
import com.zbmf.groupro.activity.SettingActivity;
import com.zbmf.groupro.activity.ThirdPartyLogin;
import com.zbmf.groupro.activity.WebViewActivity;
import com.zbmf.groupro.beans.BlogBean;
import com.zbmf.groupro.beans.BoxBean;
import com.zbmf.groupro.beans.CouponsBean;
import com.zbmf.groupro.beans.Group;
import com.zbmf.groupro.beans.RedPackgedBean;

/**
 * Created by xuhao on 2016/12/14.
 */

public class ShowActivity {
    public  static Intent intent=null;
    public static void showActivity(Activity activity, Class<?> c){
        intent=new Intent(activity,c);
        StartActivity(activity,intent);
    }
    public static void showActivity(Context context, Class<?> c){
        intent=new Intent(context,c);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        StartActivity(context,intent);
    }
    public static void StartActivity(Activity activity,Intent intent){
        activity.startActivity(intent);
        if(intent!=null){
            intent=null;
        }
    }
    public static void StartActivityForResult(Activity activity,Intent intent,int code){
        activity.startActivityForResult(intent,code);
        if(intent!=null){
            intent=null;
        }
    }

    public static void showActivity(Activity activity, Bundle aBundle, String targetName){
        Intent intent = new Intent();
        intent.setClassName(activity.getPackageName(),targetName);
        if (null != aBundle) {
            intent.putExtras(aBundle);
        }

        activity.startActivity(intent);
    }

    public static void StartActivity(Context context,Intent intent){
        context.startActivity(intent);
        if(intent!=null){
            intent=null;
        }
        System.gc();
    }
    public static void showPayDetailActivity(Activity activity)
    {
        intent=new Intent(activity, PayDetailActivity.class);
        StartActivity(activity,intent);
    }

    /**
     * 大图
     * @param context
     * @param img_url
     */
    public static void ShowBigImage(Context context,String img_url){
        intent=new Intent(context, BigImageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("img_url",img_url);
        StartActivity(context,intent);
    }

    /**
     * 红包页面
     * @param activity
     * @param red
     */
    public static void showRedBagActivity(FragmentActivity activity, RedPackgedBean red) {
        intent=new Intent(activity, RedPackgedActivity.class);
        intent.putExtra("redpackged",red);
        StartActivity(activity,intent);
    }

    /**
     * 直播室
     * @param activity
     */
    public static void showChatActivity(Activity activity,Group group){
        intent=new Intent(activity, Chat1Activity.class);
        intent.putExtra("GROUP",group);
        StartActivity(activity,intent);
    }
    /**
     * 圈子简介
     * @param activity
     */
    public static void showGroupDetailActivity(Activity activity,Group group){
        intent=new Intent(activity, GroupDetailActivity.class);
        intent.putExtra("GROUP",group);
        StartActivity(activity,intent);
    }
    /**
     * 我的资料
     * @param activity
     */
    public static void showMyDetail(Activity activity){
        intent=new Intent(activity, MyDetailActivity.class);
        StartActivity(activity,intent);
    }
    /**
     * 优惠券
     * @param activity
     * @param couponsBean
     */
    public static void showConponsActivity(Activity activity,CouponsBean couponsBean){
        intent=new Intent(activity, ConponsActivity.class);
        intent.putExtra("CouponsBean",couponsBean);
        StartActivity(activity,intent);
    }
    /**
     * 直播室历史纪录
     * @param activity
     */
    public static void showLiveHistoryActivity(Activity activity,Group group){
        intent=new Intent(activity, LiveHistoryActivity.class);
        intent.putExtra("GROUP",group);
        StartActivity(activity,intent);
    }
    /**
     * 直播室历史纪录日期
     * @param activity
     */
    public static void showLiveHistoryDateActivity(Activity activity,Group group){
        intent=new Intent(activity, HistoryDateActivity.class);
        intent.putExtra("GROUP",group);
        StartActivity(activity,intent);
    }
    /**
     * 铁粉专区
     * @param activity
     * @param
     */
    public static void showFansActivity(Activity activity,Group group){
        intent=new Intent(activity, FansActivity.class);
        intent.putExtra("GROUP",group);
        StartActivityForResult(activity,intent,Constants.FANS_MESSAGE);
    }
    /**
     * 加入铁粉专区
     * @param activity
     * @param
     */
    public static void showAddFansActivity(Activity activity,Group group){
        intent=new Intent(activity, AddFansActivity.class);
        intent.putExtra("GROUP",group);
        StartActivityForResult(activity,intent,Constants.ADD_FANS);
    }
    /**
     * 铁粉宝盒简介专区
     * @param activity
     * @param boxBean
     */
    public static void showBoxDetailDescActivity(Activity activity, BoxBean boxBean){
        intent=new Intent(activity, BoxDetailActivity.class);
        intent.putExtra("BoxBean",boxBean);
        StartActivity(activity,intent);
    }
    /**
     * 铁粉宝盒详情专区
     * @param activity
     * @param boxBean
     */
    public static void showBoxDetailActivity(Activity activity, BoxBean boxBean){
        intent=new Intent(activity, BoxDetailWebActivity.class);
        intent.putExtra("BoxBean",boxBean);
        StartActivity(activity,intent);
    }
    /**
     * 铁粉宝盒详情专区
     * @param activity
     */
    public static void showBoxDetailActivity(Context activity, String group_id,String box_id){
        intent=new Intent(activity, BoxDetailWebActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("group_id",group_id);
        intent.putExtra("box_id",box_id);
        StartActivity(activity,intent);
    }
    /**
     * WebView页面
     * @param activity
     * @param url
     */
    public static void showWebViewActivity(Activity activity, String url){
        intent=new Intent(activity, WebViewActivity.class);
        intent.putExtra("web_url",url);
        StartActivity(activity,intent);
    }
    /**
     * WebView页面
     * @param activity
     * @param blogBean
     */
    public static void showBlogDetailActivity(Activity activity, BlogBean blogBean){
        intent=new Intent(activity, BlogDetailActivity.class);
        intent.putExtra("blogBean",blogBean);
        Log.e("blog_url>>>>",blogBean.getApp_link());
        StartActivity(activity,intent);
    }
    /**
     * WebView页面
     * @param activity
     * @param blogBean
     */
    public static void showBlogDetailActivity(Context activity, BlogBean blogBean){
        intent=new Intent(activity, BlogDetailActivity.class);
        intent.putExtra("blogBean",blogBean);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        StartActivity(activity,intent);
    }
    /**
     * 第三方登陆绑定手机号
     * @param activity
     */
    public static void showBindphoneActivity(Activity activity){
        intent=new Intent(activity, ThirdPartyLogin.class);
        StartActivity(activity,intent);
    }

    /**
     * 设置页面
     * @param activity
     */
    public static void showSettingActivity(Activity activity){
        intent=new Intent(activity, SettingActivity.class);
        StartActivity(activity,intent);
    }

    /**
     * 博文评论
     * @param blogDetailActivity
     * @param blogBean
     */
    public static void showBlogPingActivity(BlogDetailActivity blogDetailActivity, BlogBean blogBean) {
        intent=new Intent(blogDetailActivity, BlogPingActivity.class);
        intent.putExtra("blogBean",blogBean);
        StartActivity(blogDetailActivity,intent);
    }
    public static boolean isLogin(Activity activity){
        if(TextUtils.isEmpty(SettingDefaultsManager.getInstance().authToken())){
            Toast.makeText(activity,"请您先登录",Toast.LENGTH_SHORT).show();
            intent=new Intent(activity, LoginActivity.class);
            StartActivity(activity,intent);
            return false;
        }else{
            return true;
        }
    }
}
