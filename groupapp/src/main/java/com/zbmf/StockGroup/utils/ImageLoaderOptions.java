package com.zbmf.StockGroup.utils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zbmf.StockGroup.R;

/**
 * Created by xuhao on 2016/12/12.
 */
public class ImageLoaderOptions {
    public static DisplayImageOptions avataroptions;
    public static DisplayImageOptions AvatarOptions(){
        synchronized (new Object()) {
            if(avataroptions==null){
                avataroptions = new DisplayImageOptions.Builder()
                        .displayer(new RoundedBitmapDisplayer(500))
                        .showImageOnLoading(R.drawable.avatar_default)
                        .showStubImage(R.drawable.avatar_default)          // 设置图片下载期间显示的图片
                        .showImageForEmptyUri(R.drawable.avatar_default)  // 设置图片Uri为空或是错误的时候显示的图片
                        .showImageOnFail(R.drawable.avatar_default)       // 设置图片加载或解码过程中发生错误显示的图片
                        .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                        .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
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
                        .showImageOnLoading(R.drawable.dia_loading)
                        .showStubImage(R.drawable.loading)          // 设置图片下载期间显示的图片
                        .showImageForEmptyUri(R.drawable.avatar_default)  // 设置图片Uri为空或是错误的时候显示的图片
                        .showImageOnFail(R.drawable.avatar_default)       // 设置图片加载或解码过程中发生错误显示的图片
                        .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                        .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
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
                        .showImageOnLoading(R.drawable.dia_loading)
                        .showStubImage(R.drawable.dia_loading)          // 设置图片下载期间显示的图片
                        .showImageForEmptyUri(R.drawable.dia_loading)  // 设置图片Uri为空或是错误的时候显示的图片
                        .showImageOnFail(R.drawable.dia_loading)       // 设置图片加载或解码过程中发生错误显示的图片
                        .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                        .cacheOnDisk(true)                          // 设置下载的图片是否缓存在SD卡中
                        .build();
            }
        }
        return bigprogressoptions;
    }
    public static DisplayImageOptions roundedBitMapoptios;
    public static DisplayImageOptions RoundedBitMapoptios(){
        synchronized (new Object()){
            if (roundedBitMapoptios == null) {
                roundedBitMapoptios= new DisplayImageOptions.Builder()
                        .showImageForEmptyUri(R.drawable.default_image)  // 设置图片Uri为空或是错误的时候显示的图片
                        .showImageOnFail(R.drawable.default_image)       // 设置图片加载或解码过程中发生错误显示的图片
                        .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                        .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                        .displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                        .showStubImage(R.drawable.banner_loading)     // 设置图片下载期间显示的图片
                        .showImageOnFail(R.drawable.default_image)
                        .build();
            }
        }
        return roundedBitMapoptios;
    }
}
