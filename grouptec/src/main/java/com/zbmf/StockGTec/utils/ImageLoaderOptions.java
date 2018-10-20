package com.zbmf.StockGTec.utils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.zbmf.StockGTec.R;

/**
 * Created by xuhao on 2016/12/12.
 */
public class ImageLoaderOptions {
    public static DisplayImageOptions avataroptions;
    public static DisplayImageOptions AvatarOptions(){
        synchronized (new Object()) {
            if(avataroptions==null){
                avataroptions = new DisplayImageOptions.Builder()
                        .showStubImage(R.drawable.avatar_default)          // 设置图片下载期间显示的图片
                        .showImageForEmptyUri(R.drawable.avatar_default)  // 设置图片Uri为空或是错误的时候显示的图片
                        .showImageOnFail(R.drawable.avatar_default)       // 设置图片加载或解码过程中发生错误显示的图片
                        .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                        .cacheOnDisc(false)                          // 设置下载的图片是否缓存在SD卡中
                        .build();
            }
        }
        return avataroptions;
    }
    public static DisplayImageOptions progressoptions;
    public static DisplayImageOptions ProgressOptions(){
        synchronized (new Object()) {
            if(progressoptions==null){
                progressoptions = new DisplayImageOptions.Builder()
                        .showStubImage(R.drawable.loading)          // 设置图片下载期间显示的图片
                        .showImageForEmptyUri(R.drawable.avatar_default)  // 设置图片Uri为空或是错误的时候显示的图片
                        .showImageOnFail(R.drawable.avatar_default)       // 设置图片加载或解码过程中发生错误显示的图片
                        .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                        .cacheOnDisc(false)                          // 设置下载的图片是否缓存在SD卡中
                        .build();
            }
        }
        return progressoptions;
    }
    public static DisplayImageOptions bigprogressoptions;
    public static DisplayImageOptions BigProgressOptions(){
        synchronized (new Object()) {
            if(bigprogressoptions==null){
                bigprogressoptions = new DisplayImageOptions.Builder()
                        .showStubImage(R.drawable.big_loading)          // 设置图片下载期间显示的图片
                        .showImageForEmptyUri(R.drawable.avatar_default)  // 设置图片Uri为空或是错误的时候显示的图片
                        .showImageOnFail(R.drawable.avatar_default)       // 设置图片加载或解码过程中发生错误显示的图片
                        .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                        .cacheOnDisc(false)                          // 设置下载的图片是否缓存在SD卡中
                        .build();
            }
        }
        return bigprogressoptions;
    }

    public static DisplayImageOptions sDisplayImageOptions;
    public static DisplayImageOptions Options(){
        synchronized (new Object()) {
            if(sDisplayImageOptions==null){
                sDisplayImageOptions = new DisplayImageOptions.Builder()
                        .showStubImage(R.color.white)          // 设置图片下载期间显示的图片
                        .showImageForEmptyUri(R.color.white)  // 设置图片Uri为空或是错误的时候显示的图片
                        .showImageOnFail(R.color.white)       // 设置图片加载或解码过程中发生错误显示的图片
                        .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                        .cacheOnDisc(false)                          // 设置下载的图片是否缓存在SD卡中
                        .build();
            }
        }
        return sDisplayImageOptions;
    }
}
