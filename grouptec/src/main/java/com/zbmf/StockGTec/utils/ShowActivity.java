package com.zbmf.StockGTec.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.zbmf.StockGTec.activity.BigImageActivity;
import com.zbmf.StockGTec.activity.BlogDetailActivity;
import com.zbmf.StockGTec.activity.BlogPingActivity;
import com.zbmf.StockGTec.activity.RedPackgedActivity;
import com.zbmf.StockGTec.activity.WebViewActivity;
import com.zbmf.StockGTec.beans.BlogBean;
import com.zbmf.StockGTec.beans.RedPackgedBean;

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

    public static void startActivity(Activity activity, Bundle aBundle,String targetName){
        Intent intent = new Intent();
        intent.setClassName(activity.getPackageName(),targetName);
        if (null != aBundle) {
            intent.putExtras(aBundle);
        }

        activity.startActivity(intent);
    }

    public static void StartActivity(Activity activity,Intent intent){
        activity.startActivity(intent);
        if(intent!=null){
            intent=null;
        }
        System.gc();
    }
    public static void StartActivity(Context context,Intent intent){
        context.startActivity(intent);
        if(intent!=null){
            intent=null;
        }
        System.gc();
    }
    public static void ShowBigImage(Context context,String img_url){
        intent=new Intent(context, BigImageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("img_url",img_url);
        StartActivity(context,intent);
    }

    public static void showRedBagActivity(FragmentActivity activity, RedPackgedBean red) {
        intent=new Intent(activity, RedPackgedActivity.class);
        intent.putExtra("redpackged",red);
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
     * 博文评论
     * @param blogDetailActivity
     * @param blogBean
     */
    public static void showBlogPingActivity(BlogDetailActivity blogDetailActivity, BlogBean blogBean) {
        intent=new Intent(blogDetailActivity, BlogPingActivity.class);
        intent.putExtra("blogBean",blogBean);
        StartActivity(blogDetailActivity,intent);
    }
}
