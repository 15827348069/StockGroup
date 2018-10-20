package com.zbmf.StocksMatch.view;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.zbmf.StocksMatch.R;
import com.zbmf.worklibrary.glide.GlideCircleTransform;

/**
 * Created by xuhao
 * on 2017/7/18.
 */

public class GlideOptionsManager {

    private static GlideOptionsManager instance;

    private RequestOptions mOptions,mBanner,mAngle,noPlaceholder;

    private GlideOptionsManager() {

    }

    public static GlideOptionsManager getInstance() {
        if(instance == null) {
            synchronized (GlideOptionsManager.class) {
                if(instance == null) {
                    instance = new GlideOptionsManager();
                }
            }
        }
        return instance;
    }
    //获取圆形图像
    public RequestOptions getRequestOptions() {
        if(mOptions == null) {
            mOptions = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.tp)
                    .error(R.drawable.tp)
                    .priority(Priority.HIGH)
                    .transform(new GlideCircleTransform());
        }
        return mOptions;
    }
    //获取比赛的圆形图像
    public RequestOptions getRequestOptionsMatch() {
        if(mOptions == null) {
            mOptions = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.icon_avatar_default)
                    .error(R.drawable.icon_avatar_default)
                    .priority(Priority.HIGH)
                    .transform(new GlideCircleTransform());
        }
        return mOptions;
    }
    //获取轮播banner图 获取方形图像或圆角图片
    public RequestOptions getBannerOptions(int dp) {
        if(mBanner == null) {
            mBanner = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.tp)
                    .error(R.drawable.tp)
                    .priority(Priority.HIGH)
                    .transform(new GlideRoundTransform(dp));
        }
        return mBanner;
    }
    //没有占位图
    public RequestOptions getBitOptionsNoPlaceholder(int dp) {
        if(noPlaceholder == null) {
            noPlaceholder = new RequestOptions()
                    .centerCrop()
                    .error(R.drawable.tp)
                    .priority(Priority.HIGH)
                    .transform(new GlideRoundTransform(dp));
        }
        return noPlaceholder;
    }
    //获取带圆角的图片
    public RequestOptions getAngleOptions(int dp) {
        if(mAngle == null) {
            mAngle = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.tp)
                    .error(R.drawable.tp)
                    .priority(Priority.HIGH)
                    .transform(new GlideRoundTransform(dp));
        }
        return mAngle;
    }


    public RequestOptions getBannerOptions1() {
        if(mBanner == null) {
            mBanner = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.welcome)
                    .error(R.drawable.welcome)
                    .priority(Priority.HIGH)
                    .transform(new GlideRoundTransform(0));
        }
        return mBanner;
    }
}
